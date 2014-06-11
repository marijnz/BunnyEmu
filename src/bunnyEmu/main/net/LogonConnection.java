/*
 * BunnyEmu - A Java WoW sandbox/emulator
 * https://github.com/marijnz/BunnyEmu
 */
package bunnyEmu.main.net;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import bunnyEmu.main.entities.packet.ClientPacket;
import bunnyEmu.main.entities.packet.Packet;
import bunnyEmu.main.logon.LogonAuth;
import bunnyEmu.main.utils.Logger;
import bunnyEmu.main.utils.PacketLog;
import bunnyEmu.main.utils.PacketLog.PacketType;

/**
 * Establish the first connection between server and client, handles client packets for all WoW versions.
 * 
 * @author Marijn
 * 
 */
public class LogonConnection extends Connection {

    LogonAuth auth;
    
    private static final byte CLIENT_LOGON_CHALLENGE 	= 0x00;
    private static final byte CLIENT_LOGON_PROOF 		= 0x01;
    private static final byte CLIENT_REALMLIST 			= 0x010;

    public LogonConnection(Socket clientSocket) {
        super(clientSocket);
        auth = new LogonAuth(this); // The LogonAuth reads the stream, not packets like in WorldConnection/RealmAuth
        start();
    }

    @Override
    public void run() {
        try {
        	byte readByte;
        	ClientPacket p = null;

        	while((readByte = (byte) in.read()) != -1) {
                if ((p = readPacket(readByte)) == null) 
                    continue;

                Logger.writeError("Got auth packet: " + p.toString());
                PacketLog.logPacket(PacketType.CLIENT_KNOWN_IMPLEMENTED, p);
                
                switch (p.nOpcode) {
                	case CLIENT_LOGON_CHALLENGE:
                		auth.serverLogonChallenge(p);  	// Responding to the client with some coowl data.
                        break; 
                	case CLIENT_LOGON_PROOF:
                		auth.serverLogonProof(p); 		// The client proving that the password entered is correct.
                        break; 
                    case CLIENT_REALMLIST:
                    	auth.serverRealmList(); 		// Sending the realm(s)
                        break; 
                 }
        	}
        } catch (Exception e) {
        	Logger.writeError(LogonConnection.class.getName() + " force closed");
        	e.printStackTrace();
        } finally {
            close();
        }
    }

	private ClientPacket readPacket(byte firstByte){
		ClientPacket p = new ClientPacket();
         p.nOpcode = firstByte;
         p.packet = ByteBuffer.allocate(200);
         try {
	         if(p.nOpcode == CLIENT_LOGON_CHALLENGE){
	        	 in.readByte();
	        	 p.size = in.readByte();
	        	 in.readByte();
	         } else if (p.nOpcode == CLIENT_LOGON_PROOF){
	        	 p.size = 1 + 32 + 20 + 20 + 1 + 1;
	         } else if(p.nOpcode == CLIENT_REALMLIST)
	        	 p.size = 4;
	         
	         byte[] b = new byte[p.size];
	         in.read(b);
	         p.packet = ByteBuffer.wrap(b);
	            
		} catch (IOException e) {
			e.printStackTrace();
		}
        return p;
	}
	
    public boolean send(Packet p){
    	Logger.writeError("Sending packet: (" + p.size + ") " + p.packetAsHex());
        return super.sendPacket(p);
    }
		
}
