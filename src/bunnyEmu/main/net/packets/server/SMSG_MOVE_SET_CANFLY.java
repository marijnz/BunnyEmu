package bunnyEmu.main.net.packets.server;

import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.utils.BitPack;
import bunnyEmu.main.utils.Opcodes;

/**
 * Enable the character to fly
 * 
 * @author Marijn
 *
 */
public class SMSG_MOVE_SET_CANFLY extends ServerPacket {

	private Char character;

	public SMSG_MOVE_SET_CANFLY(Char character) {
		super(Opcodes.SMSG_MOVE_SET_CANFLY, 50);
		this.character = character;
	}

	@Override
	public boolean writeMoP() {
		BitPack bitPack = new BitPack(this);
		bitPack.setGuid(character.getGUID());

		bitPack.writeGuidMask(new byte[] {5, 3, 0, 2, 4, 1, 6, 7});
		bitPack.flush();
		bitPack.writeGuidBytes(new byte[] {1, 3, 5, 0, 7, 2, 4, 6});
		
		this.wrap();
		return true;
	}

}
