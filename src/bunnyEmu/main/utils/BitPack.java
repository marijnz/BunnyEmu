package bunnyEmu.main.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import bunnyEmu.main.entities.packet.Packet;

/**
 * Adopted from Arctium core, MoP
 * 
 * @author Marijn
 *
 */
public class BitPack {
	
	Packet packet;

    byte[] guidBytes = new byte[8];
    byte[] guildGuidBytes = new byte[8];
    byte[] transportGuidBytes = new byte[8];
    byte[] targetGuidBytes = new byte[8];

    public byte bitPosition;
    public byte bitValue;

	public BitPack(Packet packet) {
        this.packet = packet;
        bitPosition = 8;
    }
	
	public void write(boolean bit){
		this.write((bit) ? 1 : 0);
	}

    public void write(int bit){
        --bitPosition;
        if (bit != 0)
            bitValue |= (byte)(1 << (bitPosition));

        if (bitPosition == 0){
            bitPosition = 8;
            packet.put(bitValue);
            bitValue = 0;
        }
    }

    public void write(int bit, int count) {
        for (int i = count - 1; i >= 0; --i)
            write((bit >> i) & 1);
    }
    
    public void write(long bit, int count) {
        for (int i = count - 1; i >= 0; --i)
            write((safeLongToInt(bit) >> i) & 1);
    }
    
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) 
            throw new IllegalArgumentException (l + " cannot be cast to int without changing its value.");
        return (int) l;
    }

    public void writeGuidMask(byte[] order) {
        for (byte i = 0; i < order.length; i++)
            write(guidBytes[order[i]]);
    }

    public void writeGuildGuidMask(byte[] order) {
        for (byte i = 0; i < order.length; i++)
            write(guildGuidBytes[order[i]]);
    }

    public void writeTargetGuidMask(byte[] order) {
        for (byte i = 0; i < order.length; i++)
            write(targetGuidBytes[order[i]]);
    }

    public void writeTransportGuidMask(byte[] order) {
        for (byte i = 0; i < order.length; i++)
            write(transportGuidBytes[order[i]]);
    }

    public void writeGuidBytes(byte[] order) {
        for (byte i = 0; i < order.length; i++)
            if (guidBytes[order[i]] != 0)
                packet.put((byte)(guidBytes[order[i]] ^ 1));
            
    }

    public void writeGuildGuidBytes(byte[] order) {
        for (byte i = 0; i < order.length; i++)
            if (guildGuidBytes[order[i]] != 0)
                packet.put((byte)(guildGuidBytes[order[i]] ^ 1));
    }

    public void writeTargetGuidBytes(byte[] order) {
        for (byte i = 0; i < order.length; i++)
            if (targetGuidBytes[order[i]] != 0)
                packet.put((byte)(targetGuidBytes[order[i]] ^ 1));
    }

    public void writeTransportGuidBytes(byte[] order){
        for (byte i = 0; i < order.length; i++)
            if (transportGuidBytes[order[i]] != 0)
                packet.put((byte)(transportGuidBytes[order[i]] ^ 1));
    }

    public void flush() {
        if (bitPosition == 8)
            return;
        packet.put(bitValue);
        bitValue = 0;
        bitPosition = 8;
    }
    
    public void setGuid(long guid) {
		ByteBuffer b = ByteBuffer.allocate(8);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putLong(guid);
		this.guidBytes = b.array();
	}

	public void setGuildGuid(long guildGuid) {
		this.guildGuidBytes = ByteBuffer.allocate(8).putLong(guildGuid).array();
	}

	public void setTargetGuid(long targetGuid) {
	}
	public void setTransportGuid(long transportGuid) {
	}

}
