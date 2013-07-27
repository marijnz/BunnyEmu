/*
 * BunnyEmu - A Java WoW sandbox/emulator
 * https://github.com/marijnz/BunnyEmu
 */
package bunnyEmu.main.entities.packet;


/**
 * Client packet that supports unsigned reading (returns val in double size!)
 * TODO: Actually use the unsigned reading instead of the hacky fixes.
 *
 * @author Marijn
 * 
 */
public class ClientPacket extends Packet implements IPacketReadable{
    
    public ClientPacket(){
        header = new byte[6];
    }
    
    public short readUnsignedByte() {
		return (short) (this.get() & 0xFF);
	}
	
	public int readUnsignedShort() {
		return (readUnsignedByte() << 8) | readUnsignedByte();
	}
	
	public int readUnsignedInteger() {
		return (readUnsignedByte() << 24) | (readUnsignedByte() << 16) | (readUnsignedByte() << 8) | readUnsignedByte();
	}
	
	public int readUnsignedLong() {
		return (readUnsignedByte() << 56) | (readUnsignedByte() << 48) | (readUnsignedByte() << 40) | (readUnsignedByte() << 32) | (readUnsignedByte() << 24) | (readUnsignedByte() << 16) | (readUnsignedByte() << 8) | readUnsignedByte();
	}

	@Override
	public boolean readVanilla() {
		return false;
	}

	@Override
	public boolean readBC() {
		return false;
	}

	@Override
	public boolean readWotLK() {
		return false;
	}

	@Override
	public boolean readCata() {
		return false;
	}

	@Override
	public boolean readMoP() {
		return false;
	}

	@Override
	public boolean readGeneric() {
		return false;
	}

}
