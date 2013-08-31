package bunnyEmu.main.utils.types;

public class MovementValues
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
    public boolean IsFallingOrJumping        = false; // MoP?
    public boolean HasJumpData               = false; // MoP?

    // Data
    public int MovementFlags     			= 0;
    public int MovementFlags2   			= 0;
    public int Time                      	= 0;
    
    // Jumping & Falling
    public float JumpVelocity             	= 0;
    public float Cos                      	= 0;
    public float Sin                     	= 0;
    public float CurrentSpeed             	= 0;
    public int FallTime                  	= 0;

    public MovementValues() { }
    public MovementValues(int updateflags) {
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
    
    /*
    public class MovementFlag {
        private int Forward            = 0x1;
        private int Backward           = 0x2;
        private int StrafeLeft         = 0x4;
        private int StrafeRight        = 0x8;
        private int TurnLeft           = 0x10;
        private int TurnRight          = 0x20;
        private int PitchUp            = 0x40;
        private int PitchDown          = 0x80;
        private int RunMode            = 0x100;
        private int Gravity            = 0x200;
        private int Root               = 0x400;
        private int Falling            = 0x800;
        private int FallReset          = 0x1000;
        private int PendingStop        = 0x2000;
        private int PendingStrafeStop  = 0x4000;
        private int PendingForward     = 0x8000;
        private int PendingBackward    = 0x10000;
        private int PendingStrafeLeft  = 0x20000;
        private int PendingStrafeRight = 0x40000;
        private int PendingRoot        = 0x80000;
        private int Swim               = 0x100000;
        private int Ascension          = 0x200000;
        private int Descension         = 0x400000;
        private int CanFly             = 0x800000;
        private int Flight             = 0x1000000;
        private int IsSteppingUp       = 0x2000000;
        private int WalkOnWater        = 0x4000000;
        private int FeatherFall        = 0x8000000;
        private int HoverMove          = 0x10000000;
        private int Collision          = 0x20000000;
    }
    */
}