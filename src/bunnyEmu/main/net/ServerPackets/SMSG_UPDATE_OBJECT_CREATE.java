package bunnyEmu.main.net.ServerPackets;

import bunnyEmu.main.entities.Client;
import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.utils.Opcodes;
import bunnyEmu.main.utils.types.ObjectType;
import bunnyEmu.main.utils.types.UpdateFlag;
import bunnyEmu.main.utils.types.UpdateType;

/**
 * Initial update object
 * 
 * @author Marijn
 *
 */
public class SMSG_UPDATE_OBJECT_CREATE extends UpdatePacket{

	private Char character;
	
	public SMSG_UPDATE_OBJECT_CREATE(Client client){
		super(Opcodes.SMSG_UPDATE_OBJECT, 3000);
		
		character = client.getCurrentCharacter();
	}
	
	@Override
	public boolean writeMoP(){
		this.putShort((short) character.getMapID());
		this.putInt(1);
		this.put(UpdateType.CreateObject);
		this.writePackedGuid(character.getGUID());
		this.put(ObjectType.Player);
		
		byte updateFlags = UpdateFlag.Alive | UpdateFlag.Rotation | UpdateFlag.Self;
		writeUpdateObjectMovementMoP(character, updateFlags);
		character.WriteUpdateFields(this);
		
		this.put((byte) 0);
		
		this.wrap();
		return true;
	}
	
	@Override
	public boolean writeCata(){
		// Default on update packet
		this.putShort((short) character.getMapID());
		this.putInt(1); // one update block?
		
		//Create update packet
		this.put(UpdateType.CreateObject);
		this.writePackedGuid(character.getGUID());
		this.put(ObjectType.Player);
		
		// Writing movement
		byte updateFlags = UpdateFlag.Alive | UpdateFlag.Self;
		writeUpdateObjectMovementCata(character, updateFlags);
		character.WriteUpdateFields(this);
		
		//this.put((byte) 0); ?
		
		this.wrap();
		
		return true;
	}
	
}
