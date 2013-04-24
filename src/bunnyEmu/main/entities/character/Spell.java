package bunnyEmu.main.entities.character;

/**
 * A Spell that can be taught to a Character.
 * 
 * @author Marijn
 *
 */
public class Spell {

	private int id;
	
	public Spell(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
}
