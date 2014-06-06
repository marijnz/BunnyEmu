package bunnyEmu.main.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import bunnyEmu.main.entities.Client;
import bunnyEmu.main.entities.packet.Packet;
import bunnyEmu.main.utils.Logger;
import bunnyEmu.main.utils.PacketLog;
import bunnyEmu.main.utils.PacketLog.PacketType;

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
	 * @param clientSocket The socket the client connected on.
	 */
	public Connection(Socket clientSocket) {
		this.clientSocket = clientSocket;
		initialize();
	}

	private void initialize() {
		System.out.println("Created Connection.");
		
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new DataInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			Logger.writeError("Couldn't create input and output streams.");
		}
	}
	
	public void close() {
		try {
			//data.allConnections.remove(this);
			out.close();
			in.close();
			clientSocket.close();
			interrupt();
		} catch (IOException e) {
			Logger.writeError("Couldn't close connection properly");
		}
	}

	/**
	 * 
	 * @param p The sent packet
	 * 
	 * @return True if successful, false if the actual capacity exceeds the given size
	 */
	protected boolean sendPacket(Packet p) {
		if (p.size < p.packet.capacity()) {
			String errorMessage = "";
			errorMessage += "Packet Error: The size of the packer (" + p.size;
			errorMessage += ") is less than the total capacity (" + p.packet.capacity() +").";
			Logger.writeError(errorMessage);
			
			return false;
		}
		PacketLog.logPacket(PacketType.SERVER, p);
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
