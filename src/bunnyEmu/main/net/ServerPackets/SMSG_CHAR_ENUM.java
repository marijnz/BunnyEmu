package bunnyEmu.main.net.ServerPackets;

import java.nio.ByteOrder;

import bunnyEmu.main.entities.Char;
import bunnyEmu.main.entities.Client;
import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.utils.Constants;
import bunnyEmu.main.utils.Opcodes;

public class SMSG_CHAR_ENUM extends ServerPacket {

	int charCount;

	public SMSG_CHAR_ENUM(Client client) {
		super(Opcodes.SMSG_CHAR_ENUM, 0);
		
		charCount = client.getCharacters().size();
		create(Opcodes.SMSG_CHAR_ENUM, 300 * charCount, null);

		put((byte) charCount);
		
		for (int atChar = 0; atChar < charCount; atChar++) {
			Char currentChar = client.getCharacters().get(atChar);
			//if (client.getVersion() > Constants.VERSION_BC)
				packet.order(ByteOrder.BIG_ENDIAN);
			putLong(currentChar.getGUID()); // PlayerGuid;
			packet.order(ByteOrder.LITTLE_ENDIAN);
			putString(currentChar.getName()); // name
			put((byte) currentChar.getCharRace()); // Race;
			put((byte) currentChar.getCharClass()); // Class;
			put((byte) 0); // Gender;
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
			putInt(0); // Guild ID;
			putInt(0); // Character Flags;
			if (client.getVersion() <= Constants.VERSION_BC)
				put((byte) 0); // Login Flags;
			else {
				putInt(0); // Login Flags;
				put((byte) 0); // Is Customize Pending?;
			}

			putInt(0); // Pet DisplayID;
			putInt(0); // Pet Level;
			putInt(0); // Pet FamilyID;

			int EQUIPMENT_SLOT_END = 19;
			if (client.getVersion() > Constants.VERSION_BC)
				EQUIPMENT_SLOT_END++;
			
			for (int itemSlot = 0; itemSlot < EQUIPMENT_SLOT_END; ++itemSlot) {
				putInt(0); // Item DisplayID;
				put((byte) 0); // Item Inventory Type;
				if (client.getVersion() > Constants.VERSION_BC)
					putInt(1); // Item EnchantID;
			}

			if (client.getVersion() > Constants.VERSION_BC) {
				for (int c = 0; c < 3; c++) { // In 3.3.3 they added 3x new uint32 uint8 uint32 {
					putInt(0); // bag;
					put((byte) 18); // slot;
					putInt(1); // enchant?;
				}
			} else {
				putInt(0); // first bag display id
				put((byte) 0); // first bag inventory type
			}

		}

		wrap();
	}

}
