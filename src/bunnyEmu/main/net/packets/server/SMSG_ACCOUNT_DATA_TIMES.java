package bunnyEmu.main.net.packets.server;

import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.utils.BitPack;
import bunnyEmu.main.utils.Opcodes;

/**
 * Current time on the server, required or client keeps frozen
 * 
 * @author Marijn
 *
 */
public class SMSG_ACCOUNT_DATA_TIMES extends ServerPacket {
	
	public SMSG_ACCOUNT_DATA_TIMES(int mask) {
		super(Opcodes.SMSG_ACCOUNT_DATA_TIMES, 4 + 1 + 4 + 8 * 4);

		BitPack bitPack = new BitPack(this);
		
		putInt((int) (System.currentTimeMillis() / 1000L));	// unix time
		
		for (int i = 0; i < 8; i++) {
				putInt(0);
		}
		
		putInt(mask);		// type mask
		
		bitPack.write(0);
		bitPack.flush();
		
		wrap();
	}

}
