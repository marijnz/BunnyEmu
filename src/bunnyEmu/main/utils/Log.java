/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bunnyEmu.main.utils;



/**
 *
 * @author Marijn
 */
public class Log {
    
    public static final int NONE = 0;
    public static final int ERROR = 1;
    public static final int INFO = 2;
    public static final int DEBUG = 3;
    
    private static int _level = 1;
    
    public static void log(String message){
         log(_level, message);
    }
    
    public static void log(byte message){
        log(_level, message);
    }
    
    public static void log(int level, byte message){
    	log(level, String.valueOf(message));
    }
    
    public static void log(short message){
        log(_level, message);
    }
    
    public static void log(int level, short message){
    	log(level, String.valueOf(message));
    }
    
     public static void log(int message){
        log(_level, message);
    }
    
    public static void log(int level, int message){
        log(level, String.valueOf(message));
    }
    
    public static void log(int level, String message){
    	 switch(level){
        	case ERROR: System.out.println("ERROR: " + message); break;
        	case INFO:	System.out.println("INFO: " + message); break;
        	case DEBUG:	System.out.println("DEBUG: " + message); break;
        }
    }
    
    
    public static void setlevel(int level){
        _level = level;
        log(DEBUG, "Logger level changed to " + level);
    }
    
    public static void logPacket(byte[] packet){
        
    	
    }
}
