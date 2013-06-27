package bunnyEmu.main.utils.types.fields;

/**
 * UpdateFields for MoP 5.1.0
 * 
 * @author Marijn
 *
 */
public class UpdateFields {

	 public class ObjectFields {
		 public static final String Guid                      		 =  "Guid";
		 public static final String Data                              = "Data";
		 public static final String Type                              = "Type";
		 public static final String Scale                             = "Scale";
	 };

    public class UnitFields {
    	public static final String DisplayPower                      = "DisplayPower";
    	public static final String Health                            = "Health";
    	public static final String MaxHealth                         = "MaxHealth";
    	public static final String Level                             = "Level";
    	public static final String FactionTemplate                   = "FactionTemplate";
    	public static final String DisplayID                   		 = "DisplayID";
    	public static final String NativeDisplayID                   = "NativeDisplayID";
    }
    
	public class PlayerFields{
		public static final String HairColorID                       = "HairColorID";
    	public static final String RestState                         = "RestState";
    	public static final String ArenaFaction                      = "ArenaFaction";
    	public static final String VirtualPlayerRealm                = "VirtualPlayerRealm";
    	public static final String CurrentSpecID                     = "CurrentSpecID";
    	public static final String XP                                = "XP";
    	public static final String NextLevelXP                       = "NextLevelXP";
    	public static final String Skill                             = "Skill";
    	public static final String WatchedFactionIndex               = "WatchedFactionIndex";
    }
}