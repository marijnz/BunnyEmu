package bunnyEmu.main.net.packets.server;

import bunnyEmu.main.entities.Realm;
import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.utils.Opcodes;

/**
 * Realm data, MoP only.
 * 
 * @author Marijn
 * 
 */
public class SMSG_REALM_CACHE extends ServerPacket {

	private Realm realm;

	public SMSG_REALM_CACHE(Realm realm) {
		super(Opcodes.SMSG_REALM_CACHE, 100);
		this.realm = realm;
	}

	@Override
	public boolean writeMoP() {

		this.putInt(realm.id);
		this.putInt(0); // unknown
		
		this.put((byte)realm.name.length());
		this.put((byte)realm.name.length());
		
		this.put((byte) 1); // unknown
		
		this.putString(realm.name);
		this.putString(realm.name);
		
		this.wrap();
		return true;
	}

}
