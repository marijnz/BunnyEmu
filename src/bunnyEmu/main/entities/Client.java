/*
 * BunnyEmu - A Java WoW sandbox/emulator
 * https://github.com/marijnz/BunnyEmu
 */
package bunnyEmu.main.entities;

import java.io.IOException;
import java.util.ArrayList;

import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.handlers.RealmHandler;
import bunnyEmu.main.net.LogonConnection;
import bunnyEmu.main.net.WorldConnection;
import bunnyEmu.main.utils.Constants;
import bunnyEmu.main.utils.Log;
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
    
    private String _name;
    private int _version;
    private byte[] _sessionKey;
    private GenericCrypt _crypt;
    
    private LogonConnection _logonConnection;
    private WorldConnection _worldConnection;
    private Realm _connectedRealm;
    
    private ArrayList<Char> characters = new ArrayList<Char>();
    private Char currentCharacter;
    
    /**
     * Creates a new Client, the version given will 
     * @param name The name of the client (username)
     * @param version The patch/version of the client i.e.: 335. 
     */
    public Client(String name, int version){
        _name = name.toUpperCase();
        _version = version;
        
        if(version <= Constants.VERSION_VANILLA)
        	_crypt = new VanillaCrypt();
        else if(version <= Constants.VERSION_BC)
        	_crypt = new BCCrypt();
        else if(version <= Constants.VERSION_CATA)
        	_crypt = new WotLKCrypt();
        else if(version <= Constants.VERSION_MOP)
        	_crypt = new MoPCrypt();
        
        // Char char1 = new Char("Test", -5626, -1496, 100, 1, (byte) 2,(byte) 1);
        Char char2 = new Char("Test", 2, 3, 4, 1, (byte) 8,(byte) 7);
	   	addCharacter(char2);
	   	//addCharacter(char2);
    }

    /**
     * @param K The session key generated in LogonAuth
     */
    public void setSessionKey(byte[] K){
        _sessionKey = K;
    }
    
    /**
     * @return K The session key generated in LogonAuth
     */
    public byte[] getSessionKey(){
        return _sessionKey;
    }
    
    public GenericCrypt getCrypt(){
        return _crypt;
    }
    
    /**
     * Initializes the version dependent crypt with the generated session key.
     * 
     * @return K The session key generated in LogonAuth
     */
    public void initCrypt(byte[] K){
        _crypt.init(K);
    }
    
    /**
     * @return The username of this client.
     */
    public String getName(){
        return _name;
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
    	_connectedRealm = realm;
    	_connectedRealm.addClient(this);
    }
    
    /**
     * Disconnects the client,
     * - Removed from realm (if connected to realm)
     * - Closed logon connection
     * - Closed world connection
     */
    public void disconnect(){
    	if(_connectedRealm != null)
    		_connectedRealm.removeClient(this);
    	RealmHandler.getTempClient(_name);
    	try {
			_logonConnection.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	try{
    		_worldConnection.getSocket().close();
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	Log.log(this._name + " disconnected!");
    }
    
    public void disconnectFromRealm(){
    	_connectedRealm.removeClient(this);
    	_connectedRealm = null;
    	RealmHandler.addTempClient(this);
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
    
    public Char getCurrentCharacter(){
    	return currentCharacter;
    }
    
	/**
	 * @return The version this Client logged in with, could be different than the actual supported versions.
	 */
	public int getVersion() {
		return _version;
	}
}
