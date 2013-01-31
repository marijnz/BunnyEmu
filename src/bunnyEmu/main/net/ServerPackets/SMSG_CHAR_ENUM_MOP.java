package bunnyEmu.main.net.ServerPackets;

import java.io.UnsupportedEncodingException;

import bunnyEmu.main.entities.Char;
import bunnyEmu.main.entities.Client;
import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.utils.BigNumber;
import bunnyEmu.main.utils.BitPack;
import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.Opcodes;


public class SMSG_CHAR_ENUM_MOP extends ServerPacket {
	
	public SMSG_CHAR_ENUM_MOP(Client client) throws UnsupportedEncodingException{
		super(Opcodes.SMSG_CHAR_ENUM, 6);
		int charCount = client.getCharacters().size();
		create(Opcodes.SMSG_CHAR_ENUM, 350 * charCount, null);
		
      BitPack bitPack = new BitPack(this);

      bitPack.write(0, 23);
      bitPack.write(charCount, 17);
    
      if (charCount != 0) {
    	  for (int c = 0; c < charCount; c++){
        	  Char currentChar = client.getCharacters().get(c);
              String name       = currentChar.getName();
              boolean loginCinematic = false;

              bitPack.setGuid(currentChar.getGUID());
              bitPack.setGuildGuid(0);

              bitPack.writeGuidMask(new byte[]{7, 0, 4});
              bitPack.writeGuildGuidMask(new byte[]{2});
              bitPack.writeGuidMask(new byte[]{5, 3});
              bitPack.write(name.getBytes("US-ASCII").length + 1, 7);
              bitPack.writeGuildGuidMask(new byte[]{0, 5, 3});
              bitPack.write(1); // login cinamatic
              bitPack.writeGuildGuidMask(new byte[]{6, 7});
              bitPack.writeGuidMask(new byte[]{1});
              bitPack.writeGuildGuidMask(new byte[]{4, 1});
              bitPack.writeGuidMask(new byte[]{2, 6});
          }
          
          bitPack.write(1);
          bitPack.flush();
          
      	//this.put(new byte[]{00, 00,00,00, 0x01,0x40,0x20,(byte) 0x80, (byte) 0x80});
          
      	
          for (int c = 0; c < charCount; c++){

        	  Char currentChar = client.getCharacters().get(c);
        	  String name = currentChar.getName();
        	  bitPack.setGuid(currentChar.getGUID());
              bitPack.setGuildGuid(0);

              this.putInt(0); // CharacterFlags
              this.putInt(0); // pet family
              this.putFloat(currentChar.getX());

              bitPack.writeGuidBytes(new byte[]{7});
              bitPack.writeGuildGuidBytes(new byte[]{6});

              // Not implanted
              for (int j = 0; j < 23; j++)
              {
                  this.putInt(0);
                  this.put((byte) 0);
                  this.putInt(0);
              }

              this.putFloat(currentChar.getX());
              this.put(currentChar.getCharClass());

              bitPack.writeGuidBytes(new byte[]{5});

              this.putFloat(currentChar.getY());

              bitPack.writeGuildGuidBytes(new byte[]{3});
              bitPack.writeGuidBytes(new byte[]{6});

              this.putInt(0); // pet level
              this.putInt(0); // pet display id

              bitPack.writeGuidBytes(new byte[]{2});
              bitPack.writeGuidBytes(new byte[]{1});

              this.put((byte) 0); // hair color
              this.put((byte) 0); // facial hair

              bitPack.writeGuildGuidBytes(new byte[]{2});

              this.putInt(0); // zone
              this.put((byte) 0);

              bitPack.writeGuidBytes(new byte[]{0});
              bitPack.writeGuildGuidBytes(new byte[]{1});

              this.put((byte) 0); // skin

              bitPack.writeGuidBytes(new byte[]{4});
              bitPack.writeGuildGuidBytes(new byte[]{5});

              this.putString(name);

              bitPack.writeGuildGuidBytes(new byte[]{0});

              this.put((byte) 60); // level

              bitPack.writeGuidBytes(new byte[]{3});
              bitPack.writeGuildGuidBytes(new byte[]{7});

              this.put((byte) 0); // hair style

              bitPack.writeGuildGuidBytes(new byte[]{4});

              this.put((byte) 0); // gender
              this.putInt(currentChar.getMapID());
              this.putInt(0); // customize flags
              this.put(currentChar.getCharRace()); // gender
              this.put((byte) 0); // face
          }
          
      }
      else
      {
          bitPack.write(1);
          bitPack.flush();
      };
      this.wrap();
	}
}
