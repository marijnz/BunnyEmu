package bunnyEmu.main.entities.character;

import java.util.ArrayList;

import bunnyEmu.main.entities.Realm;
import bunnyEmu.main.entities.Unit;
import bunnyEmu.main.utils.types.MovementSpeed;

/**
 * A Character
 * 
 * @author Marijn
 *
 */
public class Char extends Unit{

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
		
		movement = new MovementSpeed();
	}
	

	/**
	 * Prepare for initial update packet
	 * 
	 * @param realm The realm this char belongs to. Used for multiple version support, loading the right xml fields file, where indexes are defined
	 */
	public void setUpdateFields(Realm realm) {
		this.initFields(realm);
		 // ObjectFields
		 setUpdateField("ObjectFields", "Guid", guid, Long.class);
		 setUpdateField("ObjectFields", "Data", (long) 0, Long.class);
		 setUpdateField("ObjectFields", "Type", 0x19, Integer.class);
		 setUpdateField("ObjectFields", "Scale", 2.0f, Float.class);
		 
		 setUpdateField("UnitFields", "Health", 13377, Integer.class);
         setUpdateField("UnitFields", "MaxHealth", 13377, Integer.class);
          
         setUpdateField("UnitFields", "Level", 240, Integer.class);
         
         setUpdateField("UnitFields", "FactionTemplate", 0x74, Integer.class);
         
         setUpdateField("UnitFields", "Bytes0", this.charRace, Byte.class, (byte) 0);
         setUpdateField("UnitFields", "Bytes0", this.charClass, Byte.class, (byte) 1);
         setUpdateField("UnitFields", "Bytes0", this.charGender, Byte.class, (byte) 2);
         setUpdateField("UnitFields", "Bytes0", (byte) 0, Byte.class, (byte) 3); // powertype (mana etc.)

         // PlayerFields
         setUpdateField("PlayerFields", "Bytes1", (byte) 1, Byte.class, (byte) 0); // skin
         setUpdateField("PlayerFields", "Bytes1", (byte) 1, Byte.class, (byte) 1); // face
         setUpdateField("PlayerFields", "Bytes1", (byte) 1, Byte.class, (byte) 2); // hairstyle
         setUpdateField("PlayerFields", "Bytes1", (byte) 1, Byte.class, (byte) 3); // haircolor
         setUpdateField("PlayerFields", "Bytes2", (byte) 1, Byte.class, (byte) 0); 	// facial hair
         setUpdateField("PlayerFields", "Bytes2", (byte) 0, Byte.class, (byte) 1); // PB2_2
         setUpdateField("PlayerFields", "Bytes2", (byte) 0, Byte.class, (byte) 2); // Bankbag Slots
         setUpdateField("PlayerFields", "Bytes2", (byte) 2, Byte.class, (byte) 3); // Reststate
         
         setUpdateField("PlayerFields", "Bytes3", (byte) 1, Byte.class, (byte) 0); // gender
         setUpdateField("PlayerFields", "Bytes3", (byte) 0, Byte.class, (byte) 1); // drunkness
         setUpdateField("PlayerFields", "Bytes3", (byte) 0, Byte.class, (byte) 2); // PB3_3?
         setUpdateField("PlayerFields", "Bytes3", (byte) 0, Byte.class, (byte) 3); // PvPRank
         setUpdateField("PlayerFields", "WatchedFactionIndex", -1, Integer.class); // ?
         
         setUpdateField("PlayerFields", "XP", 0, Integer.class);
         setUpdateField("PlayerFields", "NextLevelXP", 400, Integer.class);
         setUpdateField("PlayerFields", "CurrentSpecID", 0, Integer.class);
         
         // 2457 = panda?
         int displayID = 55 ;
         
         setUpdateField("UnitFields", "DisplayID", displayID, Integer.class);
         setUpdateField("UnitFields", "NativeDisplayID", displayID, Integer.class);
         
         for(int i = 0; i < skills.size(); i++)
        	 setUpdateField("PlayerFields", "Skill", i, skills.get(i).getId(), Integer.class);
         
         setUpdateField("PlayerFields", "VirtualPlayerRealm", 1, Integer.class);
         
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
