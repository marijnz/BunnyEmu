package bunnyEmu.main.entities;

import java.util.BitSet;
import java.util.Hashtable;

import bunnyEmu.main.utils.Log;

/**
 * A WorldObject that supports update packets
 * 
 * @author Marijn
 * 
 */
public abstract class WorldObject {

	private static int countGUID = 8;

	protected float x;
	protected float y;
	protected float z;
	protected long guid;
	protected int mapID;

	private Hashtable<Integer, Integer> updateData = new Hashtable<Integer, Integer>();
	private BitSet mask;
	private int maskSize;

	public WorldObject() {
		Log.log("Created worldobject with GUID " + countGUID);
		this.setGUID(countGUID++);
		// 1973 in MoP?
		int dataLength = 1973;

		maskSize = (dataLength + 32) / 32;
		mask = new BitSet(dataLength);
	}
	
	public void setPosition(float x, float y, float z, int mapId){
		this.x = x;
		this.y = y;
		this.z = z;
		this.mapID = mapId;
	}
	
	/**
	 * Set a new update field with offset 0.
	 */
	public <T extends Number> void setUpdateField(int index, T value, Class<T> type) {
		this.setUpdateField(index, value, type, (byte) 0);
	}

	/**
	 * Set a new update field
	 * 
	 * @param index The index of the updatefield, i.e.: Mana regeneration
	 * @param value The value of the updatefield, i.e.: 235.32f
	 * @param type The type of the given value, i.e.: Float
	 * @param offset The offset of the offset, happens in case of i.e.: Skin, Face, Hairstyle, Haircolor, passed apart on the same index
	 */
	public <T extends Number> void setUpdateField(int index, T value, Class<T> type, byte offset) {
		if (type.isAssignableFrom(Byte.class) || type.isAssignableFrom(Short.class)) {
			mask.set(index);
			int tmpValue = type.isAssignableFrom(Byte.class) ? value.byteValue() : value.shortValue();
			int multipliedOffset = offset * (type.isAssignableFrom(Byte.class) ? 8 : 16);
			if (updateData.contains(index) && updateData.get(index) != null)
				updateData.put(index, (int)((int)updateData.get(index) | (int)(tmpValue << multipliedOffset)));
            else
            	updateData.put(index, (int)(tmpValue << multipliedOffset));

		} else if (type.isAssignableFrom(Long.class)){
			 mask.set(index);
			 mask.set(index + 1, true);
             long tmpValue = value.longValue();
             updateData.put(index, (int) (tmpValue & Integer.MAX_VALUE));
             updateData.put(index+1, (int) ((tmpValue >> 32) & Integer.MAX_VALUE));     
		} else if (type.isAssignableFrom(Float.class)){
			mask.set(index);
			updateData.put(index, Float.floatToIntBits(value.floatValue()));
		} else if (type.isAssignableFrom(Integer.class)){
			mask.set(index);
			updateData.put(index, value.intValue());
		} else{
			Log.log("Datatype not supported");
		}
	}

	/**
	 * Write the update hashtable to the update packet.
	 * @param p The packet the update data has to be written on
	 */
	public void writeUpdateFields(Packet p) {
		p.put((byte) maskSize);

		byte[] b = new byte[((mask.size() + 8) / 8) + 1];
		byte[] maskB = mask.toByteArray();
		for (int i = 0; i < maskB.length; i++)
			b[i] = maskB[i];

		p.put(b, 0, maskSize * 4);
		for (int i = 0; i < mask.size(); i++) {
			if (mask.get(i)) {
				p.putInt((int) updateData.get(i));
			}
		}
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
