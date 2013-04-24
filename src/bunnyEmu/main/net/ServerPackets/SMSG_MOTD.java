package bunnyEmu.main.net.ServerPackets;

import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.utils.Opcodes;

/**
 * Message of the day message
 * 
 * @author Marijn
 *
 */
public class SMSG_MOTD extends ServerPacket{
	
	public SMSG_MOTD(String message){
		super(Opcodes.SMSG_MOTD, 4 +  message.length() + 1);
		putInt(4);
		putString(message);
	}

}
