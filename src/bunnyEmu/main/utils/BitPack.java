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

    byte[] GuidBytes = new byte[8];
    byte[] GuildGuidBytes = new byte[8];
    byte[] TransportGuidBytes = new byte[8];
    byte[] TargetGuidBytes = new byte[8];

    public byte BitPosition;
    public byte BitValue;

	public BitPack(Packet packet) {
        this.packet = packet;
        BitPosition = 8;
    }
	
	public void write(boolean bit){
		this.write((bit) ? 1 : 0);
	}

    public void write(int bit){
        --BitPosition;
        if (bit != 0)
            BitValue |= (byte)(1 << (BitPosition));

        if (BitPosition == 0){
            BitPosition = 8;
            packet.put(BitValue);
            BitValue = 0;
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
            write(GuidBytes[order[i]]);
    }

    public void writeGuildGuidMask(byte[] order) {
        for (byte i = 0; i < order.length; i++)
            write(GuildGuidBytes[order[i]]);
    }

    public void writeTargetGuidMask(byte[] order) {
        for (byte i = 0; i < order.length; i++)
            write(TargetGuidBytes[order[i]]);
    }

    public void writeTransportGuidMask(byte[] order) {
        for (byte i = 0; i < order.length; i++)
            write(TransportGuidBytes[order[i]]);
    }

    public void writeGuidBytes(byte[] order) {
        for (byte i = 0; i < order.length; i++)
            if (GuidBytes[order[i]] != 0)
                packet.put((byte)(GuidBytes[order[i]] ^ 1));
    }

    public void writeGuildGuidBytes(byte[] order) {
        for (byte i = 0; i < order.length; i++)
            if (GuildGuidBytes[order[i]] != 0)
                packet.put((byte)(GuildGuidBytes[order[i]] ^ 1));
    }

    public void writeTargetGuidBytes(byte[] order) {
        for (byte i = 0; i < order.length; i++)
            if (TargetGuidBytes[order[i]] != 0)
                packet.put((byte)(TargetGuidBytes[order[i]] ^ 1));
    }

    public void writeTransportGuidBytes(byte[] order){
        for (byte i = 0; i < order.length; i++)
            if (TransportGuidBytes[order[i]] != 0)
                packet.put((byte)(TransportGuidBytes[order[i]] ^ 1));
    }

    public void flush() {
        if (BitPosition == 8)
            return;
        packet.put(BitValue);
        BitValue = 0;
        BitPosition = 8;
    }
    
    public void setGuid(long guid) {
		ByteBuffer b = ByteBuffer.allocate(8);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putLong(guid);
		this.GuidBytes = b.array();
	}

	public void setGuildGuid(long guildGuid) {
		this.GuildGuidBytes = ByteBuffer.allocate(8).putLong(guildGuid).array();
	}

	public void setTargetGuid(long targetGuid) {
	}
	public void setTransportGuid(long transportGuid) {
	}

}
