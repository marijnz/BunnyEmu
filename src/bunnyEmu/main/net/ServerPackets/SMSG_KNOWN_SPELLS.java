package bunnyEmu.main.net.ServerPackets;

import java.util.ArrayList;

import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.entities.character.Spell;
import bunnyEmu.main.utils.BitPack;
import bunnyEmu.main.utils.Opcodes;

public class SMSG_KNOWN_SPELLS extends ServerPacket {

	private Char character;

	public SMSG_KNOWN_SPELLS(Char character) {
		super(Opcodes.SMSG_KNOWN_SPELLS, 50 + character.getSpells().size() * 4 );
		this.character = character;

	}
	
	@Override
	public boolean writeMoP(){

		BitPack bitPack = new BitPack(this);

		ArrayList<Spell> spells = character.getSpells();
		
		bitPack.write(spells.size(), 24);
		bitPack.write(1);
		bitPack.flush();

		for(Spell spell : spells)
			this.putInt(spell.getId());
		
		this.wrap();
		return true;
	}
}
