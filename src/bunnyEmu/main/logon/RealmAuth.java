/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

/**
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
        authChallenge();
    }

    private void authChallenge() {
        ServerPacket authChallenge = new ServerPacket(0x01EC, 8);
        if(realm.getVersion() > Constants.VERSION_BC)
        	authChallenge.putInt(1);
        _seed = new SecureRandom().generateSeed(4);
        authChallenge.put(_seed);
        authChallenge.wrap();
        connection.send(authChallenge);
    }

    public void authSession(ClientPacket authSession) {
        Log.log("authSession");
        String accountName;
        byte[] mClientSeed = new byte[4];
        byte[] digest1 = new byte[20];
        
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
            if (new BigNumber(digest1).toHexString().equals(new BigNumber(digest2).toHexString())) {
            	connection.getClientParent().initCrypt(connection.getClientParent().getSessionKey()); 
            	
                Log.log("Valid account: " + _client.getName());
               
                //ServerPacket authResponse = new ServerPacket(0x1EE, 1);
                //authResponse.put((byte) 0x14);
                
                ServerPacket authResponse = new ServerPacket(0x1EE, 11);
                authResponse.put((byte) 0x0C);
                authResponse.put((byte) 0x30);
                authResponse.put((byte) 0x78);
                authResponse.putLong(0x02);
               
                connection.send(authResponse);
            }
        } else{
	        Log.log("Unvalid account");
        }
    }
}
