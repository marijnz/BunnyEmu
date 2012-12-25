package bunnyEmu.main.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import bunnyEmu.main.entities.Client;
import bunnyEmu.main.entities.Packet;
import bunnyEmu.main.utils.Log;

/**
 * 
 * @author Marijn
 */
public abstract class Connection extends Thread {

	protected Socket clientSocket;
	protected PrintWriter out;
	protected DataInputStream in;
	protected boolean connected = true;
	protected Client clientParent;

	public Connection(Socket clientSocket) {
		this.clientSocket = clientSocket;
		initialize();
	}

	private void initialize() {
		Log.log("Created Connection");
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new DataInputStream(clientSocket.getInputStream());

		} catch (IOException ex) {
			Log.log(Log.ERROR, "Couldn't create in and output streams");
		}
	}

	protected void close() {
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

	public boolean send(Packet p) {
		if (p.size < p.packet.capacity()) {
			Log.log(Log.ERROR, "packet not send: size " + p.size
					+ " <  capacity " + p.packet.capacity());
			return false;
		}
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

	public Client getClientParent() {
		return clientParent;
	}

	public void setClientParent(Client c) {
		clientParent = c;
	}

}
