package bunnyEmu.main.entities;

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
