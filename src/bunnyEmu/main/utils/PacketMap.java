package bunnyEmu.main.utils;

import java.util.HashMap;

/**
 * A HashMap with keys to both sides (Opcode String - Opcode Short)
 * 
 * @author Marijn
 * 
 */
public class PacketMap {

	public HashMap<String, Short> forward = new HashMap<String, Short>();
	public HashMap<Short, String> backward = new HashMap<Short, String>();

	public synchronized void add(String key, Short key2) {
		forward.put(key, key2);
		backward.put(key2, key);
	}

	/**
	 * Attach an opcode value to an opcode name, useful in case multiple opcodes are handled as the same packet(name) (movement packets)
	 * Note that getOpcodeValue won't return anything when using the passed value, as the name won't be saved as a key. (one way)
	 */
	public synchronized void addValue(Short value, String name) {
		backward.put(value, name);
	}

	/**
	 * @return The opcode value of the packet that belongs to the opcode's name
	 *         given
	 */

	public synchronized Short getOpcodeValue(String key) {
		return forward.get(key);
	}

	/**
	 * @return The String name of the packet that belongs to the key
	 */
	public synchronized String getOpcodeName(Short key) {
		return backward.get(key);
	}

	@SuppressWarnings("unchecked")
	public synchronized PacketMap clone() {
		PacketMap newMap = new PacketMap();
		newMap.forward = (HashMap<String, Short>) forward.clone();
		newMap.backward = (HashMap<Short, String>) backward.clone();

		return newMap;
	}

	public synchronized String toString() {
		return backward.toString() + forward.toString();
	}

}
