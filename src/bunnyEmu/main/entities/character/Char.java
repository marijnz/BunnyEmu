package bunnyEmu.main.entities.character;

import java.util.ArrayList;

import bunnyEmu.main.entities.WorldObject;
import bunnyEmu.main.utils.types.fields.UpdateFields.ObjectFields;
import bunnyEmu.main.utils.types.fields.UpdateFields.PlayerFields;
import bunnyEmu.main.utils.types.fields.UpdateFields.UnitFields;

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
	private float speed = 1;
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	private ArrayList<Skill> skills = new ArrayList<Skill>();
	
	public Char(String name, float x, float y, float z, int mapID, byte charRace, byte charClass){
		this.setName(name);
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setMapID(mapID);
		this.setCharRace(charRace);
		this.setCharClass(charClass);
		this.charGender = 0;
		
		spells.add(new Spell(669)); // orcish
		skills.add(new Skill(109)); // language 1
		//skills.add(new Skill(315)); // language 2
		
		this.setUpdateFields();
	}
	

	protected void setUpdateFields() {
		 // ObjectFields
		 setUpdateField(ObjectFields.Guid, guid, Long.class);
		 setUpdateField(ObjectFields.Data, (long) 0, Long.class);
		 setUpdateField(ObjectFields.Type, 0x19, Integer.class);
		 setUpdateField(ObjectFields.Scale, 2.0f, Float.class);
		 
		 setUpdateField(UnitFields.Health, 13377, Integer.class);
         setUpdateField(UnitFields.MaxHealth, 13377, Integer.class);
         
         setUpdateField(UnitFields.Level, 240, Integer.class);
         
         setUpdateField(UnitFields.FactionTemplate, 0x74, Integer.class);
         
         setUpdateField(UnitFields.DisplayPower, this.charRace, Byte.class, (byte) 0);
         setUpdateField(UnitFields.DisplayPower, this.charClass, Byte.class, (byte) 1);
         setUpdateField(UnitFields.DisplayPower, this.charGender, Byte.class, (byte) 2);
         setUpdateField(UnitFields.DisplayPower, (byte) 0, Byte.class, (byte) 3);

         // PlayerFields
         setUpdateField(PlayerFields.HairColorID, (byte) 1, Byte.class, (byte) 0); // skin
         setUpdateField(PlayerFields.HairColorID, (byte) 1, Byte.class, (byte) 1); // face
         setUpdateField(PlayerFields.HairColorID, (byte) 1, Byte.class, (byte) 2); // hairstyle
         setUpdateField(PlayerFields.HairColorID, (byte) 1, Byte.class, (byte) 3); // haircolor
         setUpdateField(PlayerFields.RestState, (byte) 1, Byte.class, (byte) 0); 	// facial hair
         setUpdateField(PlayerFields.RestState, (byte) 0, Byte.class, (byte) 1);
         setUpdateField(PlayerFields.RestState, (byte) 0, Byte.class, (byte) 2);
         setUpdateField(PlayerFields.RestState, (byte) 2, Byte.class, (byte) 3); // ?
         
         setUpdateField(PlayerFields.ArenaFaction, (byte) 1, Byte.class, (byte) 0); //gender..?
         setUpdateField(PlayerFields.ArenaFaction, (byte) 0, Byte.class, (byte) 1); 
         setUpdateField(PlayerFields.ArenaFaction, (byte) 0, Byte.class, (byte) 2); 
         setUpdateField(PlayerFields.ArenaFaction, (byte) 0, Byte.class, (byte) 3); 
         setUpdateField(PlayerFields.WatchedFactionIndex, -1, Integer.class); // ?
         
         setUpdateField(PlayerFields.XP, 0, Integer.class);
         setUpdateField(PlayerFields.NextLevelXP, 400, Integer.class);
         setUpdateField(PlayerFields.CurrentSpecID, 0, Integer.class);
         
         int displayID = 2457 ;
         
         setUpdateField(UnitFields.DisplayID, displayID, Integer.class);
         setUpdateField(UnitFields.NativeDisplayID, displayID, Integer.class);
         
         for(int i = 0; i < skills.size(); i++)
        	 setUpdateField(PlayerFields.Skill + i, skills.get(i).getId(), Integer.class);
         
         setUpdateField(PlayerFields.HomePlayerRealm, 1, Integer.class);
         
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
	
	public ArrayList<Spell> getSpells(){
		return spells;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	
}
