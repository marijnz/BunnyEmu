package bunnyEmu.main.net.packets.server;

import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.utils.AuthCodes;
import bunnyEmu.main.utils.BitPack;
import bunnyEmu.main.utils.Opcodes;

/**
 * AuthResponse
 * 
 * TODO: Load actual classes and races out of database or dbc
 * 
 * @author Marijn
 * 
 */
public class SMSG_AUTH_RESPONSE extends ServerPacket {


	public SMSG_AUTH_RESPONSE() {
		super(Opcodes.SMSG_AUTH_RESPONSE, 80);
	}

	public boolean writeMoP() {
		BitPack bitPack = new BitPack(this);
		
		bitPack.write(0); // inqueue
		bitPack.write(1); // account data
		
		bitPack.write(0);
		bitPack.write(0);
		bitPack.write(13, 23);	// race count
		bitPack.write(0);
		bitPack.write(0, 21);
		
		bitPack.write(11, 23);	// class count
		bitPack.write(0, 22);
		bitPack.write(0);
		
		bitPack.flush();
		
		this.put((byte) 0);
		
		for (int c = 0; c < 11; c++) {	// activate classes
			this.put((byte) 0);
			this.put((byte) (c+1));
		}
		
		for (int c = 0; c < 13; c++) {	// activate races
			this.put((byte) 0);
			this.put((byte) (c+1));
		}
		
		this.putInt(0);
		this.putInt(0);
		this.putInt(0);
		
		this.put((byte) 3);	// expansion
		this.put((byte) 3);	// expansion
		
		this.put((byte) AuthCodes.AUTH_RESPONSE_OK);
		
		return true;
	}
}
