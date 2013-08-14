package bunnyEmu.main.net.packets.server;

import bunnyEmu.main.entities.Realm;
import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.utils.BitPack;
import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.Opcodes;

/**
 * Character data, MoP only.
 * 
 * @author Marijn
 *
 */
public class SMSG_NAME_CACHE extends ServerPacket{

	private Char character;
	private Realm realm;

	public SMSG_NAME_CACHE(Char character, Realm realm) {
		super(Opcodes.SMSG_NAME_CACHE, 50);
		this.character = character;
		this.realm = realm;
	}
	
	@Override
	public boolean writeMoP(){

		BitPack bitPack = new BitPack(this);
		
		bitPack.write(0);
		bitPack.writeGuidMask(new byte[] {1, 3, 2});
		bitPack.write(character.getName().length(), 6);
		bitPack.writeGuidMask(new byte[] {6, 4, 0});
		bitPack.write(0);
		bitPack.writeGuidMask(new byte[] {5, 7});	

		bitPack.flush();
		
		bitPack.writeGuidBytes(new byte[] {1});
		
		this.putString(character.getName());
		
		bitPack.writeGuidBytes(new byte[] {0, 7});
		
		this.putInt(character.getCharRace());
		this.putInt(0); // ?
		this.putInt(1); // gender
		this.putInt(character.getCharClass());
		
		bitPack.writeGuidBytes(new byte[] {4, 6, 5});
		
		this.putInt(1);
		
		bitPack.writeGuidBytes(new byte[] {3, 2});
		
		Log.log("realm id: " + realm.id);
			
		this.wrap();
		return true;
	}
				
}
