package math;

public class Intervall {

	double a, b;
	
	public Intervall (double a, double b){
		this.a = a;
		this.b = b;
	}
	
	public boolean contains(double d){
		return a <= d && d <= b;
	}
	
	public boolean overlaps(Intervall i){
		return b >= i.a && a <= i.b;
	}
	
	public boolean isDisjointFrom(Intervall i){
		return b < i.a || a > i.b;
	}
	
	public void set(double a, double b){
		this.a = a;
		this.b = b;
	}
	
}
