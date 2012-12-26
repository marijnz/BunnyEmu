package bunnyEmu.main.utils.crypto.vanilla;

import java.nio.ByteBuffer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import bunnyEmu.main.utils.crypto.GenericCrypt;

/*
 * The Class Crypt.
 */
public class VanillaCrypt implements GenericCrypt{
	
	private byte[] vanillaKey = { (byte) 0xC2, (byte) 0xB3, (byte) 0x72, (byte) 0x3C, (byte) 0xC6,(byte) 0xAE, (byte) 0xD9, (byte) 0xB5, (byte) 0x34, (byte) 0x3C, (byte) 0x53, (byte) 0xEE, (byte) 0x2F, (byte) 0x43, (byte) 0x67, (byte) 0xCE };
	private boolean _initialized;
	private byte _send_i, _send_j, _recv_i, _recv_j;
	private byte[] _key;
	
	
	/**
	 * Instantiates a new crypt.
	 */
	public VanillaCrypt() { 
		_initialized = false;
	}

	public byte[] decrypt(byte[] data){
	    if (!_initialized) return data;

	    for (int t = 0; t < data.length; t++) {
	        _recv_i %= _key.length;
	        byte x = (byte) ((data[t] - _recv_j) ^ _key[_recv_i]);
	        ++_recv_i;
	        _recv_j = data[t];
	        data[t] = x;
	    }
	    return data;
	}

	public byte[] encrypt(byte[] data){
	    if (!_initialized) return data;

	    for (int t = 0; t < data.length; t++){
	        _send_i %= _key.length;
	        byte x = (byte) ((data[t] ^ _key[_send_i]) + _send_j);
	        ++_send_i;
	        data[t] = _send_j = x;
	    }
	    return data;
	}

	@Override
	public void init(byte[] key) {
		_key = key;
		_send_i = _send_j = _recv_i = _recv_j = 0;
	    _initialized = true;
	}
	
	/**
	 * Gets the key.
	 *
	 * @param EncryptionKey the encryption key
	 * @param key the key
	 * @return the key
	
	private byte[] getKey(byte[] EncryptionKey,byte[] key) {
		SecretKeySpec ds = new SecretKeySpec(EncryptionKey, "HmacSHA1");
		Mac m;
		try {
			m = Mac.getInstance("HmacSHA1");
			m.init(ds);
			return  m.doFinal(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	 */

	

}
