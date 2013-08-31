package bunnyEmu.main.utils.math;


public class Vector4 {

	private float x;
	private float y;
	private float z;
	private float o;
	
	public Vector4(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector4(float x, float y, float z, float o){
		set(x, y, z, o);
	}
	
	public void set(float x, float y, float z, float o){
		this.x = x;
		this.y = y;
		this.z = z;
		this.o = o;
	}
	
	public void set(Vector4 other){
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		this.o = other.o;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public float getZ(){
		return z;
	}
	
	public float getO(){
		return o;
	}
	
	public void setX(float val){
		this.x = val;
	}
	
	public void setY(float val){
		this.y = val;
	}
	
	public void setZ(float val){
		this.z = val;
	}
	
	public void setO(float val){
		this.o = val;
	}
	
	public String toString(){
		return "X: " + x + " Y: " + y + " Z: " + z + " O: " + o;
	}
}
