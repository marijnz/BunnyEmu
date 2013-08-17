package bunnyEmu.main.net.packets.client;

import java.nio.ByteOrder;

import bunnyEmu.main.entities.packet.ClientPacket;
import bunnyEmu.main.utils.BitUnpack;

/**
 * Received upon world login
 * 
 * @author Marijn
 * 
 */
public class CMSG_PLAYER_LOGIN extends ClientPacket {

	private long guid;
	

	public boolean readVanilla() {
		packet.order(ByteOrder.BIG_ENDIAN);
		guid = getLong();
		return true;
	}

	public boolean readBC() {
		return readVanilla();
	}

	public boolean readWotLK() {
		return readVanilla();
	}

	public boolean readCata() {
		return readVanilla();
	}

	public boolean readMoP() {
		byte[] guidMask = { 2, 0, 4, 3, 5, 6, 1, 7 };
		byte[] guidBytes = { 0, 3, 7, 6, 1, 2, 4, 5 };
		
		BitUnpack GuidUnpacker = new BitUnpack(this);

		guid = GuidUnpacker.GetGuid(guidMask, guidBytes);
		return true;
	}

	public long getGuid() {
		return guid;
	}

}
