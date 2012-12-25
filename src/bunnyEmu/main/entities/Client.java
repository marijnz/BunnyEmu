/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bunnyEmu.main.entities;

import java.io.IOException;
import java.util.ArrayList;

import bunnyEmu.main.handlers.RealmHandler;
import bunnyEmu.main.net.LogonConnection;
import bunnyEmu.main.net.WorldConnection;
import bunnyEmu.main.utils.Log;
import bunnyEmu.main.utils.crypto.Crypt;

/**
 *
 * @author Marijn
 */
public class Client {
    
    private String _name;
    private String _password;
    private int _version;
    private byte[] _sessionKey;
    private Crypt _crypt;
    
    private LogonConnection _logonConnection;
    private WorldConnection _worldConnection;
    private Realm _connectedRealm;
    
    private ArrayList<Char> characters = new ArrayList<Char>();
    private Char currentCharacter;
    
    public Client(String name, String password, int version){
        _name = name.toUpperCase();
        _password = password;
        _version = version;
        _crypt = new Crypt();
        Char char1 = new Char("Char1", -5626, -1496, 100, 1, (byte) 2,(byte) 1);
	   	addCharacter(char1);
    }
    
    public void setSessionKey(byte[] K){
        _sessionKey = K;
    }
    
    public byte[] getSessionKey(){
        return _sessionKey;
    }
    
    public Crypt getCrypt(){
        return _crypt;
    }
    
    public void initCrypt(byte[] K){
        _crypt.init(K);
    }
    
    public String getName(){
        return _name;
    }
    
    public WorldConnection getWorldConnection(){
        return _worldConnection;
    }
    
    
    public void attachLogon(LogonConnection c){
        _logonConnection = c;
        c.setClientParent(this);
    }
    
    public void attachWorld(WorldConnection c){
        _worldConnection = c;
        c.setClientParent(this);
    }
    
    public void connect(Realm realm){
    	_connectedRealm = realm;
    	_connectedRealm.addClient(this);
    }
    
    public void disconnect(){
    	if(_connectedRealm != null)
    		_connectedRealm.removeClient(this);
    	RealmHandler.getTempClient(_name);
    	try {
			_logonConnection.getSocket().close();
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
     * Only use for iterating (character list), adding characters should be done with addCharacter
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
     * @return The character that belongs to the guid, null if doesn't exist
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
    
    public Char getCurrentCharacter(){
    	return currentCharacter;
    }

	public int getVersion() {
		return _version;
	}
}
