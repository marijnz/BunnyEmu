/*
 * BunnyEmu - A Java WoW sandbox/emulator
 * https://github.com/marijnz/BunnyEmu
 */
package bunnyEmu.main.logon;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import bunnyEmu.main.entities.ClientPacket;
import bunnyEmu.main.entities.Realm;
import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.handlers.RealmHandler;
import bunnyEmu.main.net.WorldConnection;
import bunnyEmu.main.utils.BigNumber;
import bunnyEmu.main.utils.Constants;
import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.Opcodes;

/**
 *
 * Authenticates a client to the assigned realm
 *
 * @author Marijn
 */
public class RealmAuth extends Auth {

    private byte[] _seed;
    private WorldConnection connection;
    private Realm realm;

    public RealmAuth(WorldConnection c, Realm r) {
        connection = c;
        realm = r;
    }
    
    /**
     * MoP only, send on start
     */
    public void transferInitiate(){
    	 ServerPacket initiate = new ServerPacket(Opcodes.MSG_TRANSFER_INITIATE, 50);
    	 initiate.putString("RLD OF WARCRAFT CONNECTION - SERVER TO CLIENT");
    	 initiate.wrap();
    	 connection.send(initiate);
    }

    public void authChallenge() {
    	
        ServerPacket authChallenge = new ServerPacket(Opcodes.SMSG_AUTH_CHALLENGE, 50);
        _seed = new SecureRandom().generateSeed(4);
        
        if(realm.getVersion() < Constants.VERSION_MOP){
	        if(realm.getVersion() >= Constants.VERSION_CATA){
	        	authChallenge.put(new BigNumber().setRand(16).asByteArray(16));
	        	authChallenge.put((byte) 1);
	        }  else if(realm.getVersion() > Constants.VERSION_BC && realm.getVersion() < Constants.VERSION_CATA)
	        	authChallenge.putInt(1);
	        authChallenge.put(_seed);
	        authChallenge.put(new BigNumber().setRand(16).asByteArray(16));
        } else{
        	authChallenge.putShort((short) 0);
        	authChallenge.put(_seed);
        	 for (int i = 0; i < 8; i++)
                 authChallenge.putInt(0);
        	 authChallenge.put((byte) 1);
        }
	        
	    authChallenge.wrap();
        connection.send(authChallenge);
    }
    
    public void authSession(ClientPacket authSession) {
        Log.log("authSession");
        String accountName = null;
        byte[] mClientSeed = new byte[4];
        byte[] mClientBuild = new byte[2];
        byte[] digest1 = new byte[20];
        
        if(realm.getVersion() <= Constants.VERSION_WOTLK){
        	
        	authSession.getInt();// mClientBuild
   	        authSession.getInt();               // unk2
   	        accountName = authSession.getString();     // accountName
   	        if(realm.getVersion() > Constants.VERSION_BC)
   	        	authSession.getInt();               // unk3
   	        
   	        authSession.get(mClientSeed);       // mClientSeed
   	        if(realm.getVersion() > Constants.VERSION_BC){
   		        authSession.getLong();
   		        authSession.getInt();
   		        authSession.getInt();
   		        authSession.getInt();
   	        }
   	        authSession.get(digest1);
        } else if(realm.getVersion() <= Constants.VERSION_CATA){
        	int position = 0;
        	authSession.get(digest1, position, 7);
        	authSession.get(new byte[4]);
        	authSession.get(digest1, position += 7, 1);
        	authSession.get(new byte[12]);
        	authSession.get(digest1, position += 1, 1);
        	authSession.get(new byte[1]);
        	authSession.get(digest1, position += 1, 2);
        	authSession.get(mClientSeed);

        	authSession.get(new byte[4]);
        	authSession.get(digest1, position += 2, 6);
        	authSession.get(mClientBuild);// mClientBuild
        	Log.log(new BigNumber(mClientBuild).toHexString());
        	authSession.get(digest1, position += 6, 1);
        	authSession.get(new byte[5]);
        	
        	authSession.get(digest1, position += 1, 2);
        	
        	int firstByte = (0x000000FF & ((int)authSession.get()));
        	short addonSize = (short)firstByte;
        	
        	authSession.get(new byte[addonSize+3]); // adjusting to addonsize (1+3 = int)
        	accountName = authSession.getString();
        } else{
        	authSession.get(new byte[54]);
        	int addonSize = authSession.getInt();
        	authSession.get(new byte[addonSize + 2]); // +2 = 0-byte and "X"?
        	accountName = authSession.getString();
        }
        
        Log.log(accountName);
        _client = RealmHandler.getTempClient(accountName);
        _client.connect(realm);
        
        if (_client != null) {
            _client.attachWorld((WorldConnection) connection);
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException ex) {
                Log.log("Couldn't load algorithm");
            }

            byte[] t = {0, 0, 0, 0};
            md.update(connection.getClientParent().getName().getBytes());
            md.update(t);
            md.update(mClientSeed);
            md.update(_seed);
            md.update(connection.getClientParent().getSessionKey());
            byte[] digest2 = md.digest();	
            
            Log.log("authSession " + _client.getName() + " " + new BigNumber(digest1).toHexString() + "  " + new BigNumber(digest2).toHexString());
            
            // The cataclysm and MoP digest calculation is unknown, simply allowing it..
            if (!(realm.getVersion() < Constants.VERSION_CATA) || new BigNumber(digest1).toHexString().equals(new BigNumber(digest2).toHexString())) {
            	connection.getClientParent().initCrypt(connection.getClientParent().getSessionKey()); 
            	Log.log("Valid account: " + _client.getName());
                if(realm.getVersion() <= Constants.VERSION_CATA){
                	ServerPacket authResponse = new ServerPacket(Opcodes.SMSG_AUTH_RESPONSE, 80);
	                authResponse.put((byte) 0x0C);
	                authResponse.put((byte) 0x30);
	                authResponse.put((byte) 0x78);
	                authResponse.putLong(0x02);
	                connection.send(authResponse);
                } else{
                	// MoP sends all classes and races + some other data, packet from Arctium
                    connection.send(realm.loadPacket("authresponse_mop", 78));
                    connection.send(new ServerPacket(Opcodes.SMSG_UPDATE_CLIENT_CACHE_VERSION, 4));
                    connection.send(new ServerPacket(Opcodes.SMSG_TUTORIAL_FLAGS, 8*4));
                    
                }
            } else
            	_client.disconnectFromRealm();
        } else{
	        Log.log("Unvalid account");
        }
    }
}
