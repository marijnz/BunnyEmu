package bunnyEmu.main.entities;

import bunnyEmu.main.entities.packet.Packet;
import bunnyEmu.main.utils.Logger;
import bunnyEmu.main.utils.math.Vector4;
import bunnyEmu.main.utils.update.UpdateData;

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
		Logger.writeError("Created worldobject with GUID " + countGUID);
		this.setGUID(countGUID++);
		setPosition(1,1,1,1);
		this.data = new UpdateData();
	}
	
	public void writeUpdateFields(Packet p){
		data.writeUpdateFields(p);
	}
	
	public void setPosition(float x, float y, float z, int mapId) {
		this.position = new Vector4(x, y, z);
		this.mapID = mapId;
	}
	
	public int getMapID() {
		return mapID;
	}

	public void setMapID(int mapID) {
		this.mapID = mapID;
	}
	
	public Vector4 getPosition(){
		return position;
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
