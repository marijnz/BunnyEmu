package bunnyEmu.main.net.ServerPackets;

import bunnyEmu.main.entities.Client;
import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.utils.Opcodes;
import bunnyEmu.main.utils.types.ObjectType;
import bunnyEmu.main.utils.types.UpdateFlag;
import bunnyEmu.main.utils.types.UpdateType;

/**
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
		character.WriteUpdateFields(this, false);
		
		this.put((byte) 0);
		
		this.wrap();
		return true;
	}
	
	@Override
	public boolean writeWotLK(){
		this.put((byte) 3);
		this.writePackedGuid(character.getGUID());
		this.put(ObjectType.Player);
		
		byte updateFlags = UpdateFlag.Alive | UpdateFlag.Self;
		writeUpdateObjectMovementWotLK(character, updateFlags);
		character.WriteUpdateFields(this, false);
		
		this.put((byte) 0);
		
		this.wrap();
		
		return true;
	}
	
}
