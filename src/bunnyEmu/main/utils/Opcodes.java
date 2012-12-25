package bunnyEmu.main.utils;

public class Opcodes {
	public static final int CMSG_CHAR_CREATE				= 0x036;
	public static final int CMSG_CHAR_ENUM 					= 0x0037;
	public static final int CMSG_PING          				= 0x1DC;
	public static final int CMSG_REALM_SPLIT				= 0x38C;
	public static final int CMSG_AUTH_PROOF					= 0x01ED;
	public static final int CMSG_PLAYER_LOGIN				= 0x003D;
	public static final int CMSG_NAME_QUERY					= 0x0050;
	
	public static final int SMSG_CHAR_CREATE				= 0x03A;
	public static final int SMSG_CHAR_ENUM 					= 0x003B;
	public static final int SMSG_AUTH_CHALLENGE 			= 0x01EC;
	public static final int SMSG_AUTH_RESPONSE 				= 0x01EE;
	public static final int SMSG_LOGIN_VERIFY_WORLD 		= 0x0236;
	public static final int SMSG_FEATURE_SYSTEM_STATUS 		= 0x03C9;
	public static final int SMSG_LEARNED_DANCE_MOVES 		= 0x0455;
	public static final int SMSG_ACCOUNT_DATA_TIMES 		= 0x0209;
	public static final int SMSG_TRIGGER_CINEMATIC 			= 0x00FA;
	public static final int SMSG_CONTACT_LIST 				= 0x0067;
	public static final int SMSG_EQUIPMENT_SET_LIST 		= 0x04BC;
	public static final int SMSG_MESSAGECHAT 				= 0x0096;
	public static final int SMSG_TRANSFER_PENDING 			= 0x003F;
	public static final int SMSG_TRANSFER_ABORTED 			= 0x0040;
	public static final int SMSG_PONG 						= 0x01DD;
	public static final int SMSG_MOTD						= 0x033D;
	public static final int SMSG_BINDPOINTUPDATE			= 0x0155;
	public static final int SMSG_NAME_QUERY_RESPONSE		= 0x0051;
	public static final int SMSG_COMPRESSED_UPDATE_OBJECT	= 0x1F6;
	public static final int SMSG_NEW_WORLD					= 0x03E;
	public static final int	SMSG_SPELL_GO					= 0x132;
	public static final int SMSG_TIME_SYNC_REQ				= 0x0390;
	public static final int SMSG_FORCE_RUN_SPEED_CHANGE		= 0x0E2;
	public static final int SMSG_FORCE_MOVE_ROOT			= 0x0E8;
	

	public static final int MSG_SET_DUNGEON_DIFFICULTY 		= 0x0329;
	public static final int MSG_SET_RAID_DIFFICULTY 		= 0x04EB;
	public static final int MSG_MOVE_SET_RUN_SPEED			= 0xE24E;
	
	
}
