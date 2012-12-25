package bunnyEmu.main.net.ServerPackets;

import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.utils.Opcodes;

public class SMSG_PONG extends ServerPacket{
	
	private static int currentPing = 1;
	
	public SMSG_PONG(){
		super(Opcodes.SMSG_PONG, 4);
		this.put((byte) currentPing++);
	}

}
