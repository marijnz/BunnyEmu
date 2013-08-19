package bunnyEmu.main.utils.types;

public class ObjectMovementValues
{
    // Bits
    public boolean HasAnimKits               = false;
    public boolean HasUnknown                = false;
    public int BitCounter                	 = 0;		//unsigned
    public boolean Bit0                      = false;
    public boolean HasUnknown2               = false;
    public boolean IsVehicle                 = false;
    public boolean Bit2                      = false;
    public boolean HasUnknown3               = false;
    public boolean HasStationaryPosition     = false;
    public boolean HasGoTransportPosition    = false;
    public boolean IsSelf                    = false;
    public boolean IsAlive                   = false;
    public int BitCounter2               	 = 0;		//unsigned
    public boolean Bit3                      = false;
    public boolean HasUnknown4               = false;
    public boolean HasTarget                 = false;
    public boolean Bit1                      = false;
    public boolean HasRotation               = false;
    public boolean IsTransport               = false;
    public boolean HasMovementFlags          = false;
    public boolean HasMovementFlags2         = false;
    public boolean IsInterpolated            = false;
    public boolean IsInterpolated2           = false;

    // Data
    //public MovementFlag MovementFlags     = 0;
   // public MovementFlag2 MovementFlags2   = 0;
    //public uint Time                      = 0;

    public ObjectMovementValues() { }
    public ObjectMovementValues(int updateflags) {
        IsSelf                 = (updateflags & UpdateFlag.Self)                != 0;
        IsAlive                = (updateflags & UpdateFlag.Alive)               != 0;
        HasRotation            = (updateflags & UpdateFlag.Rotation)            != 0;
        HasStationaryPosition  = (updateflags & UpdateFlag.StationaryPosition)  != 0;
        HasTarget              = (updateflags & UpdateFlag.Target)              != 0;
        IsTransport            = (updateflags & UpdateFlag.Transport)           != 0;
        HasGoTransportPosition = (updateflags & UpdateFlag.GoTransportPosition) != 0;
        HasAnimKits            = (updateflags & UpdateFlag.AnimKits)            != 0;
        IsVehicle              = (updateflags & UpdateFlag.Vehicle)             != 0;
        HasUnknown             = (updateflags & UpdateFlag.Unknown)             != 0;
        HasUnknown2            = (updateflags & UpdateFlag.Unknown2)            != 0;
        HasUnknown3            = (updateflags & UpdateFlag.Unknown3)            != 0;
        HasUnknown4            = (updateflags & UpdateFlag.Unknown4)            != 0;
    }
}