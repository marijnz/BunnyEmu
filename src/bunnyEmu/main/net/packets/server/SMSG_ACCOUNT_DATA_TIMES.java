package bunnyEmu.main.net.packets.server;

import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.utils.Opcodes;

/**
 * Current time on the server, required or client keeps frozen
 * 
 * @author Marijn
 *
 */
public class SMSG_ACCOUNT_DATA_TIMES extends ServerPacket{
	
	public SMSG_ACCOUNT_DATA_TIMES(int mask){
		super(Opcodes.SMSG_ACCOUNT_DATA_TIMES, 4 + 1 + 4 + 8 * 4);
		
		putInt((int) (System.currentTimeMillis() / 1000L));	// unix time
		put((byte) 1);
		putInt(mask);		// type mask
		for(int i = 0; i < 8; ++i)
			if ((mask & (1 << i)) > 0)
				putInt(0);
		wrap();
	}

}
