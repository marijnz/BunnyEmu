package bunnyEmu.main.handlers;

import java.util.HashMap;

import bunnyEmu.main.entities.Client;
import bunnyEmu.main.entities.Realm;
import bunnyEmu.main.utils.Log;

/**
 * Handles all connected clients
 * 
 * @author Marijn
 *
 */
public class TempClientHandler {

	
	 /**
     * A list of all clients that haven't connected to a realm yet, as soon a client connects to a realm it gets removed from this list
     */
    private static HashMap<String, Client> temporaryClients = new HashMap<String, Client>();

	
	/**
	 * @param client An authenticated client which isn't connected to a realm
	 * @return Returns false if there already is a connected client with this name
	 */
	
	public static boolean addTempClient(Client client){
		Log.log(Log.DEBUG, "Adding temporary client: " + client.getName());
		if(temporaryClients.containsKey(client.getName()))
			return false;
		temporaryClients.put(client.getName(), client);
		return true;
	}
	
	/**
	 * @param name The username of the client
	 * @return The removed client, null if no client is attached to the given name
	 */
	public static Client removeTempClient(String name){
		return temporaryClients.remove(name);
	}
	
	/**
	 * Search for a client in all created realms and temporary unconnected clients
	 */
	public static Client findClient(String name){
		// First check through all realms
		for(Realm realm : RealmHandler.getRealms()){
			Client client = realm.getClient(name);
			if(client != null){
				Log.log(Log.DEBUG, "Found client: " + name);
				return client;
			}
		}
		// Then in the temporary client map, null if there is none
		return temporaryClients.get(name);
	}
}
