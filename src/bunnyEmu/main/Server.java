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
 * To login: Run and login with a WoW client with any username but make sure to use the password: "password".
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