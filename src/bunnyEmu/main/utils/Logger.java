package bunnyEmu.main.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import bunnyEmu.main.enums.LogType;

/**
 * Utility methods for logging.
 * @author Valkryst
 * --- Last Edit 16-June-2014
 */
public class Logger {
	private final static String verboseLog = "log.txt"; // Contains verbose logs.
	private final static String errorLog = "error_log.txt"; // Contains warnings & errors.
	
    /**
     * Appends the specified string to a log file
     * along with a timestamp in yyyy/MMM/dd-HH:mm:ss
     * format. The log file that it will be appended
     * to depends on the LogType.
     * @param logIn The string to append to the log file.
     */
    public static void writeLog(final String LOG_MESSAGE, final LogType LOG_TYPE) {
        try {
        	// Create the printwriter
        	PrintWriter out;
        	switch(LOG_TYPE) {
	        	case VERBOSE: {
	        		out = new PrintWriter(new BufferedWriter(new FileWriter(verboseLog, true)));
	        		break;
	        	}
	        	default: {
	        		out = new PrintWriter(new BufferedWriter(new FileWriter(errorLog, true)));
	        		break;
	        	}
        	}
        	
        	// Get the timestamp and then append the log to the file.
            String timeStamp = new SimpleDateFormat("yyyy/MMM/dd-HH:mm:ss").format(Calendar.getInstance().getTime());
            out.println("{" + timeStamp + "}--LogType|" + LOG_TYPE.toString() + ": " + LOG_MESSAGE);
            out.close();
            
            // Write every error log message to the terminal as well.
            System.out.println(LOG_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
