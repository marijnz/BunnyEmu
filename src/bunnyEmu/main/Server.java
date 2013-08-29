/*
 * BunnyEmu - A Java WoW sandbox/emulator
 * https://github.com/marijnz/BunnyEmu
 */
package bunnyEmu.main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.UIManager;

import bunnyEmu.main.db.DatabaseConnection;
import bunnyEmu.main.handlers.ConfigHandler;
import bunnyEmu.main.net.Connection;
import bunnyEmu.main.net.LogonConnection;
import bunnyEmu.main.utils.Log;

/**
 * 
 * To login: Run and login with a WoW client with any username but make sure to
 * use the password: "password".
 * 
 * @author Marijn
 */
public class Server {

	public static String realmlist = null;
	
	public static Properties prop = null;

	private ServerSocket serverSocket;
	private ArrayList<Connection> connections = new ArrayList<Connection>(10);

	public static void main(String[] args) {
		try {
			
			prop = ConfigHandler.loadProperties();
			
			/* this shouldn't happen ? */
			if (prop == null) {
				Log.log("Property file could not be loaded.");
				System.exit(0);
			}
			
			realmlist = prop.getProperty("realmlistAddress");

			if (realmlist.isEmpty()) {
				Log.log("No realmlist set in server.conf.. unable to start.");
				System.exit(0);
			}
			
			if (Integer.parseInt(prop.getProperty("enableGUI")) != 0) {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				ServerWindow.create();
				Thread.sleep(200);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.setlevel(Log.DEBUG);

		new Server().launch();
	}

	public void launch() {
		// RealmHandler.addRealm(new Realm(1, "Server test 2", localIP, 3344,
		// 1));
		// RealmHandler.addRealm(new Realm(1, "Server test 2", localIP, 3345,
		// 1));
		listenSocket();
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	private void listenSocket() {
		try {
			Log.log(Log.INFO, "Launched BunnyEmu - listening on " + realmlist);
			
			InetAddress addr = InetAddress.getByName(realmlist);
			serverSocket = new ServerSocket(3724, 0, addr);

			/* load database connection */
			DatabaseConnection.initConnectionPool(prop);
			
			Log.log(Log.INFO, "BunnyEmu is open-source: https://github.com/marijnz/BunnyEmu");
			Log.log(Log.INFO, "Use any username to login, use password: 'password'");
			
			/* console commands are handled by this thread */
			Runnable loggerRunnable = new ConsoleLogger();
			Thread loggerThread = new Thread(loggerRunnable);
			loggerThread.start();

		} catch (IOException e) {
			Log.log("ERROR: port 3724 is not available!");
		}
		try {
			while (true) {
				LogonConnection con = new LogonConnection(serverSocket.accept());
				Log.log(Log.INFO, "Client connected to logon server");
				connections.add(con);
			}
		} catch (IOException e) {
			Log.log("Accept failed: 3724");
		}
	}
}
