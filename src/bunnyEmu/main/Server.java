/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bunnyEmu.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import bunnyEmu.main.entities.Realm;
import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.handlers.RealmHandler;
import bunnyEmu.main.net.Connection;
import bunnyEmu.main.net.LogonConnection;
import bunnyEmu.main.utils.Log;

/**
 *
 * @author Marijn
 */
public class Server {
    
	public static String localIP = "localhost";
	
	private ServerSocket serverSocket;
	private ArrayList<Connection> connections = new ArrayList<Connection>(10);
	
	public static void main(String[] args) {
		Log.setlevel(Log.ERROR);
		new Server().launch();
	}
	
	
    public void launch() {
        
        RealmHandler.addRealm(new Realm(1, "Server test 2", "localhost", 3344, 1));
        RealmHandler.addRealm(new Realm(1, "Server test 2", "localhost", 3345, 1));
        listenSocket();
     }
    
    /**
     * Loading a packet text file assuming an arcemu-like packet dump
     * @param packetDir The packet to be loaded
     * @param capacity	How much size should be buffered for the returned data
     */
    public static ServerPacket loadPacket(String packetDir, int capacity){
    	String opcode = null;
    	ByteBuffer data = ByteBuffer.allocate(capacity);
    	try {
    		BufferedReader in = new BufferedReader(new FileReader("assets" + "/" + packetDir));
            String line = "";
            line = in.readLine(); // opcode and info line
            int firstHook = line.indexOf("(")+3;

            opcode = line.substring(firstHook, line.indexOf(")", firstHook));
            for (int i = 0; i < 3; i++)
            	in.readLine(); // unused text
            
            // "|" = start or end of line
            while((line = in.readLine()).charAt(0) == '|'){
            	String curHexChar = "";
            	int i = 1; // Skip the first "|"
            	while(true){
            		curHexChar = line.substring(i, i + 2);
            		i += 3;
            		if(curHexChar.contains(" ") || curHexChar.contains("|"))
            			break;
            		data.put((byte) Integer.parseInt(curHexChar, 16)); // Read two bytes, hex
            	}
            }
            in.close();
        } catch (IOException e) {
            //log the exception
        }
    	
    	return new ServerPacket(Short.parseShort(opcode, 16), data);
    }
    
    private void listenSocket(){
        try{
            serverSocket = new ServerSocket(3724);
        } catch (IOException e) {
            Log.log("3724 not working");
        }
        try{
            while(true){
                   LogonConnection con = new LogonConnection(serverSocket.accept());
                   Log.log(Log.INFO, "Client connected to logon server");
                   connections.add(con);
            }
        } catch (IOException e) {
            Log.log("Accept failed: 3724");
        }
    }
}
