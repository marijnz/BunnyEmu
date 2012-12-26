package bunnyEmu.main.utils.crypto;

public interface GenericCrypt {

	public byte[] encrypt(byte[] data);
	public byte[] decrypt(byte[] data);
	public void init(byte[] key);
	
}
