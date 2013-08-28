/*
 * BunnyEmu - A Java WoW sandbox/emulator
 * https://github.com/marijnz/BunnyEmu
 */
package bunnyEmu.main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
    
	public static String localIP = "127.0.0.1";
	//public static String localIP = "http://www.marijnzwemmer.com";
	
	private ServerSocket serverSocket;
	private ArrayList<Connection> connections = new ArrayList<Connection>(10);
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			ServerWindow.create();
			Thread.sleep(200);
		} catch (Exception e){
			e.printStackTrace();
		}
		Log.setlevel(Log.INFO);
		
		new Server().launch();
	}
	
	
    public void launch() {
        //RealmHandler.addRealm(new Realm(1, "Server test 2", localIP, 3344, 1));
        //RealmHandler.addRealm(new Realm(1, "Server test 2", localIP, 3345, 1));
        listenSocket();
     }
    
   public ServerSocket getServerSocket(){
	   return serverSocket;
   }
    
    private void listenSocket(){
        try{
        	Log.log(Log.INFO, "Launched BunnyEmu - listening on " + localIP);
        	InetAddress addr = InetAddress.getByName(localIP);
            serverSocket = new ServerSocket(3724, 0, addr);
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
