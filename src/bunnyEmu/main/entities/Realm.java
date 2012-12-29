/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bunnyEmu.main.entities;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import bunnyEmu.main.net.WorldConnection;
import bunnyEmu.main.utils.Constants;
import bunnyEmu.main.utils.Log;

/**
 * 
 * @author Marijn
 */
public class Realm extends Thread {
	public int id;
	public String name;
	public String address;
	public int port;
	public int icon;
	public int flags;
	public int timezone;
	public float population;
	private int version;

	private ArrayList<Client> clients = new ArrayList<Client>(10);

	ServerSocket socket = null;

	public Realm() {
		this(1, "Marijnz ultimate server", "localhost", 3456, Constants.VERSION_WOTLK);
	}

	public Realm(int id, String name, String address, int port, int version) {
		this.id = id;
		this.name = "[" + version + "]" + name ;
		this.address = address + ":" + String.valueOf(port);
		Log.log(this.address);
		Log.log(port);
		this.port = port;
		this.version = version;
		start();
	}

	public int getSize() {
		return 8 + 4 + address.length() + name.length();
	}

	@Override
	public void run() {
		try {
			listenSocket();
		} catch (IOException ex) {
			Log.log(Log.ERROR, "Couldn't create a listensocket for realm " + id);
		}
	}

	private void listenSocket() throws IOException {
		socket = new ServerSocket(port);

		while (true) {
			new WorldConnection(socket.accept(), this);
			Log.log(Log.INFO, "Connection made to realm " + id);
		}
	}

	public void addClient(Client client) {
		clients.add(client);
	}

	public void removeClient(Client client) {
		clients.remove(client);
	}

	/**
	 * Get a client connected to this realm
	 * 
	 * @param name The name of the client
	 * @return
	 */
	public Client getClient(String name) {
		name = name.toUpperCase();
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getName().equals(name))
				return clients.get(i);
		}
		return null;
	}

	public int getVersion() {
		return version;
	}

}
