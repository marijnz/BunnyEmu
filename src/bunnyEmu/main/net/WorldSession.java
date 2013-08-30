package bunnyEmu.main.net;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import bunnyEmu.main.entities.Client;
import bunnyEmu.main.entities.Realm;
import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.entities.packet.ClientPacket;
import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.net.packets.client.CMSG_MESSAGECHAT;
import bunnyEmu.main.net.packets.client.CMSG_PLAYER_LOGIN;
import bunnyEmu.main.net.packets.server.SMSG_ACCOUNT_DATA_TIMES;
import bunnyEmu.main.net.packets.server.SMSG_CHAR_ENUM;
import bunnyEmu.main.net.packets.server.SMSG_KNOWN_SPELLS;
import bunnyEmu.main.net.packets.server.SMSG_LOGIN_VERIFY_WORLD;
import bunnyEmu.main.net.packets.server.SMSG_MESSAGECHAT;
import bunnyEmu.main.net.packets.server.SMSG_MOTD;
import bunnyEmu.main.net.packets.server.SMSG_MOVE_SET_CANFLY;
import bunnyEmu.main.net.packets.server.SMSG_NAME_CACHE;
import bunnyEmu.main.net.packets.server.SMSG_NAME_QUERY_RESPONSE;
import bunnyEmu.main.net.packets.server.SMSG_NEW_WORLD;
import bunnyEmu.main.net.packets.server.SMSG_PONG;
import bunnyEmu.main.net.packets.server.SMSG_REALM_CACHE;
import bunnyEmu.main.net.packets.server.SMSG_UPDATE_OBJECT_CREATE;
import bunnyEmu.main.utils.AuthCodes;
import bunnyEmu.main.utils.BitPack;
import bunnyEmu.main.utils.BitUnpack;
import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.Opcodes;
import bunnyEmu.main.utils.Versions;

/**
 * Used after world authentication, handles incoming packets.
 * TODO: Handle packets in different classes (when things are getting on larger scale)
 * 
 * @author Marijn
 *
 */

public class WorldSession {
	private WorldConnection connection;
	private Realm realm;
	
	private final String randomNameLexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final java.util.Random rand = new java.util.Random();

	public WorldSession(WorldConnection c, Realm realm) {
		connection = c;
		this.realm = realm;
	}

	/**
	 * Send the character list to the client.
	 */
	public void sendCharacters() {
		Log.log(Log.DEBUG, "sending chars");
		connection.send(new SMSG_CHAR_ENUM(connection.getClient()));
	}

	/**
	 * Creates a character for the client and sets attributes.
	 * @throws UnsupportedEncodingException 
	 *
	 *
	 */
	public void createCharacter(ClientPacket p) throws UnsupportedEncodingException {

		byte cHairStyle = p.get();
		byte cFaceStyle  = p.get();
		byte cFacialHair = p.get();
		byte cHairColor  = p.get();
		byte cRace      = p.get();
		byte cClass     = p.get();
		byte cSkinColor  = p.get();
		
		p.get();
		
		byte cGender     = p.get();
		
		byte nameLength = p.get();
	    StringBuilder builder = new StringBuilder();
		
		// length/4 is amount of characters in ASCII
		for (int x = 0; x < nameLength/4; x++) {
			builder.append(new String(new byte[] { p.get() }, "US-ASCII"));
		}
		
		/* capitalize name */
		String cName = builder.toString().toLowerCase();
		char[] chars = cName.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		cName = String.valueOf(chars);
		
		ServerPacket isCharOkay = new ServerPacket(Opcodes.SMSG_CHAR_CREATE, 1);

		isCharOkay.put((byte) 0x31);	// name is okay to be used \\ 0x32 means not okay
		connection.send(isCharOkay);

		/* this needs to be sent immediately after previous packet otherwise client fails */
		connection.send(new ServerPacket(Opcodes.SMSG_CHAR_CREATE, 1, AuthCodes.CHAR_CREATE_SUCCESS));


		/* TODO: need database query to insert and for start position and map here */

		float x = 0.0f;
		float y = 0.0f;
		float z = 100.0f;
		
		int mapID = 0;
		
		/* this value will come from a configuration file */
		int cStartLevel = 1;
		
		connection.getClient().addCharacter(new Char(cName, x, y, z, mapID, cHairStyle, 
														cFaceStyle, cFacialHair, cHairColor,
														cSkinColor, cRace, cClass, cGender,
														cStartLevel));

		Log.log(Log.DEBUG, "Created new char with name: " + cName);
	}

	/* delete the specified character */
	public void deleteCharacter(ClientPacket p) {
	
		/* determine guid here from packet then respond okay */
		boolean[] guidMask = new boolean[8];
		byte[] guidBytes = new byte[8];
		
		BitUnpack bitUnpack = new BitUnpack(p);

		guidMask[2] = bitUnpack.getBit();
		guidMask[1] = bitUnpack.getBit();
		guidMask[5] = bitUnpack.getBit();
		guidMask[7] = bitUnpack.getBit();
		guidMask[6] = bitUnpack.getBit();

		bitUnpack.getBit();

		guidMask[3] = bitUnpack.getBit();
		guidMask[0] = bitUnpack.getBit();
		guidMask[4] = bitUnpack.getBit();

        if (guidMask[1]) 
        	guidBytes[1] = (byte) (p.get() ^ 1);
        
        if (guidMask[3]) 
        	guidBytes[3] = (byte) (p.get() ^ 1);
        
        if (guidMask[4])
        	guidBytes[4] = (byte) (p.get() ^ 1);
        
        if (guidMask[0])
        	guidBytes[0] = (byte) (p.get() ^ 1);
        
        if (guidMask[7])
        	guidBytes[7] = (byte) (p.get() ^ 1);
        
        if (guidMask[2])
        	guidBytes[2] = (byte) (p.get() ^ 1);
        
        if (guidMask[5])
        	guidBytes[5] = (byte) (p.get() ^ 1);
        
        if (guidMask[6])
        	guidBytes[6] = (byte) (p.get() ^ 1);
        
        
        ByteBuffer buffer = ByteBuffer.wrap(guidBytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int guid = buffer.getInt();
        
        boolean charDeletion = connection.getClient().removeCharacter(guid);
        
        if (charDeletion) {
        	Log.log(Log.DEBUG, "Deleted character with GUID = " + guid);
        }
        else {
        	Log.log(Log.DEBUG, "Failed to delete character with GUID = " + guid);
        }
        
        ServerPacket charDeleteOkay = new ServerPacket(Opcodes.SMSG_CHAR_DELETE, 1);
        charDeleteOkay.put((byte) 0x47);	// success

        connection.send(charDeleteOkay);
	}
	
	/**
	 * Sends initial packets after world login has been confirmed.
	 */
	public void verifyLogin(CMSG_PLAYER_LOGIN p) {

		final Char character = connection.getClient().setCurrentCharacter(p.getGuid());
		
		if (character == null) { 
			Log.log(Log.DEBUG, "\nPROBLEM: Character is null at login to world..\n");
		}
		
		connection.send(new SMSG_LOGIN_VERIFY_WORLD(character));
		connection.send(new SMSG_KNOWN_SPELLS(character));
		character.setCharSpeed(15);
		
		// Set the update fields, required for update packets
		character.setUpdateFields(realm);

		// Currently only fully supports MoP
		if (realm.getVersion() <= Versions.VERSION_BC)
			connection.send(realm.loadPacket("updatepacket_bc", 5000));
		else if (realm.getVersion() <= Versions.VERSION_WOTLK)
			//connection.send(realm.loadPacket("updatepacket_wotlk", 2500));
			connection.send(new SMSG_UPDATE_OBJECT_CREATE(this.connection.getClient(), true));
		else if (realm.getVersion() <= Versions.VERSION_CATA)
			connection.send(realm.loadPacket("updatepacket_cata", 500));
		else
			connection.send(new SMSG_UPDATE_OBJECT_CREATE(this.connection.getClient(), true));
			//connection.send(realm.loadPacket("updatepacket_mop", 500));
		
		
		//connection.send(new SMSG_MOVE_SET_CANFLY(character));

		sendAccountDataTimes(0xAA);
		sendMOTD("Welcome to BunnyEmu, have fun exploring!");
		// connection.send(new SMSG_MOVE_SET_CANFLY(character));
		//sendSpellGo(); // Shiny start
		realm.sendAllClients(new SMSG_UPDATE_OBJECT_CREATE(connection.getClient(), false), connection.getClient());
		for(Client client : realm.getAllClients())
			if(!client.equals(connection.getClient()))
				connection.send(new SMSG_UPDATE_OBJECT_CREATE(client, false));
			
	}

	/**
	 * Synch the server and client times.
	 */
	public void sendAccountDataTimes(int mask) {
		connection.send(new SMSG_ACCOUNT_DATA_TIMES(mask));
		if (this.realm.getVersion() <= Versions.VERSION_CATA)
			connection.send(new ServerPacket(Opcodes.SMSG_TIME_SYNC_REQ, 4));
	}

	/**
	 * Send a message to the player in Message Of The Day style
	 */
	public void sendMOTD(String message) {
		connection.send(new SMSG_MOTD(message));
	}

	/**
	 * Response to Ping
	 */
	public void sendPong() {
		connection.send(new SMSG_PONG());
	}

	/**
	 * Response the name request
	 */
	public void sendNameResponse() {
		connection.send(new SMSG_NAME_QUERY_RESPONSE(connection.client.getCurrentCharacter()));
	}
	
	/**
	 * Character data? Required for MoP
	 */
	public void handleNameCache(ClientPacket p){
		//long guid = p.getLong();
		//Log.log("GUID: " + guid);
		
		connection.send(new SMSG_NAME_CACHE(connection.client.getCurrentCharacter()));
	}
	
	/**
	 * Realm data? Required for MoP
	 */
	public void handleRealmCache(ClientPacket p) {
		int realmId = p.getInt();
		if(realm.id != realmId)
			return;
		
		connection.send(new SMSG_REALM_CACHE(realm));
	}
	
	/**
	 * Handles a chat message given by the client, checks for commands.
	 */
	public void handleChatMessage(CMSG_MESSAGECHAT p) {
		Char character = connection.client.getCurrentCharacter();
		Log.log("msg: " + p.getMessage());
        connection.send(new SMSG_MESSAGECHAT(connection.client.getCurrentCharacter(), p.getLanguage(), p.getMessage()));
         
         try {
	         if (p.getMessage().contains(".tele")) {
	        	 String[] coords = p.getMessage().split("\\s");
	        	 int mapId = Integer.parseInt(coords[1]);
	        	 float x = Float.parseFloat(coords[2]);
	        	 float y = Float.parseFloat(coords[3]);
	        	 float z = Float.parseFloat(coords[4]);
	        	 teleportTo(-x, -y, z, mapId);
	         } else if (p.getMessage().contains(".speed")) {
	        	 String[] coords = p.getMessage().split("\\s");
	        	 int speed = Integer.parseInt(coords[1]);
	        	 character.setCharSpeed((speed > 0) ? speed : 0);
	        	 this.sendMOTD("Modifying the multiplying speed requires a teleport to be applied.");
	         }else if (p.getMessage().contains(".fly")) {
	        	 connection.send(new SMSG_MOVE_SET_CANFLY(character));
	         }
         } catch (Exception e) {
        	 this.sendMOTD("Invalid command!");
         }
	}
	
	/**
	 * Instantly teleports the client to the given coords
	 */
	public void teleportTo(float x, float y, float z, int mapId) {
		Char character = this.connection.getClient().getCurrentCharacter();
		character.setPosition(x, y, z, mapId);
		connection.send(new SMSG_NEW_WORLD(character));
        connection.send(new SMSG_UPDATE_OBJECT_CREATE(this.connection.getClient(), true));

		connection.send(new SMSG_MOVE_SET_CANFLY(character));
	}

	/**
	 * 
	 * Untested and unimplemented packets go down here
	 * 
	 */
	
	/* temporary proof of concept */
	private String randomName() {
	    StringBuilder builder = new StringBuilder();
	    while (builder.toString().length() == 0) {
	        int length = rand.nextInt(5) + 5;
	        for(int i = 0; i < length; i++) {
	            builder.append(randomNameLexicon.charAt(rand.nextInt(randomNameLexicon.length())));
	        }
	    }
	    return builder.toString();
	}
	
	public void sendRandomName() {
		/* this is temp and should actually come from the client db */
		String generatedName = randomName();
		
		// char name must min size 3 and max size 12
		while ((generatedName.length() < 3) && (generatedName.length() > 12)) {
			generatedName = randomName();
		}
		
		ServerPacket randomCharName = new ServerPacket(Opcodes.SMSG_RANDOM_NAME_RESULT, 14);
		BitPack bitPack = new BitPack(randomCharName);
		
		bitPack.write(generatedName.length(), 6);
		bitPack.write(1);
		
		bitPack.flush();
		
		randomCharName.putString(generatedName);
		
		connection.send(randomCharName);
	}
}
