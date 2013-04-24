package bunnyEmu.main.entities.character;

/**
 * A Skill that can be taught to a Character.
 * 
 * @author Marijn
 *
 */
public class Skill {

	private int id;
	
	public Skill(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
}
