package bunnyEmu.main.net;

import java.io.UnsupportedEncodingException;

import bunnyEmu.main.entities.Char;
import bunnyEmu.main.entities.ClientPacket;
import bunnyEmu.main.entities.Realm;
import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.net.ServerPackets.SMSG_ACCOUNT_DATA_TIMES;
import bunnyEmu.main.net.ServerPackets.SMSG_CHAR_ENUM;
import bunnyEmu.main.net.ServerPackets.SMSG_CHAR_ENUM_MOP;
import bunnyEmu.main.net.ServerPackets.SMSG_FORCE_RUN_SPEED_CHANGE;
import bunnyEmu.main.net.ServerPackets.SMSG_LOGIN_VERIFY_WORLD;
import bunnyEmu.main.net.ServerPackets.SMSG_MOTD;
import bunnyEmu.main.net.ServerPackets.SMSG_NAME_QUERY_RESPONSE;
import bunnyEmu.main.net.ServerPackets.SMSG_PONG;
import bunnyEmu.main.utils.AuthCodes;
import bunnyEmu.main.utils.Constants;
import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.Opcodes;


public class WorldSession {
	private WorldConnection connection;
	private Realm realm;
	
	
	public WorldSession(WorldConnection c, Realm realm) {
		connection = c;
		this.realm = realm;
	}
	 
	 
	public void sendCharacters(){
   	 	Log.log("sending chars");
   	 	if(this.realm.getVersion() <= Constants.VERSION_CATA)
   	 		connection.send(new SMSG_CHAR_ENUM(connection.getClientParent()));
		else
			try {
				connection.send(new SMSG_CHAR_ENUM_MOP(connection.getClientParent()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//connection.send(realm.loadPacket("charenum_mop", 535));
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
		Char character;
		if(realm.getVersion() <= Constants.VERSION_CATA){
			character = connection.getClientParent().setCurrentCharacter(p.getLong());
			connection.send(new SMSG_LOGIN_VERIFY_WORLD(character));
			if(realm.getVersion() == Constants.VERSION_BC)
				connection.send(realm.loadPacket("updatepacket_bc", 5000));
			else if(realm.getVersion() == Constants.VERSION_WOTLK)
				connection.send(realm.loadPacket("updatepacket_wotlk", 2500));
			else if(realm.getVersion() == Constants.VERSION_CATA)
				connection.send(realm.loadPacket("updatepacket_cata", 500));
		} else{
			character = connection.getClientParent().setCurrentCharacter(1);
			connection.send(realm.loadPacket("updatepacket_mop", 7000));
		}
			
		sendAccountDataTimes(0xEA);
		sendMOTD();
		sendSpellGo();
		//connection.send(realm.loadPacket("forcerun", 20));
		
		connection.send(new SMSG_FORCE_RUN_SPEED_CHANGE(character, 30));
	}
	
	public void sendAccountDataTimes(int mask){
		connection.send(new SMSG_ACCOUNT_DATA_TIMES(mask));
		if(this.realm.getVersion() <= Constants.VERSION_CATA)
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
