/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bunnyEmu.main.net;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import bunnyEmu.main.entities.ClientPacket;
import bunnyEmu.main.entities.Packet;
import bunnyEmu.main.logon.LogonAuth;
import bunnyEmu.main.utils.Log;

/**
 * @author Marijn
 * TODO: Clean constants for the packets
 */
public class LogonConnection extends Connection {

    LogonAuth auth;
    
    private static final byte CLIENT_LOGON_CHALLENGE 	= 0x00;
    private static final byte CLIENT_LOGON_PROOF 		= 0x01;
    private static final byte CLIENT_REALMLIST 			= 0x010;

    public LogonConnection(Socket clientSocket) {
        super(clientSocket);
        auth = new LogonAuth(this); // The LogonAuth reads the stream instead of packets
        start();
    }

    @Override
    public void run() {
        try {
        	byte readByte;
        	ClientPacket p = null;
        	while((readByte = (byte) in.read()) != -1){
                if ((p = readPacket(readByte)) == null) 
                    continue;
                Log.log("got auth packet: " + p.toString());
                
                switch(p.opcode){
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
        	Log.log(LogonConnection.class.getName() + " force closed");
        	e.printStackTrace();
        } finally {
        	//clientParent.disconnect();
            close();
        }
    }

	private ClientPacket readPacket(byte firstByte){
		ClientPacket p = new ClientPacket();
         p.opcode = firstByte;
         p.packet = ByteBuffer.allocate(200);
         try {
	         if(p.opcode == CLIENT_LOGON_CHALLENGE){
	        	 in.readByte();
	        	 p.size = in.readByte();
	        	 in.readByte();
	         } else if (p.opcode == CLIENT_LOGON_PROOF){
	        	 p.size = 1 + 32 + 20 + 20 + 1 + 1;
	         } else if(p.opcode == CLIENT_REALMLIST)
	        	 p.size = 4;
	         
	         byte[] b = new byte[p.size];
	         in.read(b);
	         p.packet = ByteBuffer.wrap(b);
	            
		} catch (IOException e) {
			e.printStackTrace();
		}
        return p;
	}
	
	 
    @Override 
    public boolean send(Packet p){
    	Log.log("Sending packet: (" + p.size + ") " + p.packetAsHex());
        return super.send(p);
    }
		
}
