/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bunnyEmu.main.net;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import bunnyEmu.main.entities.ClientPacket;
import bunnyEmu.main.entities.Packet;
import bunnyEmu.main.entities.Realm;
import bunnyEmu.main.logon.RealmAuth;
import bunnyEmu.main.utils.BigNumber;
import bunnyEmu.main.utils.Constants;
import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.Opcodes;

/**
 *
 * @author Marijn
 */
public class WorldConnection extends Connection{
    
    private RealmAuth auth;
    private WorldSession worldSession;
    private Realm realm;
    public WorldConnection(Socket clientSocket, Realm realm){
        super(clientSocket);
        this.realm = realm;
        auth = new RealmAuth(this, realm);
        if(realm.getVersion() < Constants.VERSION_MOP)
        	auth.authChallenge();
        else
        	auth.transferInitiate();
        worldSession = new WorldSession(this, realm);
        start();
    }
    
    public void run(){
        try{
            ClientPacket p = null;
            byte readByte;
            while((readByte = (byte) in.read()) != -1){
                p = readPacket(readByte);
                if(p == null)
                    continue;
                Log.log("got packet: " + p.toString());
                if( p.sOpcode == null)
                	continue;
                p.position(0);
                switch(p.sOpcode){
                	case Opcodes.MSG_TRANSFER_INITIATE:					auth.authChallenge(); 						break; // MoP only
                    case Opcodes.CMSG_AUTH_PROOF:  						auth.authSession(p);						break;
                    													//worldSession.sendAccountDataTimes(0x15);	
                    case Opcodes.CMSG_READY_FOR_ACCOUNT_DATA_TIMES:		worldSession.sendAccountDataTimes(0x15);	break;
                    case Opcodes.CMSG_CHAR_ENUM:						worldSession.sendCharacters();				break;
                    case Opcodes.CMSG_CHAR_CREATE:						worldSession.addCharacter(p); 				break;
                    case Opcodes.CMSG_PLAYER_LOGIN:						worldSession.verifyLogin(p); 				break;
                    case Opcodes.CMSG_PING:								worldSession.sendPong(); 					break;
                    case Opcodes.CMSG_NAME_QUERY:						worldSession.sendNameResponse(); 			break;
                    case Opcodes.CMSG_REALM_SPLIT:																	break;
                    default: Log.log("unknown: "  + p.toString());
                }
            }
            Log.log("World closed connection from " + clientSocket.toString());
        } catch (IOException ex) {
        	Log.log(WorldConnection.class.getName() + " force closed");
        } finally{
        	// The client parent might be null if the realm authentication went wrong
        	if(clientParent != null)
        		clientParent.disconnectFromRealm();
            close();
        }
    }
    
    private ClientPacket readPacket(byte firstByte){
        ClientPacket p = null;
        try {
            p = new ClientPacket();
            p.header[0] = firstByte;
            in.read(p.header, 1, ((realm.getVersion() <= Constants.VERSION_CATA) ? 5 : 3));
            Log.log((realm.getVersion() <= Constants.VERSION_CATA) ? 5 : 3);
            decodeHeader(p);
            p.sOpcode = realm.getPackets().getOpcodeName(new Short(p.nOpcode));
            
            if(p.sOpcode == null)
            	Log.log("Unknown packet: " + Integer.toHexString(p.nOpcode).toUpperCase());
            		
            if (p.size < 0){
            	Log.log(Log.ERROR, p.size + " is < 0, RETURNING");
            	return null;
            } else if (p.size == 0){
            	p.packet = ByteBuffer.wrap(new byte[1]); // just put a empty byte in it..
            } else{
	            byte[] b = new byte[p.size];
	            in.read(b);
	            p.packet = ByteBuffer.wrap(b);
	            p.packet.order(ByteOrder.LITTLE_ENDIAN);
            }
        } catch (IOException e) {
            Log.log(Log.DEBUG, "Couldn't read client packet");
        }
        return p;
    }
   
    
    @Override 
    public boolean send(Packet p){
    	try{
    		p.nOpcode = realm.getPackets().getOpcodeValue(p.sOpcode);
    	} catch(NullPointerException e){
    		Log.log(p.sOpcode + " can't be send, it has no opcode linked");
    		e.printStackTrace();
    		return false;
    	}
    	p.setHeader(encode(p.size, p.nOpcode));

    	Log.log("Sending packet: 0x" + Integer.toHexString(p.nOpcode).toUpperCase() + "(" + p.size + ") " + p.packetAsHex());

    	p.position(0);
        return super.send(p);
    }
    
    /**
     * Encodes and also encrypts if the crypt has been initialized
     */
    private byte[] encode(int size, int opcode){
        int index = 0;
        int newSize = size+2;
        byte[] header = new byte[4];
        if (newSize > 0x7FFF) 
            header[index++] = (byte) (0x80 | (0xFF & (newSize >> 16)));
	
		header[index++] = (byte)(0xFF & (newSize >> ((realm.getVersion() <= Constants.VERSION_CATA) ? 8 : 0)));
		header[index++] = (byte)(0xFF & (newSize >> ((realm.getVersion() <= Constants.VERSION_CATA) ? 0 : 8)));
		header[index++] = (byte)(0xFF & opcode);
		header[index] = (byte)(0xFF & (opcode >> 8));
		
        if (clientParent != null){
        	//Log.log("First opcode1: " + opcode +  "  - " + new BigNumber(header).toHexString());
        	if(realm.getVersion() >= Constants.VERSION_MOP){
        		int totalLength = newSize-2;
                totalLength <<= 12;
                totalLength |= (opcode & 0xFFF);
                ByteBuffer buffer = ByteBuffer.allocate(4);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.putInt(totalLength);
                header = buffer.array();
        	}
        	header = clientParent.getCrypt().encrypt(header);
        }
        
        return header;
    }
    
    
    /**
     * Decodes and also decrypts if the crypt has been initialized
     */
    private void decodeHeader(Packet p){
    	if (clientParent != null)
    		p.header = clientParent.getCrypt().decrypt(p.header);
        
    	if(realm.getVersion() < Constants.VERSION_MOP){
    		ByteBuffer toHeader = ByteBuffer.allocate(6);
        	toHeader.order(ByteOrder.LITTLE_ENDIAN);
        	toHeader.put(p.header);
        	toHeader.position(0);
			p.size = (short) (toHeader.get() << 8);
			p.size |= toHeader.get() & 0xFF;
			p.size -= 4;
			p.nOpcode = (short) toHeader.getInt();
	        p.header = toHeader.array();
    	} else{
    		if(clientParent != null){
    			ByteBuffer toHeader = ByteBuffer.allocate(4);
            	toHeader.order(ByteOrder.LITTLE_ENDIAN);
            	toHeader.put(p.header,0, 4);
            	toHeader.position(0);
            	int header = toHeader.getShort();
        		short size = (short)((header >> 12));
	        	short opcode = (short)(header & 0xFFF);
	        	p.header[0] = (byte)(0xFF & size);
	        	p.header[1] = (byte)(0xFF & (size >> 8));
	        	p.header[2] = (byte)(0xFF & opcode);
	        	p.header[3] = (byte)(0xFF & (opcode >> 8));
    		}
    		
    		ByteBuffer toHeader = ByteBuffer.allocate((realm.getVersion() <= Constants.VERSION_CATA) ? 6 : 4);
        	toHeader.order(ByteOrder.LITTLE_ENDIAN);
        	toHeader.put(p.header, 0, (realm.getVersion() <= Constants.VERSION_CATA) ? 6 : 4);
        	toHeader.position(0);

	        p.size = toHeader.getShort();
        	p.nOpcode = toHeader.getShort();
    	}
    
    }
}
