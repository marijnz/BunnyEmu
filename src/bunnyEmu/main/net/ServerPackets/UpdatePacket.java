package bunnyEmu.main.net.ServerPackets;

import bunnyEmu.main.entities.Char;
import bunnyEmu.main.entities.ServerPacket;
import bunnyEmu.main.utils.BitPack;
import bunnyEmu.main.utils.types.MovementSpeed;
import bunnyEmu.main.utils.types.ObjectMovementValues;

/**
 * Adapted from Arctium
 * 
 * @author Marijn
 * 
 */
public class UpdatePacket extends ServerPacket {

	public UpdatePacket(String sOpcode, int size) {
		super(sOpcode, size);
	}

	protected void writeUpdateObjectMovement(Char character, int updateFlags) {
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

			if (values.IsTransport) {
				// Transports not implanted.
			}

			/*
			 * MovementFlags2 are not implanted if (movementFlag2 != 0)
			 * bitPack.write(0, 12);
			 */

			bitPack.write(true); // Movementflags are not implanted
			bitPack.writeGuidMask(new byte[] { 1 });

			/*
			 * IsInterpolated, not implanted if (IsInterpolated) {
			 * bitPack.write(0); // IsFalling }
			 */

			bitPack.write(0); // HasSplineData, don't write simple basic
								// splineData

			/*
			 * Movementflags are not implanted if (movementFlags != 0)
			 * bitPack.write((uint)movementFlags, 30);
			 */

			// Don't send basic spline data and disable advanced data
			// if (HasSplineData)
			// bitPack.write(0); // Disable advance splineData
		}

		bitPack.flush();

		if (values.IsAlive) {
			packet.putFloat((float) MovementSpeed.FlyBackSpeed);

			// Don't send basic spline data
			/*
			 * if (HasSplineBasicData) { // Advanced spline data not implanted
			 * if (HasAdvancedSplineData) {
			 * 
			 * }
			 * 
			 * packet.writeFloat(character.X); packet.writeFloat(character.Y);
			 * packet.writeUInt32(0); packet.writeFloat(character.Z); }
			 */

			packet.putFloat((float) MovementSpeed.SwimSpeed);

			if (values.IsTransport) {
				// Not implanted
			}

			bitPack.writeGuidBytes(new byte[] { 1 });
			packet.putFloat((float) MovementSpeed.TurnSpeed);
			packet.putFloat(character.getY());
			bitPack.writeGuidBytes(new byte[] { 3 });
			packet.putFloat(character.getZ());
			packet.putFloat(0); // orientation
			packet.putFloat((float) MovementSpeed.RunBackSpeed);
			bitPack.writeGuidBytes(new byte[] { 0, 6 });
			packet.putFloat(character.getX());
			packet.putFloat((float) MovementSpeed.WalkSpeed);
			bitPack.writeGuidBytes(new byte[] { 5 });
			packet.putInt(0);
			packet.putFloat((float) MovementSpeed.PitchSpeed);
			bitPack.writeGuidBytes(new byte[] { 2 });
			packet.putFloat((float) MovementSpeed.RunSpeed);
			bitPack.writeGuidBytes(new byte[] { 7 });
			packet.putFloat((float) MovementSpeed.SwimBackSpeed);
			bitPack.writeGuidBytes(new byte[] { 4 });
			packet.putFloat((float) MovementSpeed.FlySpeed);
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

}
