package bunnyEmu.main.entities;

import bunnyEmu.main.utils.types.MovementSpeed;

public abstract class Unit extends WorldObject{
	protected MovementSpeed movement;
	
	public MovementSpeed getMovement(){
		return movement;
	}
}
