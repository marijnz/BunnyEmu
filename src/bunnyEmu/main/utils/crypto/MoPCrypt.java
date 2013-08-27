package bunnyEmu.main.utils.crypto;

import bunnyEmu.main.utils.Log;

public class MoPCrypt extends WotLKCrypt{
	
	/**
	 * Instantiates a new Mists of Pandaria crypt.
	 */
	
	public MoPCrypt(){
		Log.log(Log.DEBUG, "Created MoP Crypt!");
		this.ServerEncryptionKey = new byte[] { 0x08, (byte) 0xF1, (byte) 0x95, (byte) 0x9F, 0x47, (byte) 0xE5, (byte) 0xD2, (byte) 0xDB, (byte) 0xA1, 0x3D, 0x77, (byte) 0x8F, 0x3F, 0x3E, (byte) 0xE7, 0x00 };
		this.ServerDecryptionKey = new byte[] { 0x40, (byte) 0xAA, (byte) 0xD3, (byte) 0x92, 0x26, 0x71, 0x43, 0x47, 0x3A, 0x31, 0x08, (byte) 0xA6, (byte) 0xE7, (byte) 0xDC, (byte) 0x98, 0x2A };
	}
	
	@Override
	public byte[] decrypt(byte[] data) {
		byte[] newBytes = new byte[4];
		System.arraycopy(data, 0, newBytes, 0, 4);
		return super.decrypt(newBytes);
	}

	public byte[] encrypt(byte[] data) {
		byte[] newBytes = new byte[4];
		System.arraycopy(data, 0, newBytes, 0, 4);
		return super.encrypt(newBytes);

	}

}
