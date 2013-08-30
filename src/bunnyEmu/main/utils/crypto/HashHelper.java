package bunnyEmu.main.utils.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import bunnyEmu.main.utils.BigNumber;
import bunnyEmu.main.utils.Log;

public class HashHelper {

	/* create and return hashPW here */
	public static String generatePasswordHash(String[] accountInfo) throws NoSuchAlgorithmException {
		MessageDigest md;

		md = MessageDigest.getInstance("SHA1");

		String user = accountInfo[1].toUpperCase() + ":" + accountInfo[2].toUpperCase();

		md.update(user.getBytes());

		byte[] accountHash = md.digest();
		Log.log(Log.DEBUG, "AccountHash: " + new BigNumber(accountHash).toHexString());
		return (new BigNumber(accountHash).toHexString());
	}
}
