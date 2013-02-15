package bunnyEmu.main.net.ServerPackets;

import bunnyEmu.main.entities.Char;
import bunnyEmu.main.entities.Client;
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

	public SMSG_UPDATE_OBJECT_CREATE(Client client){
		super(Opcodes.SMSG_UPDATE_OBJECT, 3000);
		
		Char character = client.getCurrentCharacter();
		
		this.putShort((short) character.getMapID());
		this.putInt(1);
		this.put(UpdateType.CreateObject);
		this.writePackedGuid(character.getGUID());
		this.put(ObjectType.Player);
		
		int updateFlags = UpdateFlag.Alive | UpdateFlag.Rotation | UpdateFlag.Self;
		
		writeUpdateObjectMovement(character, updateFlags);
		character.WriteUpdateFields(this, false);
		
		this.put((byte) 0);
		
		this.wrap();
		
		/*
		UpdateFlag updateFlags = UpdateFlag.Alive | UpdateFlag.Rotation | UpdateFlag.Self;
        WorldMgr.WriteUpdateObjectMovement(ref updateObject, ref character, updateFlags);

        character.WriteUpdateFields(ref updateObject);
        character.WriteDynamicUpdateFields(ref updateObject);

        session.Send(ref updateObject);
		 * 
		 */
		

	}
	
}
