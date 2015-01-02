package bunnyEmu.main.handlers;

import java.io.FileInputStream;
import java.util.Properties;

import misc.Logger;

/**
 * Manages assets/server.conf
 * 
 * @author Wazy
 *
 */
public class ConfigHandler {

	/* load properties and return them */
	public static Properties loadProperties() {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("assets/server.conf"));
			return prop;
		}
		catch (Exception e) {
			Logger.writeLog("Unable to load configuration file 'server.conf' from assets folder... terminating.", Logger.LOG_TYPE_VERBOSE);
			System.exit(0);
		}

		return null;
	}
}
