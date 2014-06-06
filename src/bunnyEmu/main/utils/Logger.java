package bunnyEmu.main.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Utility methods for logging.
 * @author Valkryst
 * --- Last Edit 15-Mar-2014
 */
public class Logger {
    /**
     * Appends the specified string to the log file
     * along with a timestamp in yyyy/MMM/dd-HH:mm:ss
     * format.
     * @param logIn The string to append to the log file.
     */
    public static void writeLog(String logIn) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("log.txt", true)));
            String timeStamp = new SimpleDateFormat("yyyy/MMM/dd-HH:mm:ss").format(Calendar.getInstance().getTime());
            out.println("{"+timeStamp+"}: "+ logIn);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Appends the specified string to the error file
     * along with a timestamp in yyyy/MMM/dd-HH:mm:ss
     * format.
     * @param errorIn The string to append to the log file.
     */
    public static void writeError(String errorIn) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("errorLog.txt", true)));
            String timeStamp = new SimpleDateFormat("yyyy/MMM/dd-HH:mm:ss").format(Calendar.getInstance().getTime());
            out.println("{"+timeStamp+"}: " + errorIn);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
