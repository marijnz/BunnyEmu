package bunnyEmu.main.enums;

/**
 * This enum defines all major WoW versions.
 * @author Valkryst
 * --- Last Edit 11-June-2014
 */
public enum ClientVersion {
	VERSION_VANILLA(112), // 1.1.2
	VERSION_BC(243), // 2.4.3
	VERSION_WOTLK(335), // 3.3.5
	VERSION_CATA(406), // 4.0.6
	VERSION_MOP(530); //5.3.0
	
	private final int num;

    private ClientVersion(final int num) {
        this.num = num;
    }

    /** @return The number contained within the enum. */
    public int getNumber() {
        return num;
    }
    
    public static ClientVersion versionStringToEnum(String clientVersion) throws IllegalArgumentException {
    	ClientVersion version = null;
    	
    	switch(clientVersion) {
	    	case "1.1.2" : {
	    		version = ClientVersion.VERSION_VANILLA;
				break;
	    	}
	    	case "2.4.3" : {
	    		version = ClientVersion.VERSION_BC;
				break;
	    	}
	    	case "3.3.5" : {
	    		version = ClientVersion.VERSION_WOTLK;
				break;
	    	}
	    	case "4.0.6" : {
	    		version = ClientVersion.VERSION_CATA;
				break;
	    	}
	    	case "5.3.0" : {
	    		version = ClientVersion.VERSION_MOP;
				break;
	    	}
	    	default : {
	    		throw new IllegalArgumentException("Version " + clientVersion + " is not supported.");
	    	}	
    	}
    	
    	return version;
    }
}
