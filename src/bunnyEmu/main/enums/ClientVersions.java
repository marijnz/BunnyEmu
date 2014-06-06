package bunnyEmu.main.enums;

/**
 * This enum defines all major WoW versions.
 * @author Valkryst
 * --- Last Edit 6-June-2014
 */
public enum ClientVersions {
	VERSION_VANILLA(112), // 1.1.2
	VERSION_BC(243), // 2.4.3
	VERSION_WOTLK(335), // 3.3.5
	VERSION_CATA(406), // 4.0.6
	VERSION_MOP(530); //5.3.0
	
	private final int num;

    private ClientVersions(final int num) {
        this.num = num;
    }

    /** @return The number contained within the enum. */
    public int getNumber() {
        return num;
    }
}
