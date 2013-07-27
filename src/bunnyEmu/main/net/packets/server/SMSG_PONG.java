package bunnyEmu.main.net.packets.server;

import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.utils.Opcodes;

/**
 * Response to ping, unrequired?
 * 
 * @author Marijn
 *
 */
public class SMSG_PONG extends ServerPacket{
	
	private static int currentPing = 1;
	
	public SMSG_PONG(){
		super(Opcodes.SMSG_PONG, 4);
		this.put((byte) currentPing++);
	}

}
