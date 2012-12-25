package bunnyEmu.main.net.ServerPackets;

import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.utils.Opcodes;

public class SMSG_ACCOUNT_DATA_TIMES extends ServerPacket{
	
	public SMSG_ACCOUNT_DATA_TIMES(int mask){
		super(Opcodes.SMSG_ACCOUNT_DATA_TIMES, 4 + 1 + 4 + 8 * 4);
		
		putInt((int) (System.currentTimeMillis() / 1000L));	// unix time of something
		put((byte) 1);
		putInt(mask);		// type mask
		for(int i = 0; i < 8; ++i)
			if ((mask & (1 << i)) > 0) {
				putInt(0);
			}
		
		wrap();
	}

}
