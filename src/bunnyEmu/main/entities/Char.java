package bunnyEmu.main.entities;

/**
 * A Character
 * 
 * @author Marijn
 *
 */
public class Char {

	private static int countGUID = 12;
	
	private String name;
	private float x;
	private float y;
	private float z;
	private int mapID;
	private byte charRace;
	private byte charClass;
	private long guid;
	
	public Char(String name, float x, float y, float z, int mapID, byte charRace, byte charClass){
		this.setName(name);
		this.setX(x);
		this.setY(y);
		this.setZ(z);
		this.setMapID(mapID);
		this.setCharRace(charRace);
		this.setCharClass(charClass);
		this.setGUID(countGUID++);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
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

	public int getMapID() {
		return mapID;
	}

	public void setMapID(int mapID) {
		this.mapID = mapID;
	}

	public long getGUID() {
		return guid;
	}

	public void setGUID(long guid) {
		this.guid = guid;
	}
	
	
	
}
