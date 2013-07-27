package bunnyEmu.main.net.packets.server;

import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.utils.Opcodes;

/**
 * Instantly teleports the character to a new map.
 * 
 * @author Marijn
 * 
 */
public class SMSG_NEW_WORLD extends ServerPacket {

	private Char character;

	public SMSG_NEW_WORLD(Char character) {
		super(Opcodes.SMSG_NEW_WORLD, 20);
		this.character = character;
	}

	public boolean writeGeneric() {
		putInt(character.getMapID());
		putFloat(character.getX());
		putFloat(0); // orientation
		putFloat(character.getY());
		putFloat(character.getZ());
		return true;
	}
}
