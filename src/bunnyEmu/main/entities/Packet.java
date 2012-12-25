/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bunnyEmu.main.entities;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import bunnyEmu.main.utils.BigNumber;

/**
 *
 * @author Marijn
 * 
 */
public abstract class Packet{
    public short opcode;
    public short size;
    public byte[] header;
    public ByteBuffer packet;
    
    public void put(byte b){
        packet.put(b);
    }
    
    public void put(byte[] b){
        packet.put(b);
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
    
    public String getString(){
    	StringBuilder b = new StringBuilder();

		for (byte c; (c = packet.get()) != 0;)
			b.append((char) c);

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
    
    @Override
    public String toString(){
       return ("<" +Integer.toHexString( opcode).toUpperCase() +  "> " + new BigNumber(header).toHexString() + "  " + new BigNumber(packet.array()).toHexString());
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
