package bunnyEmu.main.net;

import java.nio.ByteOrder;

import bunnyEmu.main.entities.Realm;
import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.entities.packet.ClientPacket;
import bunnyEmu.main.entities.packet.ServerPacket;
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

	public WorldSession(WorldConnection c, Realm realm) {
		connection = c;
		this.realm = realm;
	}

	/**
	 * Send the character list to the client.
	 */
	public void sendCharacters() {
		Log.log("sending chars");
		connection.send(new SMSG_CHAR_ENUM(connection.getClient()));
	}

	/**
	 * Creates a character for the client.
	 * 
	 * @param p
	 */
	public void addCharacter(ClientPacket p) {
		String name = p.getString();
		byte cRace = p.get();
		byte cClass = p.get();
		// More left (male/female , style data)
		Log.log("Created new char with name: " + name);
		connection.getClient().addCharacter(new Char(name, 0, 0, 0, 0, cRace, cClass));
		connection.send(new ServerPacket(Opcodes.SMSG_AUTH_RESPONSE, 1, AuthCodes.CHAR_CREATE_SUCCESS));
	}

	/**
	 * Sends initial packets after world login has been confirmed.
	 */
	public void verifyLogin(CMSG_PLAYER_LOGIN p) {
		Char character = connection.getClient().setCurrentCharacter(p.getGuid());
		connection.send(new SMSG_LOGIN_VERIFY_WORLD(character));
		connection.send(new SMSG_KNOWN_SPELLS(character));
		character.setSpeed(15);
		
		// Set the update fields, required for update packets
		character.setUpdateFields(realm);
		
		// Currently only fully supports MoP
		if (realm.getVersion() <= Versions.VERSION_BC)
			connection.send(realm.loadPacket("updatepacket_bc", 5000));
		else if (realm.getVersion() <= Versions.VERSION_WOTLK)
			//connection.send(realm.loadPacket("updatepacket_wotlk", 2500));
			connection.send(new SMSG_UPDATE_OBJECT_CREATE(this.connection.getClient()));
		else if (realm.getVersion() <= Versions.VERSION_CATA)
			connection.send(realm.loadPacket("updatepacket_cata", 500));
		else
			connection.send(new SMSG_UPDATE_OBJECT_CREATE(this.connection.getClient()));

		connection.send(new SMSG_MOVE_SET_CANFLY(character));
		sendAccountDataTimes(0xEA);
		sendMOTD("Welcome to BunnyEmu, have fun exploring!");
		sendSpellGo(); // Shiny start
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
		long guid = p.getLong();
		Log.log("GUID: " + guid);
		
		connection.send(new SMSG_NAME_CACHE(connection.client.getCurrentCharacter(), realm));
	}
	
	/**
	 * Realm data? Required for MoP
	 */
	public void handleRealmCache(ClientPacket p){
		int realmId = p.getInt();
		if(realm.id != realmId)
			return;
		
		connection.send(new SMSG_REALM_CACHE(realm));
	}
	
	/**
	 * Handles a chat message given by the client, checks for commands.
	 */
	public void handleChatMessage(ClientPacket p){
		Char character = connection.client.getCurrentCharacter();
		 BitUnpack bitUnpack = new BitUnpack(p);
         int language = p.getInt();

         int messageLength = bitUnpack.GetBits((byte) 9);
         String message = p.getString(messageLength);
         connection.send(new SMSG_MESSAGECHAT(connection.client.getCurrentCharacter(), language, message));
         
         try{
	         if(message.contains(".tele")){
	        	 String[] coords = message.split("\\s");
	        	 int mapId = Integer.parseInt(coords[1]);
	        	 float x = Float.parseFloat(coords[2]);
	        	 float y = Float.parseFloat(coords[3]);
	        	 float z = Float.parseFloat(coords[4]);
	        	 teleportTo(-x, -y, z, mapId);
	         }
	         if(message.contains(".speed")){
	        	 String[] coords = message.split("\\s");
	        	 int speed = Integer.parseInt(coords[1]);
	        	 character.setSpeed((speed > 0) ? speed : 0);
	        	 this.sendMOTD("Modifying the multiplying speed requires a teleport to be applied.");
	         }
         } catch (Exception e){
        	 this.sendMOTD("Invalid command!");
         }
	}
	
	/**
	 * Instantly teleports the client to the given coords
	 */
	public void teleportTo(float x, float y, float z, int mapId){
		Char character = this.connection.getClient().getCurrentCharacter();
		character.setPosition(x, y, z, mapId);
		connection.send(new SMSG_NEW_WORLD(character));
        connection.send(new SMSG_UPDATE_OBJECT_CREATE(this.connection.getClient()));

		connection.send(new SMSG_MOVE_SET_CANFLY(character));
	}

	/**
	 * 
	 * Untested and unimplemented packets go down here
	 * 
	 */

	private void sendSpellGo() {
		byte[] data = (new byte[] { 0x01, 0x01, 0x01, 0x01, 0x00, 0x44, 0x03,
				0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x44, (byte) 0xC8, 0x00,
				0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x02, 0x00, 0x00, 0x00, 0x01, 0x01 });
		connection.send(new ServerPacket(Opcodes.SMSG_SPELL_GO, data.length,
				data));
	}
}
