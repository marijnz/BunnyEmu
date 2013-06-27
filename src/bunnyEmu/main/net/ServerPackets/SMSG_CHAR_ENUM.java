package bunnyEmu.main.net.ServerPackets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;

import bunnyEmu.main.entities.Client;
import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.utils.BitPack;
import bunnyEmu.main.utils.Versions;
import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.Opcodes;

/**
 * Character list response
 * 
 * @author Marijn
 * 
 */
public class SMSG_CHAR_ENUM extends ServerPacket {

	int charCount;
	private Client client;

	public SMSG_CHAR_ENUM(Client client) {
		super(Opcodes.SMSG_CHAR_ENUM, 6);
		this.client = client;
	}
	
	@Override
	public boolean writeGeneric(){
		charCount = client.getCharacters().size();
		create(Opcodes.SMSG_CHAR_ENUM, 350 * charCount, null);

		put((byte) charCount);

		for (int atChar = 0; atChar < charCount; atChar++) {
			Char currentChar = client.getCharacters().get(atChar);
			packet.order(ByteOrder.BIG_ENDIAN);
			putLong(currentChar.getGUID()); // PlayerGuid;
			packet.order(ByteOrder.LITTLE_ENDIAN);
			putString(currentChar.getName()); // name
			put((byte) currentChar.getCharRace()); // Race;
			put((byte) currentChar.getCharClass()); // Class;
			put((byte) 1); // Gender;
			put((byte) 1); // Skin;
			put((byte) 4); // Face;
			put((byte) 5); // Hair Style;
			put((byte) 0); // Hair Color;
			put((byte) 0); // Facial Hair;
			put((byte) 60); // Level;
			putInt(0); // Zone ID;
			putInt(currentChar.getMapID());
			putFloat(currentChar.getX()); // X
			putFloat(currentChar.getY()); // Y
			putFloat(currentChar.getZ()); // Z
			// Guild ID
			if (client.getVersion() < Versions.VERSION_CATA)
				putInt(0);
			else
				putLong(0);
			putInt(0); // Character Flags;
			if (client.getVersion() <= Versions.VERSION_BC)
				put((byte) 0); // Login Flags;
			else {
				putInt(0); // Login Flags;
				put((byte) 0); // Is Customize Pending?;
			}

			putInt(0); // Pet DisplayID;
			putInt(0); // Pet Level;
			putInt(0); // Pet FamilyID;

			int EQUIPMENT_SLOT_END = 19;
			if (client.getVersion() == Versions.VERSION_WOTLK)
				EQUIPMENT_SLOT_END++;

			for (int itemSlot = 0; itemSlot < EQUIPMENT_SLOT_END; ++itemSlot) {
				putInt(0); // Item DisplayID;
				put((byte) 0); // Item Inventory Type;
				if (client.getVersion() >= Versions.VERSION_BC)
					putInt(1); // Item EnchantID;
			}

			if (client.getVersion() >= Versions.VERSION_WOTLK) {
				int bagCount = 3;
				if (client.getVersion() >= Versions.VERSION_CATA)
					bagCount++;
				for (int c = 0; c < bagCount; c++) { // In 3.3.3 they added 3x new uint32 uint8 uint32
					putInt(0); // bag;
					put((byte) 18); // slot;
					putInt(1); // enchant?;
				}
			} else {
				putInt(0); // first bag display id
				put((byte) 0); // first bag inventory type
			}

			if (client.getVersion() == Versions.VERSION_BC)
				putInt(0); // enchant?

		}

		wrap();
		return true;
	}

	@Override
	public boolean writeMoP() {
		int charCount = client.getCharacters().size();
		create(Opcodes.SMSG_CHAR_ENUM, 350 * charCount, null);

		BitPack bitPack = new BitPack(this);

		bitPack.write(0, 23);
		bitPack.write(charCount, 17);

		if (charCount != 0) {
			for (int c = 0; c < charCount; c++) {
				Char currentChar = client.getCharacters().get(c);
				String name = currentChar.getName();

				bitPack.setGuid(currentChar.getGUID());
				bitPack.setGuildGuid(0);

				bitPack.writeGuidMask(new byte[] { 7, 0, 4 });
				bitPack.writeGuildGuidMask(new byte[] { 2 });
				bitPack.writeGuidMask(new byte[] { 5, 3 });
				try {
					bitPack.write(name.getBytes("US-ASCII").length, 7);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				bitPack.writeGuildGuidMask(new byte[] { 0, 5, 3 });
				bitPack.write(1); // login cinamatic
				bitPack.writeGuildGuidMask(new byte[] { 6, 7 });
				bitPack.writeGuidMask(new byte[] { 1 });
				bitPack.writeGuildGuidMask(new byte[] { 4, 1 });
				bitPack.writeGuidMask(new byte[] { 2, 6 });
			}

			bitPack.write(1);
			bitPack.flush();


			for (int c = 0; c < charCount; c++) {

				Char currentChar = client.getCharacters().get(c);
				String name = currentChar.getName();
				bitPack.setGuid(currentChar.getGUID());
				Log.log("GUID: " + currentChar.getGUID());
				bitPack.setGuildGuid(0);

				this.putInt(0); // CharacterFlags
				this.putInt(0); // pet family
				this.putFloat(currentChar.getX());

				bitPack.writeGuidBytes(new byte[] { 7 });
				bitPack.writeGuildGuidBytes(new byte[] { 6 });

				// Not implanted
				for (int j = 0; j < 23; j++) {
					this.putInt(0);
					this.put((byte) 0);
					this.putInt(0);
				}

				this.putFloat(currentChar.getX());
				this.put(currentChar.getCharClass());

				bitPack.writeGuidBytes(new byte[] { 5 });

				this.putFloat(currentChar.getY());

				bitPack.writeGuildGuidBytes(new byte[] { 3 });
				bitPack.writeGuidBytes(new byte[] { 6 });

				this.putInt(0); // pet level
				this.putInt(0); // pet display id

				bitPack.writeGuidBytes(new byte[] { 2 });
				bitPack.writeGuidBytes(new byte[] { 1 });

				this.put((byte) 0); // hair color
				this.put((byte) 0); // facial hair

				bitPack.writeGuildGuidBytes(new byte[] { 2 });

				this.putInt(0); // zone
				this.put((byte) 0);

				bitPack.writeGuidBytes(new byte[] { 0 });
				bitPack.writeGuildGuidBytes(new byte[] { 1 });

				this.put((byte) 0); // skin

				bitPack.writeGuidBytes(new byte[] { 4 });
				bitPack.writeGuildGuidBytes(new byte[] { 5 });

				try {
					this.put(name.getBytes("US-ASCII"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				bitPack.writeGuildGuidBytes(new byte[] { 0 });

				this.put((byte) 121); // level

				bitPack.writeGuidBytes(new byte[] { 3 });
				bitPack.writeGuildGuidBytes(new byte[] { 7 });

				this.put((byte) 0); // hair style

				bitPack.writeGuildGuidBytes(new byte[] { 4 });

				this.put((byte) 1); // gender
				this.putInt(currentChar.getMapID());
				this.putInt(0); // customize flags
				this.put(currentChar.getCharRace()); // gender
				this.put((byte) 0); // face
			}

		} else {
			bitPack.write(1);
			bitPack.flush();
		}
		this.wrap();
		return true;
	}

}
