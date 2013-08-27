package bunnyEmu.main.entities;

import bunnyEmu.main.entities.packet.Packet;
import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.update.UpdateData;

/**
 * A WorldObject that supports update packets
 * 
 * @author Marijn
 * 
 */
public abstract class WorldObject {

	private static int countGUID = 4;

	protected float x;
	protected float y;
	protected float z;
	protected long guid;
	protected int mapID;

	protected UpdateData data;
	
	public WorldObject() {
		Log.log("Created worldobject with GUID " + countGUID);
		this.setGUID(countGUID++);
		setPosition(1,1,1,1);
		this.data = new UpdateData();
	}
	
	public void writeUpdateFields(Packet p){
		data.writeUpdateFields(p);
	}
	
	public void setPosition(float x, float y, float z, int mapId) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.mapID = mapId;
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

	public int getMapID() {
		return mapID;
	}

	public void setMapID(int mapID) {
		this.mapID = mapID;
	}

	/**
	 * @return The generated GUID for this object
	 */
	public long getGUID() {
		return guid;
	}

	public void setGUID(long guid) {
		this.guid = guid;
	}

}
