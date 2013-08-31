package bunnyEmu.main.net.packets.server;

import bunnyEmu.main.entities.Client;
import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.Opcodes;
import bunnyEmu.main.utils.types.ObjectType;
import bunnyEmu.main.utils.types.UpdateFlag;
import bunnyEmu.main.utils.types.UpdateType;

/**
 * Initial update object
 * 
 * References:
 * https://sharesource.org/hg/sniffitzt/file/889649f15711/src/processor/opcodehandler/SMSG_UPDATE_OBJECT.java
 * 
 * 
 * @author Marijn
 *
 */
public class SMSG_UPDATE_OBJECT_CREATE extends UpdatePacket {

	private Char character;
	
	private boolean self;;
	
	public SMSG_UPDATE_OBJECT_CREATE(Client client, boolean self) {
		super(Opcodes.SMSG_UPDATE_OBJECT, 3000);
		this.self = self;
		character = client.getCurrentCharacter();
	}
	
	@Override
	public boolean writeMoP() {
		this.putShort((short) character.getMapID());
		this.putInt(1);
		this.put(UpdateType.CreateObject);
		this.writePackedGuid(character.getGUID());
		this.put(ObjectType.Player);

		byte updateFlags = UpdateFlag.Alive | UpdateFlag.Rotation;
		if(self)
			updateFlags |= UpdateFlag.Self;
			
		writeUpdateObjectMovementMoP(character, updateFlags);
		character.writeUpdateFields(this);

		this.wrap();

		return true;
	}
	
	@Override
	public boolean writeCata() {
		// Default on update packet
		this.putShort((short) character.getMapID());
		this.putInt(1); // update count
		
		//Create update packet
		this.put((byte) 3); // 3: create self?
		this.writePackedGuid(character.getGUID());
		this.put(ObjectType.Player);
		
		// Writing movement
		byte updateFlags = UpdateFlag.Alive | UpdateFlag.Self;
		writeUpdateObjectMovementCata(character, updateFlags);
		character.writeUpdateFields(this);
		
		//this.put((byte) 0); ?
		
		this.wrap();
		
		return true;
	}
	
	
	@Override
	public boolean writeWotLK() {
		this.putInt(1); // update count
		
		//Create update packet
		this.put((byte) UpdateType.CreateObject2);
		this.writePackedGuid(character.getGUID());
		this.put(ObjectType.Player);
		
						
		byte updateFlags = UpdateFlag.Alive | UpdateFlag.Self;
		writeUpdateObjectMovementWotLK(character, updateFlags);
		character.writeUpdateFields(this);
		
		// update fields
		//this.put("000000002A15008001C301C058DE00F1010800004F000040060000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000B96DDBB66DDBB60D0000000000000000000000000000000000000000000000000000000000000000000000000000000030607F020000000000000000000000000000000600FE20000000400000000000800000001E3F10000001000000190000000000803F0603000093080000930800009809000008000000E8030000F353C4403C000000060000000800000000080000D0070000022BC73E0000C03F3B0000003B0000000000204200002042000080000000000000000000000000000000000000");
		this.wrap(); 	
		
		return true;
	}
	
	// first
	//this.put("0100000002010104");
	// movement
	//this.put("710000000000000048139B0DA4D2AFC5D703BBC40CE2AD42564A033F00000000000020400000E04000009040711C9740000020400000E04000009040D00F49400000E040");

	
}
