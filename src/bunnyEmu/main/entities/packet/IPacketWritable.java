package bunnyEmu.main.entities.packet;

/**
 * Interface that gives a structure to Server packets.
 * 
 * @author Marijn
 *
 */
public interface IPacketWritable {
	public boolean writeVanilla();
	public boolean writeBC();
	public boolean writeWotLK();
	public boolean writeCata();
	public boolean writeMoP();
	public boolean writeGeneric();
}
