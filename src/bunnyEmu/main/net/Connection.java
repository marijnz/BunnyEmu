package bunnyEmu.main.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import bunnyEmu.main.entities.Client;
import bunnyEmu.main.entities.packet.Packet;
import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.PacketLog;

/**
 * A connection made between the server and client, 
 * 
 * @author Marijn
 */
public abstract class Connection extends Thread {

	protected Socket clientSocket;
	protected PrintWriter out;
	protected DataInputStream in;
	protected Client client;

	/**
	 * Create a new connection attached based on the given socket
	 * 
	 * @param clientSocket The socket the client connected on
	 */
	public Connection(Socket clientSocket) {
		this.clientSocket = clientSocket;
		initialize();
	}

	private void initialize() {
		Log.log(Log.INFO, "Created Connection");
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new DataInputStream(clientSocket.getInputStream());

		} catch (IOException ex) {
			Log.log(Log.ERROR, "Couldn't create in and output streams");
		}
	}
	
	public void close() {
		try {
			//data.allConnections.remove(this);
			out.close();
			in.close();
			clientSocket.close();
			interrupt();
		} catch (IOException ex) {
			Log.log(Log.ERROR, "Couldn't close connection properly");
		}
	}

	/**
	 * 
	 * @param p The sent packet
	 * 
	 * @return True if succesful, false if the actual capacity exceeds the given size
	 */
	protected boolean sendPacket(Packet p) {
		if (p.size < p.packet.capacity()) {
			Log.log(Log.ERROR, "packet not sent: size " + p.size + " <  capacity " + p.packet.capacity());
			return false;
		}
		PacketLog.logPacket(p);
		return sendBytes(p.getFull());
	}

	private boolean sendBytes(byte[] b) {
		try {
			clientSocket.getOutputStream().write(b);
		} catch (IOException e) {
			return false;
		}
		return true;

	}

	public PrintWriter getOut() {
		return out;
	}

	public DataInputStream getIn() {
		return in;
	}

	public Socket getSocket() {
		return clientSocket;
	}

	/**
	 * @return The client that belongs to this connection.
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @param c The client that belongs to this connection.
	 */
	public void setClientParent(Client c) {
		client = c;
	}

}
