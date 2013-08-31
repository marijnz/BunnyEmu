package bunnyEmu.main.net.packets.server;

import java.util.ArrayList;

import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.entities.character.Spell;
import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.utils.BitPack;
import bunnyEmu.main.utils.Opcodes;

/**
 * Sends all the spells known by the character
 * 
 * @author Marijn
 *
 */
public class SMSG_KNOWN_SPELLS extends ServerPacket {

	private Char character;

	public SMSG_KNOWN_SPELLS(Char character) {
		super(Opcodes.SMSG_KNOWN_SPELLS, 50 + character.getCharSpells().size() * 4 );
		this.character = character;
	}
	
	@Override
	public boolean writeMoP(){
		BitPack bitPack = new BitPack(this);

		ArrayList<Spell> spells = character.getCharSpells();
		
		bitPack.write(spells.size(), 22);
		bitPack.write(1);
		bitPack.flush();

		for(Spell spell : spells)
			this.putInt(spell.getId());
		
		this.wrap();
		return true;
	}
}
