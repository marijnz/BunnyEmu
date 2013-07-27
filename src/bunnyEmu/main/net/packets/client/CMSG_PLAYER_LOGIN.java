package bunnyEmu.main.net.packets.client;

import java.nio.ByteOrder;

import bunnyEmu.main.entities.packet.ClientPacket;
import bunnyEmu.main.net.packets.server.SMSG_LOGIN_VERIFY_WORLD;
import bunnyEmu.main.utils.BigNumber;
import bunnyEmu.main.utils.BitUnpack;
import bunnyEmu.main.utils.Log;

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
		byte[] guidMask = { 5, 7, 6, 1, 2, 3, 4, 0 };
		byte[] guidBytes = { 6, 4, 3, 5, 0, 2, 7, 1 };
		BitUnpack GuidUnpacker = new BitUnpack(this);
		
		guid = GuidUnpacker.GetGuid(guidMask, guidBytes);
		return true;
	}

	public long getGuid() {
		return guid;
	}

}
