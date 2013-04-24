package bunnyEmu.main.net.ServerPackets;

import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.utils.BitPack;
import bunnyEmu.main.utils.types.MovementSpeed;
import bunnyEmu.main.utils.types.ObjectMovementValues;

/**
 * Adapted from Arctium, currently only used in the initial update packet.
 * 
 * @author Marijn
 * 
 */
public class UpdatePacket extends ServerPacket {

	public UpdatePacket(String sOpcode, int size) {
		super(sOpcode, size);
	}

	protected void writeUpdateObjectMovementMoP(Char character, byte updateFlags) {
		ObjectMovementValues values = new ObjectMovementValues(updateFlags);
		BitPack bitPack = new BitPack(this);

		bitPack.setGuid(character.getGUID());

		bitPack.write(0); // New in 5.1.0, 654, Unknown
		bitPack.write(values.Bit0);
		bitPack.write(values.HasRotation);
		bitPack.write(values.HasTarget);
		bitPack.write(values.Bit2);
		bitPack.write(values.HasUnknown3);
		bitPack.write(values.BitCounter, 24);
		bitPack.write(values.HasUnknown);
		bitPack.write(values.HasGoTransportPosition);
		bitPack.write(values.HasUnknown2);
		bitPack.write(0); // New in 5.1.0, 784, Unknown
		bitPack.write(values.IsSelf);
		bitPack.write(values.Bit1);
		bitPack.write(values.IsAlive);
		bitPack.write(values.Bit3);
		bitPack.write(values.HasUnknown4);
		bitPack.write(values.HasStationaryPosition);
		bitPack.write(values.IsVehicle);
		bitPack.write(values.BitCounter2, 21);
		bitPack.write(values.HasAnimKits);

		if (values.IsAlive) {
			bitPack.writeGuidMask(new byte[] { 3 });
			bitPack.write(0); // IsInterpolated, not implanted
			bitPack.write(1); // Unknown_Alive_2, Reversed
			bitPack.write(0); // Unknown_Alive_4
			bitPack.writeGuidMask(new byte[] { 2 });
			bitPack.write(0); // Unknown_Alive_1
			bitPack.write(1); // Pitch or splineElevation, not implanted
			bitPack.write(true); // MovementFlags2 are not implanted
			bitPack.writeGuidMask(new byte[] { 4, 5 });
			bitPack.write(0, 24); // BitCounter_Alive_1
			bitPack.write(1); // Pitch or splineElevation, not implanted
			bitPack.write(!values.IsAlive);
			bitPack.write(0); // Unknown_Alive_3
			bitPack.writeGuidMask(new byte[] { 0, 6, 7 });
			bitPack.write(values.IsTransport);
			bitPack.write(!values.HasRotation);

			bitPack.write(true); // Movementflags are not implanted
			bitPack.writeGuidMask(new byte[] { 1 });
			bitPack.write(0); // HasSplineData ?

			bitPack.flush();
			
			
			packet.putFloat((float) MovementSpeed.FlyBackSpeed);
			packet.putFloat((float) MovementSpeed.SwimSpeed);

			float speed = character.getSpeed(); // Speed multiplier
			bitPack.writeGuidBytes(new byte[] { 1 });
			packet.putFloat((float) MovementSpeed.TurnSpeed);
			packet.putFloat(character.getX());
			bitPack.writeGuidBytes(new byte[] { 3 });
			packet.putFloat(character.getZ());
			packet.putFloat(0); // orientation
			packet.putFloat((float) MovementSpeed.RunBackSpeed * speed);
			bitPack.writeGuidBytes(new byte[] { 0, 6 });
			packet.putFloat(character.getY());
			packet.putFloat((float) MovementSpeed.WalkSpeed * speed);
			bitPack.writeGuidBytes(new byte[] { 5 });
			packet.putInt(0);
			packet.putFloat((float) MovementSpeed.PitchSpeed * speed);
			bitPack.writeGuidBytes(new byte[] { 2 });
			packet.putFloat((float) MovementSpeed.RunSpeed * speed);
			bitPack.writeGuidBytes(new byte[] { 7 });
			packet.putFloat((float) MovementSpeed.SwimBackSpeed * speed);
			bitPack.writeGuidBytes(new byte[] { 4 });
			packet.putFloat((float) MovementSpeed.FlySpeed * speed);
		}

		if (values.HasStationaryPosition) {
			packet.putFloat(character.getX());
			packet.putFloat(0); // orientation
			packet.putFloat(character.getY());
			packet.putFloat(character.getZ());
		}

		// TODO: implement?
		if (values.HasRotation)
			packet.putLong(0);
		// packet.writeInt64(Quaternion.GetCompressed(wObject.Position.O));
	}
	

	/**
	 * TODO: Make it working
	 */
	protected void writeUpdateObjectMovementCata(Char character, short updateFlags) {
		this.putShort(updateFlags);  // update flags              
		ObjectMovementValues values = new ObjectMovementValues(updateFlags);

		short moveFlags = 0;
		
	    if (values.IsAlive){
	    	// Build Movement
	    	this.putInt(0); // flags2
	        this.putShort(moveFlags);      		// movement flags
	        this.putInt((int) 0);				// time (in milliseconds)
	        
	        packet.putFloat(character.getX());
	        packet.putFloat(character.getY());
	        packet.putFloat(character.getZ());
	        packet.putFloat(0); // orientation
	        
	        // Movement 2
			packet.putFloat((float) MovementSpeed.WalkSpeed);
			packet.putFloat((float) MovementSpeed.RunSpeed);
			packet.putFloat((float) MovementSpeed.RunBackSpeed); // walk/run back ?
			packet.putFloat((float) MovementSpeed.SwimSpeed);
			packet.putFloat((float) MovementSpeed.SwimBackSpeed);
			packet.putFloat((float) MovementSpeed.FlySpeed);
			packet.putFloat((float) MovementSpeed.FlyBackSpeed);
	        packet.putFloat((float) MovementSpeed.TurnSpeed);
	        packet.putFloat((float) MovementSpeed.PitchSpeed);
	    }
		
	}
	

}
