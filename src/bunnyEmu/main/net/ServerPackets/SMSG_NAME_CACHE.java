package bunnyEmu.main.net.ServerPackets;

import bunnyEmu.main.entities.Realm;
import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.entities.character.Char;
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
		this.writePackedGuid(character.getGUID());
		this.put((byte) 0);
		this.putString(character.getName());
		Log.log("realm id: " + realm.id);
		this.putInt(realm.id);
		this.put((byte) character.getCharRace());
		this.put((byte) 1); // gender
		this.put((byte) character.getCharClass());
		this.put((byte) 0); // ?
		
		this.wrap();
		return true;
	}
				
}
