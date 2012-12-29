package bunnyEmu.main.utils.crypto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CryptTools {

	/**
	 * Gets the key.
	 *
	 * @param EncryptionKey the encryption key
	 * @param key the session key (K)
	 * @return the HmacSHA1 key
	 */
	public static byte[] getKey(byte[] EncryptionKey,byte[] key) {
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
