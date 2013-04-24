package bunnyEmu.main.net.ServerPackets;

import bunnyEmu.main.entities.Realm;
import bunnyEmu.main.entities.ServerPacket;
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
		this.put((byte) 0); // unknown
		this.put((byte) 1); // unknown
		this.putString(realm.name);
		this.putString(realm.name);

		this.wrap();
		return true;
	}

}
