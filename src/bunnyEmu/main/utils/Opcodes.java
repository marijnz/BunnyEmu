package bunnyEmu.main.utils;


/**
 * The list of all opcodes used. Using a form function to get a packetmap
 * 
 * @author Marijn
 *
 */

public class Opcodes {
	public static final String CMSG_CHAR_CREATE					= "CMSG_CHAR_CREATE";;
	public static final String CMSG_CHAR_ENUM 					= "CMSG_CHAR_ENUM";
	public static final String CMSG_PING          				= "CMSG_PING";
	public static final String CMSG_REALM_SPLIT					= "CMSG_REALM_SPLIT";
	public static final String CMSG_AUTH_PROOF					= "CMSG_AUTH_PROOF";
	public static final String CMSG_PLAYER_LOGIN				= "CMSG_PLAYER_LOGIN";
	public static final String CMSG_NAME_QUERY					= "CMSG_NAME_QUERY";
	
	public static final String CMSG_READY_FOR_ACCOUNT_DATA_TIMES= "CMSG_READY_FOR_ACCOUNT_DATA_TIMES";  // 4.0.6a 13623
	public static final String CMSG_WORLD_LOGIN					= "CMSG_WORLD_LOGIN"; 					// 4.0.6a 13623
	public static final String CMSG_UPDATE_ACCOUNT_DATA			= "CMSG_UPDATE_ACCOUNT_DATA";			// 4.0.6a 13623
	
	public static final String SMSG_CHAR_CREATE					= "SMSG_CHAR_CREATE";
	public static final String SMSG_CHAR_ENUM 					= "SMSG_CHAR_ENUM";
	public static final String SMSG_AUTH_CHALLENGE 				= "SMSG_AUTH_CHALLENGE";
	public static final String SMSG_AUTH_RESPONSE 				= "SMSG_AUTH_RESPONSE";
	public static final String SMSG_LOGIN_VERIFY_WORLD 			= "SMSG_LOGIN_VERIFY_WORLD";
	public static final String SMSG_FEATURE_SYSTEM_STATUS 		= "SMSG_FEATURE_SYSTEM_STATUS";
	public static final String SMSG_LEARNED_DANCE_MOVES 		= "SMSG_LEARNED_DANCE_MOVES";
	public static final String SMSG_ACCOUNT_DATA_TIMES 			= "SMSG_ACCOUNT_DATA_TIMES";
	public static final String SMSG_TRIGGER_CINEMATIC 			= "SMSG_TRIGGER_CINEMATIC";
	public static final String SMSG_CONTACT_LIST 				= "SMSG_CONTACT_LIST";
	public static final String SMSG_EQUIPMENT_SET_LIST 			= "SMSG_EQUIPMENT_SET_LIST";
	public static final String SMSG_MESSAGECHAT 				= "SMSG_MESSAGECHAT";
	public static final String SMSG_TRANSFER_PENDING 			= "SMSG_TRANSFER_PENDING";
	public static final String SMSG_TRANSFER_ABORTED 			= "SMSG_TRANSFER_ABORTED";
	public static final String SMSG_PONG 						= "SMSG_PONG";
	public static final String SMSG_MOTD						= "SMSG_MOTD";
	public static final String SMSG_NAME_QUERY_RESPONSE			= "SMSG_NAME_QUERY_RESPONSE";
	public static final String SMSG_UPDATE_OBJECT				= "SMSG_UPDATE_OBJECT";
	public static final String SMSG_COMPRESSED_UPDATE_OBJECT	= "SMSG_COMPRESSED_UPDATE_OBJECT";
	public static final String SMSG_NEW_WORLD					= "SMSG_NEW_WORLD";
	public static final String SMSG_SPELL_GO					= "SMSG_SPELL_GO";
	public static final String SMSG_TIME_SYNC_REQ				= "SMSG_TIME_SYNC_REQ";
	public static final String SMSG_FORCE_RUN_SPEED_CHANGE		= "SMSG_FORCE_RUN_SPEED_CHANGE";
	public static final String SMSG_FORCE_MOVE_ROOT				= "SMSG_FORCE_MOVE_ROOT";
	public static final String SMSG_MOVE_SET_CANFLY				= "SMSG_MOVE_SET_CANFLY"; 			// MoP
	public static final String SMSG_KNOWN_SPELLS				= "SMSG_KNOWN_SPELLS";			// MoP
	
	public static final String MSG_SET_DUNGEON_DIFFICULTY 	= "MSG_SET_DUNGEON_DIFFICULTY";
	public static final String MSG_SET_RAID_DIFFICULTY 			= "MSG_SET_RAID_DIFFICULTY";
	public static final String MSG_MOVE_SET_RUN_SPEED			= "MSG_MOVE_SET_RUN_SPEED";
	public static final String MSG_TRANSFER_INITIATE			= "MSG_TRANSFER_INITIATE";			// MoP

	
	private static PacketMap packets;
	
	public static PacketMap formWotLK(){

		packets = new PacketMap();
		add(CMSG_CHAR_CREATE 				,0x036);
		add(CMSG_CHAR_ENUM 					,0x0037);
		add(CMSG_PING          				,0x1DC);
		add(CMSG_REALM_SPLIT				,0x38C);
		add(CMSG_AUTH_PROOF					,0x01ED);
		add(CMSG_PLAYER_LOGIN				,0x003D);
		add(CMSG_NAME_QUERY					,0x0050);
		
		add(SMSG_CHAR_CREATE				,0x03A);
		add(SMSG_CHAR_ENUM 					,0x003B);
		add(SMSG_AUTH_CHALLENGE 			,0x01EC);
		add(SMSG_AUTH_RESPONSE 				,0x01EE);
		add(SMSG_LOGIN_VERIFY_WORLD 		,0x0236);
		add(SMSG_FEATURE_SYSTEM_STATUS 		,0x03C9);
		add(SMSG_LEARNED_DANCE_MOVES 		,0x0455);
		add(SMSG_ACCOUNT_DATA_TIMES 		,0x0209);
		add(SMSG_TRIGGER_CINEMATIC 			,0x00FA);
		add(SMSG_CONTACT_LIST 				,0x0067);
		add(SMSG_EQUIPMENT_SET_LIST 		,0x04BC);
		add(SMSG_MESSAGECHAT 				,0x0096);
		add(SMSG_TRANSFER_PENDING 			,0x003F);
		add(SMSG_TRANSFER_ABORTED 			,0x0040);
		add(SMSG_PONG 						,0x01DD);
		add(SMSG_MOTD						,0x033D);
		add(SMSG_NAME_QUERY_RESPONSE		,0x0051);
		add(SMSG_UPDATE_OBJECT        		,0x00A9); 
		add(SMSG_COMPRESSED_UPDATE_OBJECT	,0x1F6);
		add(SMSG_NEW_WORLD					,0x03E);
		add(SMSG_SPELL_GO					,0x132);
		add(SMSG_TIME_SYNC_REQ				,0x0390);
		add(SMSG_FORCE_RUN_SPEED_CHANGE		,0x0E2);
		add(SMSG_FORCE_MOVE_ROOT			,0x0E8);
		
		add(MSG_SET_DUNGEON_DIFFICULTY 		,0x0329);
		add(MSG_SET_RAID_DIFFICULTY 		,0x04EB);
		add(MSG_MOVE_SET_RUN_SPEED			,0xE24E);
		add(MSG_TRANSFER_INITIATE			,0x4F57); // MoP
		
		return packets.clone();
	}
	
	public static PacketMap formCata(){
		packets = new PacketMap();
		
		add(CMSG_CHAR_CREATE               		,0x07EEC); // 4.0.6a 13623
		add(CMSG_CHAR_ENUM               		,0x06AA4); // 4.0.6a 13623
		add(CMSG_AUTH_PROOF               		,0x00E0E); // 4.0.6a 13623
		add(CMSG_CHAR_ENUM						,0x06AA4);  // 4.0.6a 13623
		add(CMSG_READY_FOR_ACCOUNT_DATA_TIMES	,0x07DA8); // 4.0.6a 13623 //unused
		add(CMSG_REALM_SPLIT					,0x060AC); // 4.0.6a 13623 //unused
		add(CMSG_PING       					,0x0064E); //4.0.6a 13623 
		add(CMSG_WORLD_LOGIN       				,0x08508); //4.0.6a 13623 //unused
		add(CMSG_PLAYER_LOGIN       			,0x08180); //4.0.6a 13623
		add(CMSG_NAME_QUERY       				,0x07AAC); //4.0.6a 13623
		add(CMSG_UPDATE_ACCOUNT_DATA       		,0x072A4); //4.0.6a 13623 // unused
		
		
		add(SMSG_AUTH_CHALLENGE 				,0x06019);  // 4.0.6a 13623
		add(SMSG_AUTH_RESPONSE 					,0x0B28C); // 4.0.6a 13623
		add(SMSG_CHAR_ENUM						,0x0ECCC); //4.0.6a 13623
		add(SMSG_CHAR_CREATE					,0x0F7EC); //4.0.6a 13623
		add(SMSG_PONG       					,0x0A01B); //4.0.6a 13623
		add(SMSG_ACCOUNT_DATA_TIMES        		,0x07280); //4.0.6a 13623
		add(SMSG_LOGIN_VERIFY_WORLD        		,0x028C0); //4.0.6a 13623
		add(SMSG_TIME_SYNC_REQ        			,0x0AA80); //4.0.6a 13623
		add(SMSG_MOTD        					,0x077C0); //4.0.6a 13623
		add(SMSG_NAME_QUERY_RESPONSE        	,0x07BC8); //4.0.6a 13623
		add(SMSG_SPELL_GO        				,0x030C0); //4.0.6a 13623
		add(SMSG_UPDATE_OBJECT        			,0x03780); //4.0.6a 13623
		add(SMSG_COMPRESSED_UPDATE_OBJECT		,0xEAC0); //4.0.6a 13623
		
		return packets.clone();
	}
	
	public static PacketMap formMoP(){
		packets = new PacketMap();
		
		add(CMSG_CHAR_CREATE               		,0xEB3); // MoP
		add(CMSG_CHAR_ENUM               		,0x0146); // MoP
		add(CMSG_AUTH_PROOF               		,0x0C07); // MoP
		add(CMSG_CHAR_ENUM						,0x06AA4);  // 4.0.6a 13623
		add(CMSG_READY_FOR_ACCOUNT_DATA_TIMES	,0x5A5); 	// MoP
		add(CMSG_REALM_SPLIT					,0x261); // MoP
		add(CMSG_PING       					,0x0064E); //4.0.6a 13623 
		add(CMSG_WORLD_LOGIN       				,0x08508); //4.0.6a 13623 //unused
		add(CMSG_PLAYER_LOGIN       			,0xEBA); // MoP
		add(CMSG_NAME_QUERY       				,0x07AAC); //4.0.6a 13623
		add(CMSG_UPDATE_ACCOUNT_DATA       		,0x072A4); //4.0.6a 13623 // unused
		
		
		add(SMSG_AUTH_CHALLENGE 				,0x0CAF);  // MoP
		add(SMSG_AUTH_RESPONSE 					,0x0A15); // MoP
		add(SMSG_CHAR_ENUM						,0x033D); // MoP
		add(SMSG_CHAR_CREATE					,0x0F25); // MoP
		add(SMSG_PONG       					,0x8AE); // MoP
		add(SMSG_ACCOUNT_DATA_TIMES        		,0x0E48); // MoP, called AccountDataInitialized
		add(SMSG_LOGIN_VERIFY_WORLD        		,0x028C0); //4.0.6a 13623
		add(SMSG_TIME_SYNC_REQ        			,0x0410); // MoP
		add(SMSG_MOTD        					,0x0849); // MoP
		add(SMSG_NAME_QUERY_RESPONSE        	,0x07BC8); //4.0.6a 13623
		add(SMSG_SPELL_GO        				,0x030C0); //4.0.6a 13623
		add(SMSG_UPDATE_OBJECT        			,0x120); // MoP
		add(SMSG_COMPRESSED_UPDATE_OBJECT		,0x0EAC0); //4.0.6a 13623
		add(SMSG_MOVE_SET_CANFLY				,0x419); //MoP
		add(SMSG_KNOWN_SPELLS					,0x155); //MoP
		
		
		add(MSG_TRANSFER_INITIATE				,0x04F57); // MoP
		
		return packets.clone();
	}
	
	private static void add(String name, Integer opcode){
		packets.add(name, opcode.shortValue());
	}
	
	
	
}
