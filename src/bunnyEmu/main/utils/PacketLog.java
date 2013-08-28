package bunnyEmu.main.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bunnyEmu.main.ServerWindow;
import bunnyEmu.main.entities.packet.Packet;

public class PacketLog {
	
	public enum PacketType{
		SERVER, CLIENT_UNKNOWN, CLIENT_KNOWN_IMPLEMENTED, CLIENT_KNOWN_UNIMPLEMENTED
	}
	
	private static Map<Object,ArrayList<Packet>> multiPackets = new HashMap<Object,ArrayList<Packet>>();

	
	public static void logPacket(PacketType type, Packet p){
		if (!multiPackets.containsKey(type)){
			ArrayList<Packet> packetList = new ArrayList<Packet>();
			multiPackets.put(type, packetList);
		}
		ArrayList<Packet> packets = multiPackets.get(type);
		packets.add(p);

		// Notify the GUI new packets have been logged
		ServerWindow.notifyChange();
	}
	
	/**
	 * Get logged packets
	 * 
	 * @param types Decide which packets to return
	 * 
	 * @return A combination of types, of the logged packets
	 */
	public static ArrayList<Packet> getPackets(ArrayList<PacketType> types){
		ArrayList<Packet> packetList = new ArrayList<Packet>();
		for(PacketType type : types)
			if(multiPackets.containsKey(type))
				packetList.addAll(multiPackets.get(type));
		return packetList;
	}
	
	
}
