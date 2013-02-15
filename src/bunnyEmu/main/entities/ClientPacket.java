/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bunnyEmu.main.entities;

/**
 * Unsigned reading adopted from Bart.
 *
 * @author Marijn
 */
public class ClientPacket extends Packet{
    
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

}
