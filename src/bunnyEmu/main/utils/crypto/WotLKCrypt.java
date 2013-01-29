/*******************************************************************************
 * Copyright (C) 2012 JMaNGOS <http://jmangos.org/>
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package bunnyEmu.main.utils.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import bunnyEmu.main.utils.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class Crypt.
 */
public class WotLKCrypt implements GenericCrypt {
	
	/** The Server decryption key. */
	protected byte[] ServerDecryptionKey;
	
	/** The Server encryption key. */
	protected byte[] ServerEncryptionKey;
	/** The is enabled. */
	protected boolean isEnabled = false;

	/** The _client decrypt. */
	protected SARC4 _clientDecrypt = new SARC4(); 
	
	/** The _server encrypt. */
	protected SARC4 _serverEncrypt = new SARC4(); 

	/**
	 * Instantiates a new crypt.
	 */
	public WotLKCrypt() { 
		Log.log("Created new WotLK crypt");
		ServerEncryptionKey = new byte[]{ (byte) 0xCC,  (byte)0x98,  (byte)0xAE, 0x04, (byte)0xE8,  (byte)0x97, (byte)0xEA,  (byte)0xCA, 0x12, 
				(byte)0xDD,  (byte)0xC0,  (byte)0x93, 0x42,  (byte)0x91, 0x53, 0x57 };
		
		ServerDecryptionKey = new byte[]{ (byte) 0xC2, (byte) 0xB3, 0x72, 0x3C, (byte) 0xC6, (byte) 0xAE, (byte) 0xD9,
				(byte) 0xB5, 0x34, 0x3C, 0x53, (byte) 0xEE, 0x2F, 0x43, 0x67, (byte) 0xCE };

	}

	/**
	 * Decrypt.
	 *
	 * @param data the data
	 * @return the byte[]
	 */
	public byte[] decrypt(byte[] data) {
		if (!isEnabled)
			return data;
		return _clientDecrypt.Update(data);
	}

	/**
	 * Encrypt.
	 *
	 * @param data the data
	 * @return the byte[]
	 */
	public byte[] encrypt(byte[] data) {
		if (!isEnabled)
			return data; 
		return _serverEncrypt.Update(data);

	}
	
	/**
	 * Inits the crypt
	 *
	 * @param key the key
	 */
	public void init(byte[] key){
		byte[] encryptHash = CryptTools.getKey(ServerEncryptionKey,key);
		_serverEncrypt.init(encryptHash);
		byte[] decryptHash = CryptTools.getKey(ServerDecryptionKey,key);
		_clientDecrypt.init(decryptHash);
		byte[] tar = new byte[1024];
		for(int i = 0; i < tar.length; i++)
			tar[i] = 0;
		
		_serverEncrypt.Update(tar);
		for(int i = 0; i < tar.length; i++)
			tar[i] = 0;
		
		_clientDecrypt.Update(tar);
		this.isEnabled = true;
	}
	
}
