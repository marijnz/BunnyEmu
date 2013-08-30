package bunnyEmu.main.utils;


/**
 * The list of all opcodes used. Using a form function to get a packetmap
 * 
 * @author Marijn
 *
 */

public class Opcodes {
	public static final String CMSG_CHAR_CREATE					= "CMSG_CHAR_CREATE";
	public static final String CMSG_CHAR_DELETE 				= "CMSG_CHAR_DELETE";
	public static final String CMSG_CHAR_ENUM 					= "CMSG_CHAR_ENUM";
	public static final String CMSG_MESSAGECHAT					= "CMSG_MESSAGECHAT";
	public static final String CMSG_RANDOM_NAME					= "CMSG_RANDOM_NAME";
	public static final String CMSG_PLAYER_LOGIN				= "CMSG_PLAYER_LOGIN";
	public static final String CMSG_AUTH_PROOF					= "CMSG_AUTH_PROOF";
	public static final String CMSG_VIOLENCE_LEVEL 				= "CMSG_VIOLENCE_LEVEL";
	public static final String CMSG_HEARTBEAT	 				= "CMSG_HEARTBEAT";
	
	public static final String CMSG_PING          				= "CMSG_PING";
	public static final String CMSG_REALM_SPLIT					= "CMSG_REALM_SPLIT";
	public static final String CMSG_NAME_QUERY					= "CMSG_NAME_QUERY";
	public static final String CMSG_READY_FOR_ACCOUNT_DATA_TIMES= "CMSG_READY_FOR_ACCOUNT_DATA_TIMES";  
	public static final String CMSG_WORLD_LOGIN					= "CMSG_WORLD_LOGIN"; 					
	public static final String CMSG_UPDATE_ACCOUNT_DATA			= "CMSG_UPDATE_ACCOUNT_DATA";			
	public static final String CMSG_NAME_CACHE					= "CMSG_NAME_CACHE";					// MoP
	public static final String CMSG_REALM_CACHE					= "CMSG_REALM_CACHE";					// MoP
	public static final String CMSG_DISCONNECT					= "CMSG_DISCONNECT";
	public static final String CMSG_LOADING_SCREEN 				= "CMSG_LOADING_SCREEN";
	public static final String CMSG_BULK_DB_QUERY 				= "CMSG_BULK_DB_QUERY";
	public static final String CMSG_MOVEMENT					= "CMSG_MOVEMENT";						// Covers ALL movement packets

	
	
	public static final String SMSG_CHAR_CREATE					= "SMSG_CHAR_CREATE";
	public static final String SMSG_CHAR_DELETE 				= "SMSG_CHAR_DELETE";
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
	public static final String SMSG_MOVE_SET_CANFLY				= "SMSG_MOVE_SET_CANFLY"; 			
	public static final String SMSG_KNOWN_SPELLS				= "SMSG_KNOWN_SPELLS";			
	public static final String SMSG_NAME_CACHE					= "SMSG_NAME_CACHE";					// MoP
	public static final String SMSG_REALM_CACHE					= "SMSG_REALM_CACHE";					// MoP
	public static final String SMSG_UPDATE_CLIENT_CACHE_VERSION = "SMSG_UPDATE_CLIENT_CACHE_VERSION";	// MoP
	public static final String SMSG_TUTORIAL_FLAGS				= "SMSG_TUTORIAL_FLAGS";				// MoP
	public static final String SMSG_RANDOM_NAME_RESULT 			= "SMSG_RANDOM_NAME_RESULT";			
	public static final String SMSG_REALM_SPLIT					= "SMSG_REALM_SPLIT";
	
	public static final String MSG_SET_DUNGEON_DIFFICULTY 		= "MSG_SET_DUNGEON_DIFFICULTY";
	public static final String MSG_SET_RAID_DIFFICULTY 			= "MSG_SET_RAID_DIFFICULTY";
	public static final String MSG_MOVE_SET_RUN_SPEED			= "MSG_MOVE_SET_RUN_SPEED";
	public static final String MSG_TRANSFER_INITIATE			= "MSG_TRANSFER_INITIATE";
	
	private static PacketMap packets;
	
	public static PacketMap formWotLK() {

		packets = new PacketMap();
		add(CMSG_CHAR_CREATE 				,0x036);
		add(CMSG_CHAR_ENUM 					,0x0037);
		add(CMSG_PING          				,0x1DC);
		add(CMSG_REALM_SPLIT				,0x38C);
		add(CMSG_AUTH_PROOF					,0x01ED);
		add(CMSG_PLAYER_LOGIN				,0x003D);
		add(CMSG_NAME_QUERY					,0x0050);
		
		add(SMSG_AUTH_CHALLENGE 			,0x01EC);
		add(SMSG_AUTH_RESPONSE 				,0x01EE);
		add(SMSG_CHAR_CREATE				,0x03A);
		add(SMSG_CHAR_ENUM 					,0x003B);
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
		add(MSG_TRANSFER_INITIATE			,0x4F57); 
		
		return packets.clone();
	}
	
	public static PacketMap formCata(){
		packets = new PacketMap();
		
		add(CMSG_CHAR_CREATE               		,0x07EEC); 
		add(CMSG_CHAR_ENUM               		,0x06AA4); 
		add(CMSG_AUTH_PROOF               		,0x00E0E); 
		add(CMSG_CHAR_ENUM						,0x06AA4);  
		add(CMSG_READY_FOR_ACCOUNT_DATA_TIMES	,0x07DA8);  //unused
		add(CMSG_REALM_SPLIT					,0x060AC);  //unused
		add(CMSG_PING       					,0x0064E);  
		add(CMSG_WORLD_LOGIN       				,0x08508);  //unused
		add(CMSG_PLAYER_LOGIN       			,0x08180); 
		add(CMSG_NAME_QUERY       				,0x07AAC); 
		add(CMSG_UPDATE_ACCOUNT_DATA       		,0x072A4);  // unused
		
		add(SMSG_AUTH_CHALLENGE 				,0x06019);  
		add(SMSG_AUTH_RESPONSE 					,0x0B28C); 
		add(SMSG_CHAR_ENUM						,0x0ECCC); 
		add(SMSG_CHAR_CREATE					,0x0F7EC); 
		add(SMSG_PONG       					,0x0A01B); 
		add(SMSG_ACCOUNT_DATA_TIMES        		,0x07280); 
		add(SMSG_LOGIN_VERIFY_WORLD        		,0x028C0); 
		add(SMSG_TIME_SYNC_REQ        			,0x0AA80); 
		add(SMSG_MOTD        					,0x077C0); 
		add(SMSG_NAME_QUERY_RESPONSE        	,0x07BC8); 
		add(SMSG_SPELL_GO        				,0x030C0); 
		add(SMSG_UPDATE_OBJECT        			,0x03780); 
		add(SMSG_COMPRESSED_UPDATE_OBJECT		,0xEAC0); 
		
		return packets.clone();
	}
	
	/* current patch = 5.3.0a */
	//https://github.com/Arctium/Arctium/commit/7efcfb57bd3bbb2010c2290720bd6441c5e12dbf
	public static PacketMap formMoP(){
		packets = new PacketMap();
		
		/* client side opcodes start */

		add(CMSG_CHAR_ENUM                		,0x0B1D);
		add(CMSG_CHAR_CREATE               		,0x0404); 
		add(CMSG_CHAR_DELETE					,0x010C);

		add(CMSG_PLAYER_LOGIN       			,0x0A19);
		add(CMSG_RANDOM_NAME					,0x091D);
		add(CMSG_LOADING_SCREEN					,0x0341);
		add(CMSG_VIOLENCE_LEVEL					,0x054B);
		add(CMSG_HEARTBEAT						,0x0E0B);
		
		add(CMSG_AUTH_PROOF               		,0x09F1); 
		add(CMSG_READY_FOR_ACCOUNT_DATA_TIMES	,0x0755); 	
		add(CMSG_REALM_SPLIT					,0x0B48);  
		add(CMSG_NAME_CACHE       				,0x0018);	// aka QueryPlayerName
		add(CMSG_MESSAGECHAT       				,0x016A);	// aka ChatMessageSay
		add(CMSG_REALM_CACHE       				,0x0209);	// aka QueryRealmName
		add(CMSG_PING       					,0x08E3); 
		add(CMSG_DISCONNECT                     ,0x09A2);	// player canceled login
		add(CMSG_BULK_DB_QUERY					,0x0149);
		
		/* client side opcodes end */
		
		
		/* server side opcodes start */
		
		add(SMSG_AUTH_CHALLENGE 				,0x0221);  
		add(SMSG_AUTH_RESPONSE 					,0x0890); 
		add(SMSG_CHAR_ENUM						,0x0FDD);
		add(SMSG_CHAR_CREATE					,0x1495);
		add(SMSG_CHAR_DELETE					,0x14C1);
		add(SMSG_RANDOM_NAME_RESULT				,0x0AD9);
		
		add(SMSG_PONG       					,0x1121);
		add(SMSG_ACCOUNT_DATA_TIMES        		,0x0CD1);
		add(SMSG_TIME_SYNC_REQ        			,0x0AD4); 
		add(SMSG_MOTD        					,0x12DC); 
		add(SMSG_SPELL_GO        				,0x030C0);	// cata
		add(SMSG_UPDATE_OBJECT        			,0x0C65);
		add(SMSG_MOVE_SET_CANFLY				,0x0F48);
		add(SMSG_KNOWN_SPELLS					,0x173F);
		add(SMSG_NAME_CACHE       				,0x0BD0);	// aka QueryPlayerNameResponse
		add(SMSG_REALM_CACHE       				,0x10CC);	// aka RealmQueryResponse
		add(SMSG_MESSAGECHAT					,0x17EF);
		add(SMSG_UPDATE_CLIENT_CACHE_VERSION	,0x1489);
		add(SMSG_TUTORIAL_FLAGS					,0x0D7E);
		add(SMSG_NEW_WORLD						,0x04D9);
		add(SMSG_REALM_SPLIT					,0x0F89);
		addMultiple(CMSG_MOVEMENT				,0x0A4B
												,0x0A4B
												,0x08D2
												,0x0813
												,0x0816
												,0x0843
												,0x0A4A
												,0x0CCA
												,0x0A9E
												,0x0A07
												,0x080A
												,0x0C02
												,0x0A12
												,0x0AC7
												,0x081F
												,0x0856
												,0x0CCF
												,0x0C13
												,0x0C93
												,0x0A5F
												,0x0C43
												,0x0886
												,0x0E0B
												,0x0AC3
												,0x0ADF
												,0x089F
												,0x0A47
												,0x0C92
												,0x0893
												,0x0A56);

		/* server side opcodes end */

		add(MSG_TRANSFER_INITIATE				,0x4F57);
		
		return packets.clone();
	}
	
	private static void add(String name, Integer opcode) {
		packets.add(name, opcode.shortValue());
	}
	
	private static void addMultiple(String name, Integer... opcodes) {
		for(Integer opcode : opcodes)
			packets.addValue(opcode.shortValue(), name);
	}
}
