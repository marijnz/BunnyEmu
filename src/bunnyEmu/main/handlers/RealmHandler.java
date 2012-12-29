package bunnyEmu.main.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import bunnyEmu.main.Server;
import bunnyEmu.main.entities.AuthServerPacket;
import bunnyEmu.main.entities.Client;
import bunnyEmu.main.entities.Realm;
import bunnyEmu.main.utils.Log;

public class RealmHandler {
	
    private static ArrayList<Realm> realms = new ArrayList<Realm>(10);
    
    /**
     * A list of all clients that hasn't connected to a realm yet, as soon a Client connects to a realm it gets removed from this list
     */
    private static HashMap<String, Client> temporaryClients = new HashMap<String, Client>();
    
    /**
     * @return The realmlist packet, version dependable
     */
	public static AuthServerPacket getRealmList(){
		short size = 8;
        for (int i = 0; i < realms.size(); i++) 
            size += realms.get(i).getSize();
        
        AuthServerPacket realmPacket = new AuthServerPacket((short) (size + 3));
        
        realmPacket.put((byte) 0x10);          // Packet header (16 in hex)
        realmPacket.putShort(size);       // Size Placeholder
        realmPacket.putInt(0);                 // unknown?
        realmPacket.putShort((short) realms.size());       // Realm count
       
        // all realms
        for (Realm realm : realms) {
        	realmPacket.put((byte) 4); 
        	realmPacket.put((byte) 0); 
        	realmPacket.put((byte) realm.flags);   
        	realmPacket.putString(realm.name);      // Name
        	realmPacket.putString(realm.address);   // Address
        	realmPacket.putFloat(realm.population);      // Population
        	realmPacket.put((byte) (Math.random() * 10)); // char count

        	realmPacket.put((byte) 1);        // ??
        	realmPacket.put((byte) 1);        // ??
        }
        
        realmPacket.putShort((short) 0x0010);
        return realmPacket;
	}
	
	/**
	 * Adding a realm to the realmlist
	 * @param realm The realm
	 */
	public static void addRealm(Realm realm){
		realms.add(realm);
	}
	
	/**
	 * Creates a new realm for the given version if it doesn't exist already
	 * @param version The WoW client version
	 */
	public static void addVersionRealm(int version){
		for(Realm realm : realms)
			if(realm.getVersion() == version)
				return;
		realms.add(new Realm(realms.size(), "Version realm", Server.localIP, 3456 + realms.size(), version));
	}
	
	/**
	 * @param client An authenticated client which isn't connected to a realm
	 * @return Returns false if there already is a connected client with this name
	 */
	
	public static boolean addTempClient(Client client){
		Log.log("Adding temporary client: " + client.getName());
		if(temporaryClients.containsKey(client.getName()))
			return false;
		temporaryClients.put(client.getName(), client);
		return true;
	}
	
	/**
	 * @param name The username of the client
	 * @return The client which also gets removed from the temporary clients list
	 */
	public static Client getTempClient(String name){
		return temporaryClients.remove(name);
	}
	
	/**
	 * Search for a client in all created realms and temporary unconnected clients
	 */
	public static Client findClient(String name){
		for(Realm realm : realms){
			Client client = realm.getClient(name);
			if(client != null){
				Log.log("got client: " + name);
				return client;
			}
				
		}
		return temporaryClients.get(name);
	}
	
	
}
