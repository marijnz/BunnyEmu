/*
 * BunnyEmu - A Java WoW sandbox/emulator
 * https://github.com/marijnz/BunnyEmu
 */
package bunnyEmu.main.entities.packet;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import javax.xml.bind.DatatypeConverter;

import bunnyEmu.main.utils.BigNumber;
import bunnyEmu.main.utils.Log;

/**
 * A basic packet with generic read and write methods. 
 *
 * @author Marijn
 * 
 */
public abstract class Packet{
    public short nOpcode;
    public String sOpcode;
    public short size;
    public byte[] header;
    public ByteBuffer packet;
    
    public void put (Packet p){
    	this.nOpcode = p.nOpcode;
    	this.sOpcode = p.sOpcode;
    	this.size = p.size;
    	this.header = p.header;
    	this.packet = p.packet;
    }
    
    public void put(byte b){
        packet.put(b);
    }
    
    public void put(byte[] b){
        packet.put(b);
    }
    
    /**
     * @param s A Byte array as String
     */
    public void put(String s){
    	for(int i = 0; i < s.length(); i += 2)
    		this.put((byte) Integer.parseInt(s.substring(i, i+2), 16));
    }
    
    public void put(byte[] b, int offset, int length){
        packet.put(b, offset, length);
    }
    
    public void put(BigNumber b){
        packet.put(b.asByteArray());
    }
    
    public void putShort(short s){
        packet.putShort(s);
    }
    
    public void putInt(int i){
        packet.putInt(i);
    }
    
    public void putLong(long l){
        packet.putLong(l);
    }
    
    public void putFloat(float f){
        packet.putFloat(f);
    }
    
    public void putString(String s){
        packet.put(s.getBytes());
        packet.put((byte) 0);
    }
    
    public byte get(){
        return packet.get();
    }
    
    public short getShort(){
        return packet.getShort();
    }
    
    public int getInt(){
        return packet.getInt();
    }
    
    public long getLong(){
        return packet.getLong();
    }
    
    public ByteBuffer get(byte[] dst){
        return packet.get(dst);
    }
    
    public ByteBuffer get(byte[] dst, int offset, int length){
        return packet.get(dst, offset, length);
    }
    
    public String getString(){
    	StringBuilder b = new StringBuilder();

		for (byte c; (c = packet.get()) != 0;)
			b.append((char) c);

		return b.toString();
    }
    
    public String getString(int length){
    	StringBuilder b = new StringBuilder();

		for (int i = 0; i < length ; i++)
			b.append((char) packet.get());

		return b.toString();
    }
    
    public byte[] getFull(){
    	return packet.array();
    }
    
    public void setHeader(byte[] header){
        this.header = header;
    }
    
    public int position(){
        return packet.position();
    }
    
    public void position(int pos){
        packet.position(pos);
    }
    
    public String headerAsHex(){
        BigInteger bi = new BigInteger(1, header);
        return String.format("%0" + (header.length << 1) + "X", bi);
    }
    
    public String packetAsHex(){
    	if(packet.capacity() == 0)
    		return "none";
        BigInteger bi = new BigInteger(1, packet.array());
        return String.format("%0" + (packet.capacity() << 1) + "X", bi);
    }
    
    public String toStringBeautified(){
    	StringBuilder sb = new StringBuilder();
    	sb.append(this.sOpcode + " " + "<" +Integer.toHexString( nOpcode).toUpperCase() +  "> ");
    	sb.append("\n-------------------------------------------------------------------------\n");
    	sb.append("00  01  02  03  04  05  06  07  08  09  0A  0B  0C  0D  0E  0F");
    	sb.append("\n-------------------------------------------------------------------------\n");
    	
    	String sPacket = DatatypeConverter.printHexBinary(packet.array());
    	
    	int liner = 15;
    	for(int i = 0; i < packet.position(); i ++){
    		sb.append(sPacket.substring(i, i+2) + "  ");
    		if((i != 0) && ((i % liner) == 0))
    			sb.append("\n");
    		if(i == 17)
    			liner = 16;
    	}
    	return sb.toString();
    }
    
    /**
     * A representation of the packet holdings
     */
    @Override
    public String toString(){
       return this.sOpcode + " " + ("<" +Integer.toHexString( nOpcode).toUpperCase() +  "> " + new BigNumber(header).toHexString() + "  " + new BigNumber(packet.array()).toHexString());
    }
    
    /**
	 * Wraps the packet to the position of the bytebuffer
	 */
	
	public void wrap(){
		byte[] b = new byte[packet.position()];
		packet.position(0);
		packet.get(b, 0,  b.length);
		
		packet = ByteBuffer.wrap(b);
		size = (short) b.length;
	}
}
