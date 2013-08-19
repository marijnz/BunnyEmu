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
public class Char extends Unit {

	private String cName;
	
	private byte cHairStyle; 
	private byte cFaceStyle; 
	private byte cFacialHair;
	private byte cHairColor; 
	private byte cSkinColor;
	
	private byte cRace;
	private byte cClass;
	private byte cGender;
	
	private int cLevel;
	private float cSpeed = 1;
	
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	private ArrayList<Skill> skills = new ArrayList<Skill>();


	public Char(String cName, float x, float y, float z, int mapID, byte cHairStyle, 
				byte cFaceStyle, byte cFacialHair, byte cHairColor, byte cSkinColor, 
				byte cRace, byte cClass, byte cGender, int cLevel) {
		
		this.setCharName(cName);
		
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setMapID(mapID);

		this.setCharHairStyle(cHairStyle);
		this.setCharFaceStyle(cFaceStyle);
		this.setCharFacialHair(cFacialHair);
		this.setCharHairColor(cHairColor);
		this.setCharSkinColor(cSkinColor);

		this.setCharRace(cRace);
		this.setCharClass(cClass);
		this.setCharGender(cGender);
		
		this.setCharLevel(cLevel);

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
		setUpdateField("ObjectFields", "Scale", 1.0f, Float.class);
		
		setUpdateField("UnitFields", "Health", 13377, Integer.class);
        setUpdateField("UnitFields", "MaxHealth", 13377, Integer.class);
          
        setUpdateField("UnitFields", "Level", 1, Integer.class);
         
        setUpdateField("UnitFields", "FactionTemplate", 0x74, Integer.class);
         
        setUpdateField("UnitFields", "Bytes0", this.cRace, Byte.class, (byte) 0);
        setUpdateField("UnitFields", "Bytes0", this.cClass, Byte.class, (byte) 1);
        setUpdateField("UnitFields", "Bytes0", this.cGender, Byte.class, (byte) 2);
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
        int displayID = 55;
        
        setUpdateField("UnitFields", "DisplayID", displayID, Integer.class);
        setUpdateField("UnitFields", "NativeDisplayID", displayID, Integer.class);
        
        for (int i = 0; i < skills.size(); i++) {
        	setUpdateField("PlayerFields", "Skill", i, skills.get(i).getId(), Integer.class);
        }
        	setUpdateField("PlayerFields", "VirtualPlayerRealm", 1, Integer.class);
	}

	public String getCharName() {
		return cName;
	}

	public byte getCharHairStyle() {
		return cHairStyle;
	}

	public byte getCharFaceStyle() {
		return cFaceStyle;
	}

	public byte getCharFacialHair() {
		return cFacialHair;
	}
	
	public byte getCharHairColor() {
		return cHairColor;
	}
	
	public byte getCharSkinColor() {
		return cSkinColor;
	}
	
	public byte getCharRace() {
		return cRace;
	}
	
	public byte getCharClass() {
		return cClass;
	}
	
	public byte getCharGender() {
		return cGender;
	}
	
	public ArrayList<Spell> getCharSpells(){
		return spells;
	}

	public float getCharSpeed() {
		return cSpeed;
	}

	public byte getCharLevel() {
		return ((byte) cLevel);
	}

	public void setCharSpeed(float speed) {
		this.cSpeed = speed;
	}
	
	public void setCharLevel(int level) {
		this.cLevel = level;
	}


	/* these attributes should only be set on creation (for now) */
	
	private void setCharName(String name) {
		this.cName = name;
	}

	private void setCharHairStyle(byte cHairStyle) {
		this.cHairStyle = cHairStyle;
	}

	private void setCharFaceStyle(byte cFaceStyle) {
		this.cFaceStyle = cFaceStyle;
	}

	private void setCharFacialHair(byte cFacialHair) {
		this.cFacialHair = cFacialHair;
	}

	private void setCharHairColor(byte cHairColor) {
		this.cHairColor = cHairColor;
	}

	private void setCharSkinColor(byte cSkinColor) {
		this.cSkinColor = cSkinColor;
	}

	private void setCharRace(byte charRace) {
		this.cRace = charRace;
	}

	private void setCharClass(byte charClass) {
		this.cClass = charClass;
	}

	private void setCharGender(byte cGender) {
		this.cGender = cGender;
	}
}
