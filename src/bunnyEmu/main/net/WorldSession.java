package bunnyEmu.main.net;

import java.nio.ByteOrder;

import bunnyEmu.main.Server;
import bunnyEmu.main.entities.Char;
import bunnyEmu.main.entities.ClientPacket;
import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.net.ServerPackets.SMSG_ACCOUNT_DATA_TIMES;
import bunnyEmu.main.net.ServerPackets.SMSG_LOGIN_VERIFY_WORLD;
import bunnyEmu.main.net.ServerPackets.SMSG_MOTD;
import bunnyEmu.main.net.ServerPackets.SMSG_NAME_QUERY_RESPONSE;
import bunnyEmu.main.net.ServerPackets.SMSG_PONG;
import bunnyEmu.main.utils.AuthCodes;
import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.Opcodes;


public class WorldSession {
	private WorldConnection connection;
	
	
	public WorldSession(WorldConnection c) {
		connection = c;
	}
	 
	 
	public void sendCharacters(){
   	 	Log.log("sending chars");

	   	int charCount = connection.getClientParent().getCharacters().size();
	   	
	   	ServerPacket charList = new ServerPacket(Opcodes.SMSG_CHAR_ENUM, 300 * charCount);
	   	
	    charList.put((byte) charCount);
	   	for (int atChar = 0; atChar < charCount; atChar++) {
	   		Char currentChar = connection.getClientParent().getCharacters().get(atChar);
	   	   charList.packet.order(ByteOrder.BIG_ENDIAN);
	   	   charList.putLong(currentChar.getGUID());		// PlayerGuid;
	   	   charList.packet.order(ByteOrder.LITTLE_ENDIAN);
	   	   charList.put(currentChar.getName().getBytes()); // name
	   	   charList.put((byte) 0);
	   	   charList.put((byte) currentChar.getCharRace()); // Race;
	   	   charList.put((byte) currentChar.getCharClass()); // Class;
	   	   charList.put((byte) 0); // Gender;
	   	   charList.put((byte) 1); // Skin;
	   	   charList.put((byte) 4); // Face;
	   	   charList.put((byte) 5); // Hair Style;
	   	   charList.put((byte) 0); // Hair Color;
	   	   charList.put((byte) 0); // Facial Hair;
	   	   charList.put((byte) 60); // Level;
	   	   charList.putInt(0); // Zone ID;
	   	   //charList.putInt(currentChar.getMapID()); // Map ID;
		   	   
		   	charList.put((byte) currentChar.getMapID());
		   	charList.put((byte) 0);
		   	charList.put((byte) 0);
		   charList.put((byte) 0);
	   	   charList.putFloat(currentChar.getX()); // X
	   	   charList.putFloat(currentChar.getY()); // Y
	   	   charList.putFloat(currentChar.getZ()); // Z
	   	   charList.putInt(0); // Guild ID;
	   	   charList.putInt(0); // Character Flags;
	   	   charList.putInt(0); // Login Flags;
	   	   charList.put((byte) 0); // Is Customize Pending?;
	   	   charList.putInt(0); // Pet DisplayID;
	   	   charList.putInt(0); // Pet Level;
	   	   charList.putInt(0); // Pet FamilyID;
	   	   
	   	   
	   	   //int EQUIPMENT_SLOT_END = 18;
	   	   byte[] data = {1,2,3,4,5,6,7,8,9,10,11,11,12,12,16,13,14,28,19, 0}; // added 0 to make it correct.. why?
	   	   for (byte ItemSlot = 0; ItemSlot < data.length; ++ItemSlot) {
	   	       charList.putInt(0); // Item DisplayID;
	   	       charList.put(data[ItemSlot]); // Item Inventory Type;
	   	       charList.putInt(1); // Item EnchantID;
	   	   }
	   	   for (int c=0; c < 3; c++){ // In 3.3.3 they added 3x new uint32 uint8 uint32  {
	   	      charList.putInt(0); // bag;
	   	      charList.put((byte) 18); // slot;
	   	      charList.putInt(1); // enchant?;
	   	   }
	   	}
	   	
	   	charList.wrap();
	   	connection.send(charList);
	   	
   }
	
	public void addCharacter(ClientPacket p){
		String name = p.getString();
		byte cRace = p.get();
		byte cClass = p.get();
		// More left (male/female , style data)
		Log.log("Created new char with name: " + name);
		connection.getClientParent().addCharacter(new Char(name, 0, 0, 0, 0, cRace, cClass));
		connection.send(new ServerPacket(Opcodes.SMSG_AUTH_RESPONSE, 1, AuthCodes.CHAR_CREATE_SUCCESS));
	}
	
	public void verifyLogin(ClientPacket p){
		Char character = connection.getClientParent().setCurrentCharacter(p.getLong());
		connection.send(new SMSG_LOGIN_VERIFY_WORLD(character));
		
		connection.send(Server.loadPacket("testpacket.txt", 2500));
	
		sendAccountDataTimes(0xEA);
		sendMOTD();
		sendSpellGo();
	}
	
	public void sendAccountDataTimes(int mask){
		connection.send(new SMSG_ACCOUNT_DATA_TIMES(mask));
		connection.send(new ServerPacket(Opcodes.SMSG_TIME_SYNC_REQ, 4));
	}
	
	
	public void sendMOTD(){
		connection.send(new SMSG_MOTD("Welcome to this test server"));
	}
	
	public void sendPong(){
		connection.send(new SMSG_PONG());
	}
	
	public void sendNameResponse(){
		connection.send(new SMSG_NAME_QUERY_RESPONSE(connection.clientParent.getCurrentCharacter()));
	}
	
	
	/**
	 * 
	 * Untested and unimplemented packets go down here
	 * 
	 */

	
	private void sendSpellGo(){
		byte[] data = (new byte[]{
				0x01,0x01,0x01,0x01,0x00,0x44,0x03,0x00,0x00,0x00,0x01,0x00,0x00,0x44,(byte) 0xC8,0x00
				,0x00,0x01,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x02,0x00,0x00,0x00,0x01
				,0x01
		});
		connection.send(new ServerPacket(Opcodes.SMSG_SPELL_GO, data.length, data));
	}
	
	private void setPosition(){
		Char currentChar = connection.getClientParent().getCurrentCharacter();

		ServerPacket updatePoint = new ServerPacket(Opcodes.SMSG_NEW_WORLD, 20);
		updatePoint.putInt(currentChar.getMapID());
		updatePoint.putFloat(currentChar.getX());
		updatePoint.putFloat(currentChar.getY());
		updatePoint.putFloat(currentChar.getZ());
		updatePoint.putInt(0);// orientation
		connection.send(updatePoint);
	}
	
	private void setSpeed(float speed){
		ServerPacket setRunSpeed = new ServerPacket(Opcodes.SMSG_FORCE_RUN_SPEED_CHANGE, 50);
		setRunSpeed.writePackedGuid(connection.getClientParent().getCurrentCharacter().getGUID());
		setRunSpeed.putInt(0);
		setRunSpeed.put((byte) 0);
		setRunSpeed.putFloat(speed);
		
		connection.send(setRunSpeed);
	}
	
	private void sendMessage(String channel, String message){
		ServerPacket msgPacket = new ServerPacket(Opcodes.SMSG_MESSAGECHAT, 1+4+8+4+ channel.length()+1 + 8 + 4 + message.length()+1 + 1);
		msgPacket.put((byte) 17);
		msgPacket.putInt( 0);		// language
		msgPacket.writePackedGuid(connection.clientParent.getCurrentCharacter().getGUID());	// guid
		msgPacket.putInt( 0);		// rank?
		msgPacket.putString(channel);			// channel name
		msgPacket.writePackedGuid(connection.clientParent.getCurrentCharacter().getGUID());	// guid again?
		msgPacket.putInt(message.length()+1);
		msgPacket.putString(message);
		msgPacket.put((byte) 4); // gm = 4, normal = 0
		connection.send(msgPacket);
	}
	
	
	
	
}
