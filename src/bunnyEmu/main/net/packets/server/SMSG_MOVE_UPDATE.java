package bunnyEmu.main.net.packets.server;

import bunnyEmu.main.entities.character.Char;
import bunnyEmu.main.entities.packet.ServerPacket;
import bunnyEmu.main.utils.BitPack;
import bunnyEmu.main.utils.Opcodes;
import bunnyEmu.main.utils.math.Vector4;
import bunnyEmu.main.utils.types.MovementValues;
import misc.Logger;

/**
 * Movement update packet.
 * 
 * @author Marijn
 * 
 */
public class SMSG_MOVE_UPDATE extends ServerPacket {

	private Char character;
	private MovementValues movementValues;
	private Vector4 position;

	public SMSG_MOVE_UPDATE(Char character, MovementValues movementValues, Vector4 position) {
		super(Opcodes.SMSG_MOVE_UPDATE, 300);
		this.character = character;
		this.movementValues = movementValues;
		this.position = position;
	}

	public boolean writeMoP() {
		 BitPack BitPack = new BitPack(this, character.getGUID());

         BitPack.writeGuidMask((byte) 0);

         BitPack.write(!movementValues.HasRotation);
         BitPack.write(!movementValues.HasMovementFlags2);

         BitPack.writeGuidMask((byte) 5);

         if (movementValues.HasMovementFlags2)
             BitPack.write(movementValues.MovementFlags2, 13);
         
         Logger.writeLog("MovementFlags: " + movementValues.MovementFlags, Logger.LOG_TYPE_VERBOSE);

         BitPack.write(!movementValues.HasMovementFlags);
         BitPack.write(0);
         BitPack.write(0, 22);
         BitPack.write(1);
         BitPack.write(1);
         BitPack.write(0);
         BitPack.write(0);

         BitPack.writeGuidMask((byte)7);

         BitPack.write(movementValues.IsTransport);
         BitPack.write(1);

         BitPack.writeGuidMask((byte)4, (byte)1);

         BitPack.write(movementValues.IsFallingOrJumping);

         if (movementValues.HasMovementFlags)
             BitPack.write(movementValues.MovementFlags, 30);

         BitPack.write(movementValues.Time == 0);

         if (movementValues.IsFallingOrJumping)
             BitPack.write(movementValues.HasJumpData);

         BitPack.writeGuidMask((byte) 2, (byte) 3, (byte) 6);

         BitPack.flush();

         BitPack.writeGuidBytes((byte) 6);

         if (movementValues.Time != 0)
             putInt(movementValues.Time);

         if (movementValues.IsFallingOrJumping)
         {
             putFloat(movementValues.JumpVelocity);

             if (movementValues.HasJumpData)
             {
                 putFloat(movementValues.Cos);
                 putFloat(movementValues.CurrentSpeed);
                 putFloat(movementValues.Sin);
             }

             putInt(movementValues.FallTime);
         }

         putFloat(position.getX());

         BitPack.writeGuidBytes((byte) 1);

         putFloat(position.getY());

         BitPack.writeGuidBytes((byte) 2, (byte) 7, (byte) 5);

         Logger.writeLog("POSITION: " + position.toString(), Logger.LOG_TYPE_VERBOSE);
         putFloat(position.getZ());

         BitPack.writeGuidBytes((byte) 0, (byte) 4, (byte) 3);

         if (movementValues.HasRotation)
             putFloat(position.getO());
         this.wrap();
		return true;
	}
}
