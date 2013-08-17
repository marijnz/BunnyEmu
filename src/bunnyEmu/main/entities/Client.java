/*
 * BunnyEmu - A Java WoW sandbox/emulator
 * https://github.com/marijnz/BunnyEmu
 */
package bunnyEmu.main.entities;

import java.io.IOException;
import java.util.ArrayList;

import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.handlers.ClientHandler;
import bunnyEmu.main.net.LogonConnection;
import bunnyEmu.main.net.WorldConnection;
import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.Versions;
import bunnyEmu.main.utils.crypto.BCCrypt;
import bunnyEmu.main.utils.crypto.GenericCrypt;
import bunnyEmu.main.utils.crypto.MoPCrypt;
import bunnyEmu.main.utils.crypto.VanillaCrypt;
import bunnyEmu.main.utils.crypto.WotLKCrypt;

/**
 * A client that is made upon logging in and will be destroyed on disconnecting.
 * 
 *
 * @author Marijn
 */
public class Client {
    
    private String name;
    private int version;
    private byte[] sessionKey;
    private GenericCrypt crypt;
    
    private LogonConnection _logonConnection;
    private WorldConnection _worldConnection;
    private Realm realm;
    
    private ArrayList<Char> characters = new ArrayList<Char>();
    private Char currentCharacter;
    
    /**
     * Creates a new Client, the version given will 
     * @param name The name of the client (username)
     * @param version The patch/version of the client i.e.: 335. 
     */
    public Client(String name, int version){
        this.name = name.toUpperCase();
        this.version = version;
        
        if(version <= Versions.VERSION_VANILLA)
        	crypt = new VanillaCrypt();
        else if(version <= Versions.VERSION_BC)
        	crypt = new BCCrypt();
        else if(version <= Versions.VERSION_CATA)
        	crypt = new WotLKCrypt();
        else if(version <= Versions.VERSION_MOP)
        	crypt = new MoPCrypt();
        
        // Char char1 = new Char("Test", -5626, -1496, 100, 1, (byte) 2,(byte) 1);
       // Char char2 = new Char("Test", 2, 3, 4, 1, (byte) 1,(byte) 7);
	   	//addCharacter(char2);
	   //	addCharacter(char2);
    }

    /**
     * @param K The session key generated in LogonAuth
     */
    public void setSessionKey(byte[] K){
        sessionKey = K;
    }
    
    /**
     * @return K The session key generated in LogonAuth
     */
    public byte[] getSessionKey(){
        return sessionKey;
    }
    
    public GenericCrypt getCrypt(){
        return crypt;
    }
    
    /**
     * Initializes the version dependent crypt with the generated session key.
     * 
     * @return K The session key generated in LogonAuth
     */
    public void initCrypt(byte[] K){
        crypt.init(K);
    }
    
    /**
     * @return The username of this client.
     */
    public String getName(){
        return name;
    }
    
    /**
     * @return The worldsocket connection attached to this client.
     */
    public WorldConnection getWorldConnection(){
        return _worldConnection;
    }
    
    /**
     * @param c The logonsocket connection that belongs to this client.
     */
    public void attachLogon(LogonConnection c){
        _logonConnection = c;
        c.setClientParent(this);
    }
    
    /**
     * @param c The worldsocket connection that belongs to this client.
     */
    public void attachWorld(WorldConnection c){
        _worldConnection = c;
        c.setClientParent(this);
    }
    
    /**
     * Connects the client to the assigned Realm
     * @param realm The realm the client selected in the realmlist.
     */
    public void connect(Realm realm){
    	this.realm = realm;
    	realm.addClient(this);
    }
    
    /**
     * Disconnects the client,
     * - Removed from realm (if connected to realm)
     * - Closed logon connection
     * - Closed world connection
     */
    public void disconnect(){
    	if(realm != null)
    		realm.removeClient(this);
    	else
    		ClientHandler.removeTempClient(name);
    	try {
			_logonConnection.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	// World connection might not exist yet if the client is disconnected as a result of an incorrect password
    	if(_worldConnection != null){
	    	try{
	    		_worldConnection.getSocket().close();
	    	} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	
    	Log.log(this.name + " disconnected!");
    }
    
    public void disconnectFromRealm(){
    	realm.removeClient(this);
    	realm = null;
    	ClientHandler.addTempClient(this);
    }
    
    /**
     * Only use for iterating (character list), adding characters should be done with addCharacter.
     */
    public  ArrayList<Char> getCharacters(){
    	return characters;
    }
    
    public int addCharacter(Char c){
    	Log.log("adding char with GUID " + c.getGUID());
    	if(characters.size() >= 10)
    		return -1;
    	characters.add(c);
    	return characters.size();
    }
    
    public void setCurrentCharacter(Char currentCharacter){
    	this.currentCharacter = currentCharacter;
    }
    
    /**
     * @return The character that belongs to the guid, null if doesn't exist.
     */
    public Char setCurrentCharacter(long GUID){
    	Log.log("setting cur char with GUID " + GUID);
    	for(Char cChar : characters)
    		if(cChar.getGUID() == GUID){
    			currentCharacter = cChar;
    			return cChar;
    		}
    	return null;
    }
    
    /**
     * @return The Character this client is currently playing with (possibly null if the player didn't get past the character selection screen yet).
     */
    
    public Char getCurrentCharacter() {
    	return currentCharacter;
    }
    
	/**
	 * @return The version this Client logged in with, could be different than the actual supported versions.
	 */
	public int getVersion() {
		return version;
	}
}
