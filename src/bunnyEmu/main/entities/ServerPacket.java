/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bunnyEmu.main.entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Marijn
 */
public class ServerPacket extends Packet{
    
	public ServerPacket(String sOpcode){
		 create(sOpcode, 0, null);
	}
	
    public ServerPacket(String sOpcode, int size){
    	create(sOpcode, size, null);
    }
    
    public ServerPacket(String sOpcode, int size, byte oneByte){
    	byte[] b = {oneByte};
    	create(sOpcode, size, b);
    }
    
    public ServerPacket(String sOpcode, int size, byte[] data){
    	create(sOpcode, size, data);
    }
    
    public ServerPacket(String sOpcode, ByteBuffer data){
    	create(sOpcode, data.capacity(), data.array());
    }
    
    protected void create(String sOpcode, int size, byte[] data){
    	 this.sOpcode = sOpcode;
         this.size = (short) size;
         if(data == null)
        	 packet = ByteBuffer.allocate(size);
         else
        	 packet = ByteBuffer.wrap(data);
         packet.order(ByteOrder.LITTLE_ENDIAN);
         header = new byte[4];
    }
    
    /**
	 * Write packed guid. (from JMaNGOS)
	 *
	 * @param guid the guid
	 */
	public void writePackedGuid(long guid) {
		long tguid = guid;
		byte[] packGUID = new byte[8+1];
        packGUID[0] = 0;
        int size = 1;
        for (byte i = 0; tguid != 0; ++i)
        {
            if ((tguid & 0xFF) > 0)
            {
                packGUID[0] |= (1 << i);
                packGUID[size] =  (byte) (tguid & 0xFF);
                ++size;
            }

            tguid >>= 8;
        }
		packet.put(packGUID, 0, size);
	}
	
	@Override
	public byte[] getFull(){
		ByteBuffer temp = ByteBuffer.allocate(header.length + packet.capacity());
	    temp.put(header);
	    temp.put(packet);
	    return temp.array();
	}
	
}
