package bunnyEmu.main.utils.crypto;

import bunnyEmu.main.utils.Log;


public class BCCrypt extends VanillaCrypt {

	private final static byte[] seed = { 0x38, (byte) 0xA7, (byte) 0x83, 0x15, (byte) 0xF8,(byte)  0x92, 0x25, 0x30, 0x71, (byte) 0x98, 0x67, (byte) 0xB1, (byte) 0x8C, 0x4, (byte) 0xE2, (byte) 0xAA };

	public BCCrypt(){
		Log.log("Created new Burning Crusade crypt");
	}
	
	@Override
	public void init(byte[] key) {
		_key = CryptTools.getKey(seed, key);
		_send_i = _send_j = _recv_i = _recv_j = 0;
	    _initialized = true;
	}
	
}
