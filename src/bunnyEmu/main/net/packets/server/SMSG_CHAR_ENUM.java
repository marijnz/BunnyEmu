package bunnyEmu.main.net.packets.server;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;

import bunnyEmu.main.entities.Client;
import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.enums.ClientVersion;
import bunnyEmu.main.utils.BitPack;
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
		create(Opcodes.SMSG_CHAR_ENUM, 350 * charCount + 10, null);

		put((byte) charCount);

		for (int atChar = 0; atChar < charCount; atChar++) {
			Char currentChar = client.getCharacters().get(atChar);
			packet.order(ByteOrder.BIG_ENDIAN);
			putLong(currentChar.getGUID()); // PlayerGuid;
			packet.order(ByteOrder.LITTLE_ENDIAN);
			putString(currentChar.getCharName()); // name
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
			putFloat(currentChar.getPosition().getX()); // X
			putFloat(currentChar.getPosition().getY()); // Y
			putFloat(currentChar.getPosition().getZ()); // Z
			// Guild ID
			if (client.getVersion() < ClientVersion.VERSION_CATA.getNumber())
				putInt(0);
			else
				putLong(0);
			putInt(0); // Character Flags;
			if (client.getVersion() <= ClientVersion.VERSION_BC.getNumber())
				put((byte) 0); // Login Flags;
			else {
				putInt(0); // Login Flags;
				put((byte) 0); // Is Customize Pending?;
			}

			putInt(0); // Pet DisplayID;
			putInt(0); // Pet Level;
			putInt(0); // Pet FamilyID;

			int EQUIPMENT_SLOT_END = 19;
			if (client.getVersion() == ClientVersion.VERSION_WOTLK.getNumber())
				EQUIPMENT_SLOT_END++;

			for (int itemSlot = 0; itemSlot < EQUIPMENT_SLOT_END; ++itemSlot) {
				putInt(0); // Item DisplayID;
				put((byte) 0); // Item Inventory Type;
				if (client.getVersion() >= ClientVersion.VERSION_BC.getNumber())
					putInt(1); // Item EnchantID;
			}

			if (client.getVersion() >= ClientVersion.VERSION_WOTLK.getNumber()) {
				int bagCount = 3;
				if (client.getVersion() >= ClientVersion.VERSION_CATA.getNumber())
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

			if (client.getVersion() == ClientVersion.VERSION_BC.getNumber())
				putInt(0); // enchant?

		}

		wrap();
		return true;
	}

	@Override
	public boolean writeMoP() { // 5.3.0a
		int charSize = 0;
		int charCount = client.getCharacters().size();
		
		// if no characters we still need space to send
		if (charCount == 0) {
			charSize = 350;
		}
		else {
			charSize = 350 * charCount;
		}
		
		
		create(Opcodes.SMSG_CHAR_ENUM, charSize, null);

		BitPack bitPack = new BitPack(this);

		bitPack.write(1);
		
		bitPack.write(charCount, 16);

		if (charCount != 0) {
			for (int c = 0; c < charCount; c++) {
				Char currentChar = client.getCharacters().get(c);
				String name = currentChar.getCharName();

				bitPack.setGuid(currentChar.getGUID());
				bitPack.setGuildGuid(0);

				bitPack.writeGuidMask(new byte[] { 1 });
				bitPack.writeGuildGuidMask(new byte[] { 5, 7, 6 });
				bitPack.writeGuidMask(new byte[] { 5 });
				bitPack.writeGuildGuidMask(new byte[] { 3 });
				bitPack.writeGuidMask(new byte[] { 2 });
				bitPack.writeGuildGuidMask(new byte[] { 4 });
				bitPack.writeGuidMask(new byte[] { 7 });
				
				try {
					bitPack.write(name.getBytes("US-ASCII").length, 6);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				bitPack.write(false); // login cinematic
				bitPack.writeGuildGuidMask(new byte[] { 1 });
				bitPack.writeGuidMask(new byte[] { 4 });
				bitPack.writeGuildGuidMask(new byte[] { 2, 0 });
				bitPack.writeGuidMask(new byte[] { 6, 3, 0 });
			}

			bitPack.write(0, 21);
			bitPack.flush();

			for (int c = 0; c < charCount; c++) {

				Char currentChar = client.getCharacters().get(c);
				String name = currentChar.getCharName();
				bitPack.setGuid(currentChar.getGUID());
				bitPack.setGuildGuid(0);

				bitPack.writeGuidBytes(new byte [] { 4 });
				
				this.put((byte)currentChar.getCharRace());
				
				bitPack.writeGuidBytes(new byte [] { 6 });
				bitPack.writeGuildGuidBytes(new byte[] { 1 });
				
				this.put((byte) 0);
				this.put(currentChar.getCharHairStyle());	// hair style
				
				bitPack.writeGuildGuidBytes(new byte[] { 6 });
				bitPack.writeGuidBytes(new byte [] { 3 });
				
				this.putFloat(currentChar.getPosition().getX());
				this.putInt(0);								// CharacterFlags TODO: check
				
				bitPack.writeGuildGuidBytes(new byte[] { 0 });
				
				this.putInt(0);								// pet level
				this.putInt(currentChar.getMapID());		// map
				
				bitPack.writeGuildGuidBytes(new byte[] { 7 });
				
				this.putInt(0);								// customize flags
				
				bitPack.writeGuildGuidBytes(new byte[] { 4 });
				bitPack.writeGuidBytes(new byte[] { 2, 5 });

				this.putFloat(currentChar.getPosition().getY());
				this.putInt(0);								// pet family
		
				try {
					this.put(name.getBytes("US-ASCII"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				this.putInt(0);								// pet display id
				
				bitPack.writeGuildGuidBytes(new byte[] { 3 });
				bitPack.writeGuidBytes(new byte[] { 7 });

				this.put(currentChar.getCharLevel());		// level
				
				bitPack.writeGuidBytes(new byte[] { 1 });
				bitPack.writeGuildGuidBytes(new byte[] { 2 });
				
				// Not implemented
				for (int j = 0; j < 23; j++) {
					this.putInt(0);
					this.putInt(0);
					this.put((byte) 0);
				}

				this.putFloat(currentChar.getPosition().getZ());
				this.putInt(0);								// zone
				this.put(currentChar.getCharFacialHair());	// facial hair
				this.put(currentChar.getCharClass());


				bitPack.writeGuildGuidBytes(new byte[] { 5 });

				this.put(currentChar.getCharSkinColor());	// skin
				this.put(currentChar.getCharGender());		// gender
				this.put(currentChar.getCharFaceStyle());	// face

				bitPack.writeGuidBytes(new byte[] { 0 });

				this.put(currentChar.getCharHairColor());	// hair color
			}

		} else {
			bitPack.write(0, 21);
			bitPack.flush();
		}
		
		this.wrap();

		return true;
	}
}
