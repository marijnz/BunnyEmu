/*
 * BunnyEmu - A Java WoW sandbox/emulator
 * https://github.com/marijnz/BunnyEmu
 */
package bunnyEmu.main.entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import bunnyEmu.main.Server;
import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.enums.ClientVersions;
import bunnyEmu.main.net.WorldConnection;
import bunnyEmu.main.utils.Logger;
import bunnyEmu.main.utils.Opcodes;
import bunnyEmu.main.utils.PacketMap;

/**
 * A realm that has to be added to the RealmHandler.
 * 
 * @author Marijn
 */
public class Realm extends Thread {
	public int id;
	public String name;
	public String address;
	public int port;
	public int icon;
	public int flags = 0;
	public int timezone;
	public float population = 0;
	private int version;
	private PacketMap packets;

	private ArrayList<Client> clients = new ArrayList<Client>(10);

	ServerSocket socket = null;

	public Realm() {
		this(1, "Marijnz ultimate server", Server.realmlist, 3456, ClientVersions.VERSION_WOTLK.getNumber());
	}

	/**
	 * Instantiates a new realm, usually only created through the RealmHandler.
	 * 
	 * @param id An unique id.
	 * @param name The name of the ream how it should be listed in the realmlist.
	 * @param address The address of the worldsocket, usually same host as the logonsocket.
	 * @param port The port of the worldsocket.
	 * @param version The version of the realm, see Readme for up-to-date version support.
	 */
	public Realm(int id, String name, String address, int port, int version) {
		this.id = id;
		this.name = "[" + version + "]" + name ;
		this.address = address + ":" + String.valueOf(port);
		this.port = port;
		this.version = version;
		
		if(version <= ClientVersions.VERSION_WOTLK.getNumber())
			packets = Opcodes.formWotLK();
		else if(version <= ClientVersions.VERSION_CATA.getNumber())
			packets = Opcodes.formCata();
		else if(version <= ClientVersions.VERSION_MOP.getNumber())
			packets = Opcodes.formMoP();
		start();
		
		System.out.println("Created new realm: " + this.name);
	}

	/**
	 * @return The size of the realm, only used for the realmlist packet.
	 */
	public int getSize() {
		return 8 + 4 + address.length() + name.length();
	}

	@Override
	public void run() {
		try {
			listenSocket();
		} catch (IOException ex) {
			Logger.writeError("Couldn't create a listening socket for realm " + id);
		}
	}

	private void listenSocket() throws IOException {
		socket = new ServerSocket(port);

		while (true) {
			// TODO: Keep track on worldconnections in case we want to support multiple clients to interact. 
			new WorldConnection(socket.accept(), this);
			System.out.println("Connection made to realm " + id);
		}
	}

	/**
	 * Add a new client to the realm, usually done after the client has been connected to it.
	 * @param client The connected client
	 */
	public void addClient(Client client) {
		clients.add(client);
	}

	/**
	 * Removes a client from the realm, usually done after the client has been disconnected.
	 * @param The disconnected client 
	 */
	public void removeClient(Client client) {
		clients.remove(client);
	}

	/**
	 * Get a client connected to this realm
	 * 
	 * @param name The name of the client
	 * @return
	 */
	public Client getClient(String name) {
		name = name.toUpperCase();
		for(Client client : clients)
			if (client.getName().equals(name))
				return client;
		return null;
	}
	
	public ArrayList<Client> getAllClients(){
		return clients;
	}

	/**
	 * Send a packet to all connected clients except for passed client
	 */
	public void sendAllClients(ServerPacket p, Client ignoreClient){
		Logger.writeError("Ignore client: " + ignoreClient.getName());
		for(Client client : clients)
			if(!client.equals(ignoreClient)){
				System.out.println("Sending packet " + p.sOpcode + " to client: " + client.getName());
				client.getWorldConnection().send(p);
			}
	}
	/**
	 * @return The version of this realm, can be used to build packets for specific versions.
	 */
	public int getVersion() {
		return version;
	}
	
	public String getVersionName(){
		if(this.version <= ClientVersions.VERSION_VANILLA.getNumber())
			return "Vanilla";
		if(this.version <= ClientVersions.VERSION_BC.getNumber())
			return "BC";
		if(this.version <= ClientVersions.VERSION_WOTLK.getNumber())
			return "WotLK";
		if(this.version <= ClientVersions.VERSION_CATA.getNumber())
			return "Cata";
		if(this.version <= ClientVersions.VERSION_MOP.getNumber())
			return "MoP";
		else
			return null;
	}


	/**
	 * @return The packets that belong to the version this realm has been assigned.
	 */
	public PacketMap getPackets() {
		return packets;
	}
	
	 /**
     * Loading a packet text file assuming an arcemu-like packet dump
     * @param packetDir The packet to be loaded
     * @param capacity	How much size should be buffered for the returned data
     * 
	  * TODO: Make it logging style independent
     */
    public ServerPacket loadPacket(String packetDir, int capacity){
    	Logger.writeError("loading packet");
    	String opcode = null;
    	ByteBuffer data = ByteBuffer.allocate(capacity);
    	try {
    		BufferedReader in = new BufferedReader(new FileReader("assets" + "/" + packetDir));
            String line = "";
            line = in.readLine(); // opcode and info line
            int firstHook = line.indexOf("(")+3;

            opcode = line.substring(firstHook, line.indexOf(")", firstHook));
            for (int i = 0; i < 3; i++)
            	in.readLine(); // unused text
            
            // "|" = start or end of line
            while((line = in.readLine()).charAt(0) == '|'){
            	String curHexChar = "";
            	int i = 1; // Skip the first "|"
            	while(true){
            		curHexChar = line.substring(i, i + 2);
            		i += 3;
            		if(curHexChar.contains(" ") || curHexChar.contains("|"))
            			break;
            		data.put((byte) Integer.parseInt(curHexChar, 16)); // Read two bytes, hex
            	}
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	Logger.writeError(data.toString());
    	ServerPacket p;
    	try{
    		p = new ServerPacket(packets.getOpcodeName(Short.parseShort(opcode, 16)), data);
    	} catch (NumberFormatException e){
    		p = new ServerPacket(Opcodes.SMSG_COMPRESSED_UPDATE_OBJECT, data);
    	}
    	return p;
    	
    }

}
