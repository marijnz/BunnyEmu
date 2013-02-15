/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bunnyEmu.main.entities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import bunnyEmu.main.Server;
import bunnyEmu.main.net.WorldConnection;
import bunnyEmu.main.utils.Constants;
import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.Opcodes;
import bunnyEmu.main.utils.PacketMap;

/**
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
		this(1, "Marijnz ultimate server", Server.localIP, 3456, Constants.VERSION_WOTLK);
	}

	public Realm(int id, String name, String address, int port, int version) {
		this.id = id;
		this.name = "[" + version + "]" + name ;
		this.address = address + ":" + String.valueOf(port);
		Log.log(this.address);
		Log.log(port);
		this.port = port;
		this.version = version;
		if(version <= Constants.VERSION_WOTLK)
			packets = Opcodes.formWotLK();
		else if(version <= Constants.VERSION_CATA)
			packets = Opcodes.formCata();
		else if(version <= Constants.VERSION_MOP)
			packets = Opcodes.formMoP();
		start();
		
		Log.log("Created new realm: " + this.name);
	}

	public int getSize() {
		return 8 + 4 + address.length() + name.length();
	}

	@Override
	public void run() {
		try {
			listenSocket();
		} catch (IOException ex) {
			Log.log(Log.ERROR, "Couldn't create a listensocket for realm " + id);
		}
	}

	private void listenSocket() throws IOException {
		socket = new ServerSocket(port);

		while (true) {
			new WorldConnection(socket.accept(), this);
			Log.log(Log.INFO, "Connection made to realm " + id);
		}
	}

	public void addClient(Client client) {
		clients.add(client);
	}

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
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getName().equals(name))
				return clients.get(i);
		}
		return null;
	}

	public int getVersion() {
		return version;
	}
	
	public String getVersionName(){
		if(this.version <= Constants.VERSION_VANILLA)
			return "Vanilla";
		if(this.version <= Constants.VERSION_BC)
			return "BC";
		if(this.version <= Constants.VERSION_WOTLK)
			return "WotLK";
		if(this.version <= Constants.VERSION_CATA)
			return "Cata";
		if(this.version <= Constants.VERSION_MOP)
			return "MoP";
		else
			return null;
	}

	public PacketMap getPackets() {
		return packets;
	}
	
	 /**
     * Loading a packet text file assuming an arcemu-like packet dump
     * @param packetDir The packet to be loaded
     * @param capacity	How much size should be buffered for the returned data
     */
    public ServerPacket loadPacket(String packetDir, int capacity){
    	Log.log("loading packet");
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
    	
    	Log.log(data.toString());
    	ServerPacket p;
    	try{
    		p = new ServerPacket(packets.getOpcodeName(Short.parseShort(opcode, 16)), data);
    	} catch (NumberFormatException e){
    		p = new ServerPacket(Opcodes.SMSG_COMPRESSED_UPDATE_OBJECT, data);
    	}
    	return p;
    	
    }

}
