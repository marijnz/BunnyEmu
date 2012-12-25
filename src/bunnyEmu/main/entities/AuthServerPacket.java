package bunnyEmu.main.entities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AuthServerPacket extends Packet{

	  public AuthServerPacket(short size){
		  this.size = size;
		  packet = ByteBuffer.allocate(size);
		  packet.order(ByteOrder.LITTLE_ENDIAN);
	  }
	  
}
