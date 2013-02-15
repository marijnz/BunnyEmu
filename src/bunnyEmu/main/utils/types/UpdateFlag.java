package bunnyEmu.main.utils.types;

public class UpdateFlag {
	
	public static final short Self 					= 0x0001;
	public static final short Alive 				= 0x0002;
	public static final short Rotation				= 0x0004;
	public static final short StationaryPosition 	= 0x0008;
	public static final short Target 				= 0x0010;
	public static final short Transport 			= 0x0020;
	public static final short GoTransportPosition 	= 0x0040;
	public static final short AnimKits 				= 0x0080;
	public static final short Vehicle 				= 0x0100;
	public static final short Unknown 				= 0x0200;
	public static final short Unknown2 				= 0x0400;
	public static final short Unknown3 				= 0x0800;
	public static final short Unknown4 				= 0x1000;

	
	/* Self                = 0x0001,
        Alive               = 0x0002,
        Rotation            = 0x0004,
        StationaryPosition  = 0x0008,
        Target              = 0x0010,
        Transport           = 0x0020,
        GoTransportPosition = 0x0040,
        AnimKits            = 0x0080,
        Vehicle             = 0x0100,
        Unknown             = 0x0200,
        Unknown2            = 0x0400,
        Unknown3            = 0x0800,
        Unknown4            = 0x1000,
	 */
}
