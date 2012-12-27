package bunnyEmu.main.utils.crypto;

import java.nio.ByteBuffer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


/*
 * The Class Crypt.
 */
public class VanillaCrypt implements GenericCrypt{
	
	protected boolean _initialized = false;
	protected byte _send_i, _send_j, _recv_i, _recv_j;
	protected byte[] _key;
	
	
	/**
	 * Instantiates a new crypt.
	 */
	public VanillaCrypt() { 
		
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
		byte[] seed = { 0x38, (byte) 0xA7, (byte) 0x83, 0x15, (byte) 0xF8,(byte)  0x92, 0x25, 0x30, 0x71, (byte) 0x98, 0x67, (byte) 0xB1, (byte) 0x8C, 0x4, (byte) 0xE2, (byte) 0xAA };

		//_key = key;
		_key = getKey(seed, key);
		_send_i = _send_j = _recv_i = _recv_j = 0;
	    _initialized = true;
	}
	
	/**
	 * Gets the key.
	 *
	 * @param EncryptionKey the encryption key
	 * @param key the key
	 * @return the key
	
	*/
	
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
	
	 

	

}
