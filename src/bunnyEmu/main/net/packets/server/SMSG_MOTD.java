package bunnyEmu.main.net.packets.server;

import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.utils.BitPack;
import bunnyEmu.main.utils.Opcodes;

/**
 * Message of the day message
 * 
 * @author Marijn
 *
 */
public class SMSG_MOTD extends ServerPacket {
	
	public SMSG_MOTD(String message) {
		super(Opcodes.SMSG_MOTD, 4 +  message.length() + 1);

		BitPack bitPack = new BitPack(this);

		bitPack.write(3, 4);
		bitPack.write(message.length(), 7);

		putString(message);

		wrap();
	}

}
