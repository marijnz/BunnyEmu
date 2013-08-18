package bunnyEmu.main.net.packets.server;

import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.entities.packet.ServerPacket;
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
		MovementSpeed movementSpeed = character.getMovement();
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
			
			
			packet.putFloat((float) movementSpeed.FlyBackSpeed);
			packet.putFloat((float) movementSpeed.SwimSpeed);

			float speed = character.getCharSpeed(); // Speed multiplier
			bitPack.writeGuidBytes(new byte[] { 1 });
			packet.putFloat((float) movementSpeed.TurnSpeed);
			packet.putFloat(character.getX());
			bitPack.writeGuidBytes(new byte[] { 3 });
			packet.putFloat(character.getZ());
			packet.putFloat(0); // orientation
			packet.putFloat((float) movementSpeed.RunBackSpeed * speed);
			bitPack.writeGuidBytes(new byte[] { 0, 6 });
			packet.putFloat(character.getY());
			packet.putFloat((float) movementSpeed.WalkSpeed * speed);
			bitPack.writeGuidBytes(new byte[] { 5 });
			packet.putInt(0);
			packet.putFloat((float) movementSpeed.PitchSpeed * speed);
			bitPack.writeGuidBytes(new byte[] { 2 });
			packet.putFloat((float) movementSpeed.RunSpeed * speed);
			bitPack.writeGuidBytes(new byte[] { 7 });
			packet.putFloat((float) movementSpeed.SwimBackSpeed * speed);
			bitPack.writeGuidBytes(new byte[] { 4 });
			packet.putFloat((float) movementSpeed.FlySpeed * speed);
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
		MovementSpeed movementSpeed = character.getMovement();
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
			packet.putFloat((float) movementSpeed.WalkSpeed);
			packet.putFloat((float) movementSpeed.RunSpeed);
			packet.putFloat((float) movementSpeed.RunBackSpeed); // walk/run back ?
			packet.putFloat((float) movementSpeed.SwimSpeed);
			packet.putFloat((float) movementSpeed.SwimBackSpeed);
			packet.putFloat((float) movementSpeed.FlySpeed);
			packet.putFloat((float) movementSpeed.FlyBackSpeed);
	        packet.putFloat((float) movementSpeed.TurnSpeed);
	        packet.putFloat((float) movementSpeed.PitchSpeed);
	    }
		
	}
	
	/**
	 * TODO: make it working
	 */
	protected void writeUpdateObjectMovementWotLK(Char character, short updateFlags) {
		MovementSpeed movementSpeed = character.getMovement();
		this.putShort(updateFlags);  // update flags              
		//ObjectMovementValues values = new ObjectMovementValues(updateFlags);
		
	    //if (values.IsAlive){
	    	// Build Movement
	    	this.putInt(0); // flags2
	        this.putShort((short) 0);      		// movement flags
	        this.putInt((int) 0);				// time (in milliseconds) TODO: actual time?
	        
	        packet.putFloat(character.getX());
	        packet.putFloat(character.getY());
	        packet.putFloat(character.getZ());
	        packet.putFloat(0); // orientation
	        
	        packet.putInt(0); // Fall time
	        /* 32 bytes so far */
	        // Movement 2
	        
			packet.putFloat((float) movementSpeed.WalkSpeed);
			packet.putFloat((float) movementSpeed.RunSpeed);
			packet.putFloat((float) movementSpeed.RunBackSpeed); // walk/run back ?
			packet.putFloat((float) movementSpeed.SwimSpeed);
			packet.putFloat((float) movementSpeed.SwimBackSpeed);
			packet.putFloat((float) movementSpeed.FlySpeed);
			packet.putFloat((float) movementSpeed.FlyBackSpeed);
	        packet.putFloat((float) movementSpeed.TurnSpeed);
	        packet.putFloat((float) movementSpeed.PitchSpeed);
	        
	        /* 32+36 = 68 bytes so far */
	   // }
	}

}
