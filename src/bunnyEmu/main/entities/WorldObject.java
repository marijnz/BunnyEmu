package bunnyEmu.main.entities;

import bunnyEmu.main.entities.packet.Packet;
import bunnyEmu.main.utils.math.Vector4;
import bunnyEmu.main.utils.update.UpdateData;
import misc.Logger;

/**
 * A WorldObject that supports update packets
 * 
 * @author Marijn
 * 
 */
public abstract class WorldObject {

	private static int countGUID = 1;

	protected Vector4 position;
	protected long guid;
	protected int mapID;

	protected UpdateData data;
	
	public WorldObject() {
		Logger.writeLog("Created worldobject with GUID " + countGUID, Logger.LOG_TYPE_VERBOSE);
		this.setGUID(countGUID++);
		setPosition(1,1,1,1);
		this.data = new UpdateData();
	}
	
	public void writeUpdateFields(Packet p){
		data.writeUpdateFields(p);
	}
	
	////////////////////// Get Methods:
	public int getMapID() {
		return mapID;
	}
	
	public Vector4 getPosition(){
		return position;
	}
	
	/** @return The generated GUID for this object */
	public long getGUID() {
		return guid;
	}

	////////////////////// Set Methods:
	public void setMapID(int mapID) {
		this.mapID = mapID;
	}
	
	public void setPosition(float x, float y, float z, int mapId) {
		this.position = new Vector4(x, y, z);
		this.mapID = mapId;
	}
	
	public void setGUID(long guid) {
		this.guid = guid;
	}

}
