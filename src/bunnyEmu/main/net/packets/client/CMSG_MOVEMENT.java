package bunnyEmu.main.net.packets.client;

import bunnyEmu.main.entities.packet.ClientPacket;
import bunnyEmu.main.utils.BitUnpack;
import bunnyEmu.main.utils.math.Vector4;
import bunnyEmu.main.utils.types.MovementValues;

/**
 * Handles all movement packets, adapted from Arctium
 * 
 * @author Marijn
 * 
 */
public class CMSG_MOVEMENT extends ClientPacket {

	private MovementValues movementValues;
	private Vector4 position;

	public CMSG_MOVEMENT() {
		movementValues = new MovementValues();
	}

	public boolean readMoP() {
		BitUnpack BitUnpack = new BitUnpack(this);

		boolean[] guidMask = new boolean[8];
		byte[] guidBytes = new byte[8];

		position = new Vector4(packet.getFloat(), packet.getFloat(),
				packet.getFloat());

		guidMask[3] = BitUnpack.getBit();

		boolean HasPitch = !BitUnpack.getBit();

		guidMask[0] = BitUnpack.getBit();

		int counter = BitUnpack.getBits(22);

		guidMask[2] = BitUnpack.getBit();

		boolean HasSplineElevation = !BitUnpack.getBit();

		movementValues.HasRotation = !BitUnpack.getBit();

		BitUnpack.getBit(); // unk1
		BitUnpack.getBit(); // unk2

		guidMask[7] = BitUnpack.getBit();

		boolean HasTime = !BitUnpack.getBit();

		movementValues.IsFallingOrJumping = BitUnpack.getBit();
		movementValues.HasMovementFlags2 = !BitUnpack.getBit();
		movementValues.HasMovementFlags = !BitUnpack.getBit();

		boolean Unknown3 = !BitUnpack.getBit();
		BitUnpack.getBit(); // unk4

		guidMask[6] = BitUnpack.getBit();
		guidMask[1] = BitUnpack.getBit();

		movementValues.IsTransport = BitUnpack.getBit();

		guidMask[4] = BitUnpack.getBit();
		guidMask[5] = BitUnpack.getBit();

		if (movementValues.HasMovementFlags)
			movementValues.MovementFlags = BitUnpack.getBits(30);

		if (movementValues.IsFallingOrJumping)
			movementValues.HasJumpData = BitUnpack.getBit();

		if (movementValues.HasMovementFlags2)
			movementValues.MovementFlags2 = BitUnpack.getBits(13);

		if (guidMask[0])
			guidBytes[0] = (byte) (packet.get() ^ 1);

		for (int i = 0; i < counter; i++)
			packet.getInt();

		if (guidMask[4])
			guidBytes[4] = (byte) (packet.get() ^ 1);
		if (guidMask[1])
			guidBytes[1] = (byte) (packet.get() ^ 1);
		if (guidMask[5])
			guidBytes[5] = (byte) (packet.get() ^ 1);
		if (guidMask[6])
			guidBytes[6] = (byte) (packet.get() ^ 1);
		if (guidMask[2])
			guidBytes[2] = (byte) (packet.get() ^ 1);
		if (guidMask[3])
			guidBytes[3] = (byte) (packet.get() ^ 1);
		if (guidMask[7])
			guidBytes[7] = (byte) (packet.get() ^ 1);

		if (HasPitch)
			packet.getFloat();

		if (movementValues.HasRotation)
			position.setO(packet.getFloat());

		if (movementValues.IsFallingOrJumping) {
			movementValues.JumpVelocity = packet.getFloat();

			if (movementValues.HasJumpData) {
				movementValues.CurrentSpeed = packet.getFloat();
				movementValues.Sin = packet.getFloat();
				movementValues.Cos = packet.getFloat();
			}

			movementValues.FallTime = packet.getInt();
		}

		if (Unknown3)
			packet.getInt();

		if (HasSplineElevation)
			packet.getFloat();

		if (HasTime)
			movementValues.Time = packet.getInt();
		
		return true;
	}

	public MovementValues getMovementValues() {
		return movementValues;
	}

	public Vector4 getPosition() {
		return position;
	}
}
