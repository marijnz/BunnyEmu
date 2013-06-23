package bunnyEmu.main.utils.crypto;

import bunnyEmu.main.utils.Log;


/*
 * The Class Crypt.
 */
public class VanillaCrypt implements GenericCrypt{
	
	protected boolean _initialized = false;
	protected byte _send_i, _send_j, _recv_i, _recv_j;
	protected byte[] _key;
	
	
		/**
		 * Instantiates a new Vanilla crypt.
		 */
	
	public VanillaCrypt() { 
		Log.log("Created new Vanilla crypt");
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
}
