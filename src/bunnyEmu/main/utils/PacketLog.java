package bunnyEmu.main.utils;

import java.util.ArrayList;

import bunnyEmu.main.ServerWindow;
import bunnyEmu.main.entities.packet.Packet;

public class PacketLog {

	private static ArrayList<Packet> packets = new ArrayList<Packet>();
	
	public static void logPacket(Packet p){
		packets.add(p);
		ServerWindow.updatePackets(packets);
	}
	
	
}
