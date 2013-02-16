package bunnyEmu.main.net.ServerPackets;

import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.utils.Opcodes;

public class SMSG_FORCE_RUN_SPEED_CHANGE extends ServerPacket{
	
	public SMSG_FORCE_RUN_SPEED_CHANGE(Char character, float speed) {
		super(Opcodes.SMSG_FORCE_RUN_SPEED_CHANGE, 50);
		writePackedGuid(character.getGUID());
		putInt(0);
		put((byte) 0);
		putFloat(speed);
	}
		
}
