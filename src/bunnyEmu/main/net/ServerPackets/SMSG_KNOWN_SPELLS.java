package bunnyEmu.main.net.ServerPackets;

import bunnyEmu.main.entities.Char;
import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.utils.BitPack;
import bunnyEmu.main.utils.Opcodes;

public class SMSG_KNOWN_SPELLS extends ServerPacket {

	private Char character;

	public SMSG_KNOWN_SPELLS(Char character) {
		super(Opcodes.SMSG_KNOWN_SPELLS, 50);
		this.character = character;

	}
	
	@Override
	public boolean writeMoP(){

		BitPack bitPack = new BitPack(this);

		bitPack.write(1, 24);
		bitPack.write(1);
		bitPack.flush();

		this.putInt(98);
		this.wrap();
		return true;
	}
}
