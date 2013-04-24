package bunnyEmu.main.utils.crypto;

/**
 * Tells what a Crypts should have, en/decrypting methods differ each client version
 * 
 * @author Marijn
 *
 */
public interface GenericCrypt {

	/**
	 * @param data The data that has to be encrypted, a server packet.
	 * @return The encrypted data.
	 */
	public byte[] encrypt(byte[] data);
	/**
	 * @param data The data that has to be decrypted, a client packet
	 * @return The decrypted data.
	 */
	public byte[] decrypt(byte[] data);
	/**
	 * Initializes the crypt with the session key, has to be done before en/decrypting
	 * @param key The session key of the client
	 */
	public void init(byte[] key);
	
	
}
