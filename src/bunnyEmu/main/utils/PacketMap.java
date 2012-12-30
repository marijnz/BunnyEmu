package bunnyEmu.main.utils;

import java.util.HashMap;

/**
 * A HashMap with keys to both sides
 * 
 * @author Marijn
 *
 */
public class PacketMap{

	  public HashMap<String, Short> forward = new HashMap<String, Short>();
	  public HashMap<Short,String> backward = new HashMap<Short, String>();

	  public synchronized void add(String key, Short key2) {
	    forward.put(key, key2);
	    backward.put(key2, key);
	  }

	  public synchronized Short getOpcodeValue(String key) {
	    return forward.get(key);
	  }

	  public synchronized String getOpcodeName(Short key) {
	    return backward.get(key);
	  }
	  
	  @SuppressWarnings("unchecked")
	  public synchronized  PacketMap clone(){
		  PacketMap newMap = new PacketMap();
		  newMap.forward = (HashMap<String, Short>) forward.clone();
		  newMap.backward =(HashMap<Short, String>) backward.clone();
		  
		  return newMap;
	  }
	  
	  public synchronized String toString(){
		  return backward.toString() + forward.toString();
	  }
	  
	}
