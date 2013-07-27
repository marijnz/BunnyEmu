package bunnyEmu.main.net.packets.server;

import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.utils.Opcodes;

/**
 * Verifying world upon worldlogin
 * 
 * @author Marijn
 *
 */
public class SMSG_LOGIN_VERIFY_WORLD extends ServerPacket{

	public SMSG_LOGIN_VERIFY_WORLD(Char character){
		super(Opcodes.SMSG_LOGIN_VERIFY_WORLD, 20);
		putInt(character.getMapID()); 
		putFloat(character.getX());
		putFloat(character.getY());
		putFloat(character.getZ());
		putFloat(0); // orientation
	}
}
