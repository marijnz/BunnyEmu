/*
 * BunnyEmu - A Java WoW sandbox/emulator
 * https://github.com/marijnz/BunnyEmu
 */
package bunnyEmu.main.net;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import bunnyEmu.main.entities.Realm;
import bunnyEmu.main.entities.packet.ClientPacket;
import bunnyEmu.main.entities.packet.IPacketReadable;
import bunnyEmu.main.entities.packet.IPacketWritable;
import bunnyEmu.main.entities.packet.Packet;
import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.enums.ClientVersion;
import bunnyEmu.main.logon.RealmAuth;
import bunnyEmu.main.net.packets.client.CMSG_AUTH_PROOF;
import bunnyEmu.main.net.packets.client.CMSG_CHAR_CREATE;
import bunnyEmu.main.net.packets.client.CMSG_MESSAGECHAT;
import bunnyEmu.main.net.packets.client.CMSG_MOVEMENT;
import bunnyEmu.main.net.packets.client.CMSG_PLAYER_LOGIN;
import bunnyEmu.main.utils.Opcodes;
import bunnyEmu.main.utils.PacketLog;
import bunnyEmu.main.utils.PacketLog.PacketType;
import misc.Logger;

/**
 * 
 * A connection between the client and the world, handles all incoming packets for all WoW versions (<= MoP)
 *
 * @author Marijn
 * 
 * 
 */
public class WorldConnection extends Connection{
    
    private RealmAuth auth;
    private WorldSession worldSession;
    private Realm realm;
    private Method packetWriter;
    private Method packetReader;
    
    public WorldConnection(Socket clientSocket, Realm realm){
        super(clientSocket);
        this.realm = realm;
        
        try {
        	packetWriter = IPacketWritable.class.getMethod("write" + realm.getVersionName());
        	packetReader = IPacketReadable.class.getMethod("read" + realm.getVersionName());
		} catch (Exception e) {;}
        
        auth = new RealmAuth(this, realm);
        if(realm.getVersion() != ClientVersion.VERSION_MOP)
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
                
                if(p.sOpcode == null){
                	Logger.writeLog("Received unknown packet: " + p.toString(), Logger.LOG_TYPE_WARNING);
                	PacketLog.logPacket(PacketType.CLIENT_UNKNOWN, p);
                	continue;
                }
                
                p.position(0);
                try {
                	// Trying to write implementation, if the class doesn't exist, nothing will happen
                	ClientPacket pTemp = (ClientPacket) Class.forName("bunnyEmu.main.net.packets.client." + p.sOpcode).newInstance();
                	pTemp.put(p);
                	p = pTemp;
					try {
	    				if(!((boolean) packetReader.invoke(p, new Object[0])))
	    					p.readGeneric();
	    			} catch (Exception e){
	    				e.printStackTrace();
	    			}
					Logger.writeLog("Received known packet with implementation: " + p.toString(), Logger.LOG_TYPE_WARNING);
					PacketLog.logPacket(PacketType.CLIENT_KNOWN_IMPLEMENTED, p);
				} catch (Exception e){
					Logger.writeLog("Received known packet without implementation: " + p.toString(), Logger.LOG_TYPE_WARNING);
					PacketLog.logPacket(PacketType.CLIENT_KNOWN_UNIMPLEMENTED, p);
				}
                
                switch(p.sOpcode) {
                	case Opcodes.MSG_TRANSFER_INITIATE:					auth.authChallenge(); 									break; // MoP only
                    case Opcodes.CMSG_AUTH_PROOF:  						auth.authSession((CMSG_AUTH_PROOF) p);					break;
                    case Opcodes.CMSG_READY_FOR_ACCOUNT_DATA_TIMES:		worldSession.sendAccountDataTimes(0x15);				break;
                    case Opcodes.CMSG_CHAR_ENUM:						worldSession.sendCharacters();							break;
                    case Opcodes.CMSG_RANDOM_NAME:						worldSession.sendRandomName();							break;
                    case Opcodes.CMSG_CHAR_CREATE:						worldSession.createCharacter((CMSG_CHAR_CREATE) p); 	break;
                    case Opcodes.CMSG_CHAR_DELETE:						worldSession.deleteCharacter(p); 						break;
                    case Opcodes.CMSG_PLAYER_LOGIN:						worldSession.verifyLogin((CMSG_PLAYER_LOGIN) p); 		break;
                    case Opcodes.CMSG_PING:								worldSession.sendPong(); 								break;
                    case Opcodes.CMSG_NAME_QUERY:						worldSession.sendNameResponse(); 						break;
                    case Opcodes.CMSG_NAME_CACHE:						worldSession.handleNameCache(p);						break; // MoP only
                    case Opcodes.CMSG_REALM_CACHE: 						worldSession.handleRealmCache(p);						break; // MoP only
                    case Opcodes.CMSG_MESSAGECHAT: 						worldSession.handleChatMessage((CMSG_MESSAGECHAT)p);	break;
                    case Opcodes.CMSG_MOVEMENT: 						worldSession.handleMovement((CMSG_MOVEMENT) p);			break;
                    case Opcodes.CMSG_DISCONNECT: 						client.disconnect(); 									break;
                }
            }

            System.out.println("World closed connection from " + clientSocket.toString());
        } catch (IOException e) {
        	Logger.writeLog(WorldConnection.class.getName() + " force closed", Logger.LOG_TYPE_WARNING);
        	e.printStackTrace();
        } finally {
        	// The client parent might be null if the realm authentication hasn't been completed yet
        	if(client != null)
        		client.disconnectFromRealm();
            close();
        }
    }
    
    private ClientPacket readPacket(byte firstByte){
        ClientPacket p = null;
        try {
            p = new ClientPacket();
            p.header[0] = firstByte;
            in.read(p.header, 1, ((realm.getVersion() != ClientVersion.VERSION_MOP) ? 5 : 3));
            decodeHeader(p);
            p.sOpcode = realm.getPackets().getOpcodeName(new Short(p.nOpcode));
            		
            if (p.size < 0){
            	Logger.writeLog(p.size + " is < 0, RETURNING " + p.headerAsHex(), Logger.LOG_TYPE_VERBOSE);
            	return null;
            } else if (p.size == 0){
            	p.packet = ByteBuffer.wrap(new byte[1]); // just put an empty byte in it to avoid null errors on logging
            } else{
	            byte[] b = new byte[p.size];
	            in.read(b);
	            p.packet = ByteBuffer.allocate(b.length);
	            p.packet.order(ByteOrder.LITTLE_ENDIAN); 
	            p.packet.put(b);
            }
        } catch (IOException e) {
            Logger.writeLog("Couldn't read client packet", Logger.LOG_TYPE_WARNING);
        }
        return p;
    }
   
    public boolean send(ServerPacket p){
    	try{
    		try {
				if(!((boolean) packetWriter.invoke(p, new Object[0])))
					p.writeGeneric();
			} catch (Exception e){
				e.printStackTrace();
				return false;
			}
    		p.nOpcode = realm.getPackets().getOpcodeValue(p.sOpcode);
    	} catch(NullPointerException e){
    		Logger.writeLog(p.sOpcode + " can't be send, it has no opcode linked", Logger.LOG_TYPE_ERROR);
    		Logger.writeLog(p.toString(), Logger.LOG_TYPE_ERROR);
    		return false;
    	}
    	p.setHeader(encode(p.size, p.nOpcode));

    	Logger.writeLog("Sending packet: " + p.sOpcode + "  0x" + Integer.toHexString(p.nOpcode).toUpperCase() + "(" + p.size + ") " + p.packetAsHex(), Logger.LOG_TYPE_VERBOSE);

    	p.position(0);
        return super.sendPacket(p);
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
	
		header[index++] = (byte)(0xFF & (newSize >> ((realm.getVersion() != ClientVersion.VERSION_MOP) ? 8 : 0)));
		header[index++] = (byte)(0xFF & (newSize >> ((realm.getVersion() != ClientVersion.VERSION_MOP) ? 0 : 8)));
		header[index++] = (byte)(0xFF & opcode);
		header[index] = (byte)(0xFF & (opcode >> 8));
		
        if (client != null){
        	if(realm.getVersion() == ClientVersion.VERSION_MOP){
        		int totalLength = newSize-2;
                totalLength <<= 13;
                totalLength |= (opcode & 0x1FFF);
                ByteBuffer buffer = ByteBuffer.allocate(4);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.putInt(totalLength);
                header = buffer.array();
        	}
        	header = client.getCrypt().encrypt(header);
        }
        
        return header;
    }
    
    
    /**
     * Decodes and also decrypts if the crypt has been initialized
     */
    private void decodeHeader(Packet p){
    	// Client parent isn't null so client is authenticated
    	if (client != null)
    		p.header = client.getCrypt().decrypt(p.header);
    	
    	if(realm.getVersion() != ClientVersion.VERSION_MOP){
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
    		if(client != null){
    			ByteBuffer toHeader = ByteBuffer.allocate(4);
            	toHeader.order(ByteOrder.LITTLE_ENDIAN);
            	toHeader.put(p.header,0, 4);
            	toHeader.position(0);
            	int header = toHeader.getInt();
        		int size = ((header >> 13));
        		int opcode = (header & 0x1FFF);
	        	p.header[0] = (byte)(0xFF & size);
	        	p.header[1] = (byte)(0xFF & (size >> 8));
	        	p.header[2] = (byte)(0xFF & opcode);
	        	p.header[3] = (byte)(0xFF & (opcode >> 8));
    		}
    		
    		ByteBuffer toHeader = ByteBuffer.allocate((realm.getVersion() != ClientVersion.VERSION_MOP) ? 6 : 4);
        	toHeader.order(ByteOrder.LITTLE_ENDIAN);
        	toHeader.put(p.header, 0, (realm.getVersion() != ClientVersion.VERSION_MOP) ? 6 : 4);
        	toHeader.position(0);

	        p.size = toHeader.getShort();
        	p.nOpcode = toHeader.getShort();
        	p.header = toHeader.array();
    	}
    
    }
}
