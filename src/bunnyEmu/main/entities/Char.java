package bunnyEmu.main.entities;

import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.types.UpdateFields.ObjectFields;
import bunnyEmu.main.utils.types.UpdateFields.PlayerFields;
import bunnyEmu.main.utils.types.UpdateFields.UnitFields;

/**
 * A Character
 * 
 * @author Marijn
 *
 */
public class Char extends WorldObject{

	
	private String name;
	private byte charRace;
	private byte charClass;
	private byte charGender;
	
	public Char(String name, float x, float y, float z, int mapID, byte charRace, byte charClass){
		this.setName(name);
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setMapID(mapID);
		this.setCharRace(charRace);
		this.setCharClass(charClass);
		this.charGender = 0;
		
		this.setUpdateFields();
	}
	

	protected void setUpdateFields() {
		 // ObjectFields
		 setUpdateField(ObjectFields.Guid, guid, Long.class);
		 setUpdateField(ObjectFields.Data, (long) 0, Long.class);
		 setUpdateField(ObjectFields.Type, 0x19, Integer.class);
		 setUpdateField(ObjectFields.Scale, 1.0f, Float.class);
		 
		 // UnitFields
		 setUpdateField(UnitFields.Charm, (long) 0, Long.class);
		 setUpdateField(UnitFields.Summon, (long) 0, Long.class);
		 setUpdateField(UnitFields.Critter, (long) 0, Long.class);
		 setUpdateField(UnitFields.CharmedBy, (long) 0, Long.class);
		 setUpdateField(UnitFields.SummonedBy, (long) 0, Long.class);
		 setUpdateField(UnitFields.CreatedBy, (long) 0, Long.class);
		 setUpdateField(UnitFields.Target, (long) 0, Long.class);
		 setUpdateField(UnitFields.ChannelObject, (long) 0, Long.class);
		 
		 setUpdateField(UnitFields.Health, 123, Integer.class);
		 

         for (int i = 0; i < 5; i++)
        	 setUpdateField(UnitFields.Power + i, 0, Integer.class);

         setUpdateField(UnitFields.MaxHealth, 123, Integer.class);

         for (int i = 0; i < 5; i++)
        	 setUpdateField(UnitFields.MaxPower + i, 0, Integer.class);
         
         setUpdateField(UnitFields.PowerRegenFlatModifier, 0, Integer.class);
         setUpdateField(UnitFields.PowerRegenInterruptedFlatModifier, 0, Integer.class);
         setUpdateField(UnitFields.BaseHealth, 0, Integer.class);
         setUpdateField(UnitFields.BaseMana, 0, Integer.class);
         setUpdateField(UnitFields.Level, 1, Integer.class);
         setUpdateField(UnitFields.FactionTemplate, 0x74, Integer.class);
         setUpdateField(UnitFields.Flags, 0, Integer.class);
         setUpdateField(UnitFields.Flags2, 0, Integer.class);


         for (int i = 0; i < 5; i++){
        	 setUpdateField(UnitFields.Stats + i, 0, Integer.class);
        	 setUpdateField(UnitFields.StatPosBuff + i, 0, Integer.class);
        	 setUpdateField(UnitFields.StatNegBuff + i, 0, Integer.class);
         }
         
         setUpdateField(UnitFields.DisplayPower, this.charRace, Byte.class, (byte) 0);
         setUpdateField(UnitFields.DisplayPower, this.charClass, Byte.class, (byte) 1);
         setUpdateField(UnitFields.DisplayPower, this.charGender, Byte.class, (byte) 2);
         setUpdateField(UnitFields.DisplayPower, (byte) 0, Byte.class, (byte) 3);

         // PlayerFields
         setUpdateField(PlayerFields.MaxLevel, 90, Integer.class);
         setUpdateField(PlayerFields.HairColorID, (byte) 0, Byte.class, (byte) 0); // skin
         setUpdateField(PlayerFields.HairColorID, (byte) 0, Byte.class, (byte) 1); // face
         setUpdateField(PlayerFields.HairColorID, (byte) 0, Byte.class, (byte) 2); // hairstyle
         setUpdateField(PlayerFields.HairColorID, (byte) 0, Byte.class, (byte) 3); // haircolor
         setUpdateField(PlayerFields.RestState, (byte) 0, Byte.class, (byte) 0); 	// facial hair
         
	}

	

	public byte getCharRace() {
		return charRace;
	}

	public void setCharRace(byte charRace) {
		this.charRace = charRace;
	}

	public byte getCharClass() {
		return charClass;
	}

	public void setCharClass(byte charClass) {
		this.charClass = charClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
