package bunnyEmu.main.utils.crypto;

/**
 * Tells what a Crypts should have, en/decrypting methods differ each client version
 * 
 * @author Marijn
 *
 */
public interface GenericCrypt {

	public byte[] encrypt(byte[] data);
	public byte[] decrypt(byte[] data);
	public void init(byte[] key);
	
	
}
