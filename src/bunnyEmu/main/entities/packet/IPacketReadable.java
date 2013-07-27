package bunnyEmu.main.entities.packet;

/**
 * Interface that gives a structure to Server packets.
 * 
 * @author Marijn
 *
 */
public interface IPacketReadable {
	public boolean readVanilla();
	public boolean readBC();
	public boolean readWotLK();
	public boolean readCata();
	public boolean readMoP();
	public boolean readGeneric();
}
