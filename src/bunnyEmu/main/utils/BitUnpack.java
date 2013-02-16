package bunnyEmu.main.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import bunnyEmu.main.entities.Packet;

/**
 * Bitunpacker, adopted by Arctium. MoP only
 * 
 * @author Marijn
 * 
 */

public class BitUnpack {
	private Packet packet;
	private int Position;
	private short Value;

	public BitUnpack(Packet packet) {
		this.packet = packet;
		Position = 8;
		Value = 0;
	}

	public boolean getBit() {
		if (Position == 8) {
			Value = packet.get();
			Position = 0;
		}

		int returnValue = Value;
		Value = (byte) (2 * returnValue);
		++Position;
		return ((returnValue >> 7) == 1 || (returnValue >> 7) == -1);
	}

	public int GetBits(byte bitCount) {
		int returnValue = 0;

		for (int i = bitCount - 1; i >= 0; --i)
			returnValue = getBit() ? (1 << i) | returnValue : returnValue;

		return returnValue;
	}

	public int GetNameLength(byte bitCount) {
		int returnValue = 0;
		// Unknown, always before namelength bits...
		getBit();

		for (int i = bitCount - 1; i >= 0; --i)
			returnValue = getBit() ? (1 << i) | returnValue : returnValue;

		return returnValue;
	}

	public long GetGuid(byte[] mask, byte[] bytes) {
		boolean[] guidMask = new boolean[mask.length];
		byte[] guidBytes = new byte[bytes.length];

		for (int i = 0; i < guidMask.length; i++)
			guidMask[i] = getBit();

		for (byte i = 0; i < bytes.length; i++)
			if (guidMask[mask[i]]){
				char b = (char) ( ( packet.get()) ^ 1);
				guidBytes[bytes[i]] = (byte) b;
			}
				
		
		ByteBuffer b =  ByteBuffer.wrap(guidBytes);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.position(0);
		return b.getLong();
	}
}
