    package bunnyEmu.main.logon;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import bunnyEmu.main.Server;
import bunnyEmu.main.entities.AuthServerPacket;
import bunnyEmu.main.entities.Client;
import bunnyEmu.main.entities.ClientPacket;
import bunnyEmu.main.entities.Realm;
import bunnyEmu.main.handlers.RealmHandler;
import bunnyEmu.main.net.LogonConnection;
import bunnyEmu.main.utils.BigNumber;
import bunnyEmu.main.utils.Constants;
import bunnyEmu.main.utils.Log;

    /**
     *
     * @author Marijn
     */
    public class LogonAuth extends Auth{

        // BigNumbers for the SRP6 implementation
        private static final BigNumber N = new BigNumber("894B645E89E1535BBDAD5B8B290650530801B18EBFBF5E8FAB3C82872A3E9BB7", 16); // Safe prime
        private static final BigNumber g = new BigNumber("7"); // Generator
        private static final BigNumber s = new BigNumber().setRand(32); // Salt
        private static final BigNumber k = new BigNumber("3"); // k - used to generate a lot..
        private static final BigNumber b = new BigNumber().setRand(19); // server private value
        
        private BigNumber v; // Verifier
        private BigNumber gmod; // gmod - used to calculate B
        private BigNumber B; // server public value
        
        
        private MessageDigest md;
        
        byte[] I = null; // Client name
        private LogonConnection connection;

        public LogonAuth(LogonConnection connection){
            this.connection = connection;
            try {
                md = MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException ex) {
                Log.log("Couldn't load algorithm");
            }
        }
        
        public void serverLogonChallenge(ClientPacket in) throws IOException {
            Log.log("serverLogonChallenge");
            
            
            byte[]  gamename = new byte[4];	// 'WoW'
            String version = "";
            short build;
            byte[]  platform = new byte[4];	// 'x86'
            byte[]  os = new byte[4];		  // 'Win'
            byte[]  country = new byte[4];	 // 'enUS'
            
            in.get(gamename);                        // gamename
            version += in.get();                	// version 1
            int midVal = in.get();   				 // version 2
            if(midVal >= 10)
            	midVal = 0;
            version += midVal;
            version += in.get();                  	// version 3
            build=  in.getShort();                    	 // build
            in.get(platform);                          	// platform
            in.get(os);                                // os
            in.get(country);                           // country
            in.getInt();                               // timezone_bias
            in.getInt();                               // ip
            byte I_len = in.get();                 // I_len
            Log.log(I_len);
            I = new byte[I_len];
            in.packet.get(I, 0, I_len);                       // I  
            
            Log.log("test: " + version);
            
            String P = "password".toUpperCase();
            
           //Generate account hash
            md.update(I);
            md.update((":").getBytes());
            md.update(P.getBytes());
            
            
            byte[] accountHash = md.digest();
            Log.log("AccountHash: " + new BigNumber(accountHash).toHexString());
            String username = new String(I);
            _client = new Client(username, P, Integer.parseInt(version));
            _client.attachLogon((LogonConnection) connection);

            // Kick the existing client out if it's logged in already
            Client existingClient = RealmHandler.findClient(username);
            if(existingClient != null)
            	existingClient.disconnect();

        	RealmHandler.addVersionRealm(_client.getVersion());
        	
            RealmHandler.addTempClient(_client);
            
            // Generate x - the Private key
            md.update(s.asByteArray(32));
            md.update(accountHash);
            BigNumber x = new BigNumber();
            x.setBinary(md.digest());

            // Generate B - the server public value
            v = g.modPow(x, N);
            gmod = g.modPow(b, N);
            B = (v.multiply(k).add(gmod)).remainder(N);
            
            AuthServerPacket serverLogonChallenge = new AuthServerPacket((short) 119);
            serverLogonChallenge.put((byte) 0); // opcode
            serverLogonChallenge.put((byte) 0); // unk
            serverLogonChallenge.put((byte) 0); // WoW_SUCCES
             serverLogonChallenge.put(B.asByteArray(32));
            serverLogonChallenge.put((byte) 1);
            serverLogonChallenge.put(g.asByteArray(1));
            serverLogonChallenge.put((byte) 32);
            serverLogonChallenge.put(N.asByteArray(32));
            serverLogonChallenge.put(s.asByteArray(32));
            serverLogonChallenge.put(new byte[16]);
            serverLogonChallenge.put((byte) 0); // unk

            connection.send(serverLogonChallenge);
            /*
            */
            Log.log("send challenge");
        }

        public void serverLogonProof(ClientPacket in) throws IOException {
            byte[] _A = new byte[32];
            byte[] _M1 = new byte[20];
            byte[] crc_hash = new byte[20];
            
            Log.log("serverLogonProof");

            in.get(_A);
            in.get(_M1);
            in.get(crc_hash);
            in.get();      // number_of_keys
            in.get();      // unk
            
            // Generate u - the so called "Random scrambling parameter"
            BigNumber A = new BigNumber();
            A.setBinary(_A);
            //A = new BigNumber("7290BF0303C3857F0B4E62EBA9602EDF6C49CFF9996F1AF3CA324AC4BE9A98A7");
            md.update(A.asByteArray(32));
            md.update(B.asByteArray(32));

            BigNumber u = new BigNumber();
            u.setBinary(md.digest());
           
            // Generate S - the Session key
            BigNumber S = (A.multiply(v.modPow(u, N))).modPow(b, N);
            
            // Generate vK - the hashed session key, hashed with H hash function
            byte[] t = S.asByteArray(32);
            byte[] t1 = new byte[16];
            byte[] vK = new byte[40];

            for (int i = 0; i < 16; i++) 
                t1[i] = t[i * 2];

            md.update(t1);

            byte[] digest = md.digest();
            for (int i = 0; i < 20; i++) 
                vK[i * 2] = digest[i];

            for (int i = 0; i < 16; i++) 
                t1[i] = t[i * 2 + 1];

            md.update(t1);
            digest = md.digest();
            for (int i = 0; i < 20; i++) 
                vK[i * 2 + 1] = digest[i];

            // generating M - the server's SRP6 M value

            md.update(N.asByteArray(32));
            byte[] hash = md.digest();

            md.update(g.asByteArray(1));
            digest = md.digest();
            for (int i = 0; i < 20; i++) 
                hash[i] ^= digest[i];

            md.update(I);
            byte[] t4 = new byte[20];
            t4 = md.digest();
            
            
            BigNumber K = new BigNumber();
            K.setBinary(vK);
            BigNumber t3 = new BigNumber();
            t3.setBinary(hash);
            BigNumber t4_correct = new BigNumber();
            t4_correct.setBinary(t4);
            
            md.update(t3.asByteArray());
            md.update(t4_correct.asByteArray());
            md.update(s.asByteArray());
            md.update(A.asByteArray());
            md.update(B.asByteArray());
            md.update(K.asByteArray()) ;

            byte[] m = md.digest();
            BigNumber M = new BigNumber(m);
            BigNumber M1 = new BigNumber(_M1);
            
          //  if(!M.equals(M1)) {
          //      return;
          //  }
            
            Log.log(Log.DEBUG, "M = " + M.toHexString());
            Log.log(Log.DEBUG, "M1 = " + M1.toHexString());

            _client.setSessionKey(K.asByteArray());
            
            md.update(A.asByteArray());
            md.update(_M1); 
            md.update(K.asByteArray());

            short size = 32;
            if(_client.getVersion() <= Constants.VERSION_VANILLA){
            	size = 26;
            	Log.log("siz is ");
            }
            
	        AuthServerPacket serverLogonAuth = new AuthServerPacket((short) size);
	        serverLogonAuth.put((byte) 1); // cmd
	        serverLogonAuth.put((byte) 0); // error
	        serverLogonAuth.put(md.digest());
	        // Acount flags
	        if(_client.getVersion() <= Constants.VERSION_VANILLA)
	        	serverLogonAuth.putInt(0);     
	        else{
	        	serverLogonAuth.putInt(0x00800000);
	  	        //  survey ID
	  	        serverLogonAuth.putInt(0); 
	  	        //  unk2-3
	  	        serverLogonAuth.putShort((short) 0);
	        }
	        //serverLogonAuth.wrap();
            connection.send(serverLogonAuth);
            	
           
        }
        
        
        public void serverRealmList() throws IOException {
        	Log.log("Sending realmlist");
            connection.send(RealmHandler.getRealmList()); 
        }
      
        
    }
