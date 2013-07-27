package bunnyEmu.main.net.packets.server;

import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.utils.Opcodes;

/**
 * Modifies the client's run speed.
 * 
 * @author Marijn
 *
 */
public class SMSG_FORCE_RUN_SPEED_CHANGE extends ServerPacket{
	
	public SMSG_FORCE_RUN_SPEED_CHANGE(Char character, float speed) {
		super(Opcodes.SMSG_FORCE_RUN_SPEED_CHANGE, 50);
		writePackedGuid(character.getGUID());
		putInt(0);
		put((byte) 0);
		putFloat(speed);
	}
		
}
