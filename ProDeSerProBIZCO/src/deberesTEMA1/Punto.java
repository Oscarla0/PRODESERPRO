package deberesTEMA1;

public class Punto {
	
	private double X;
	private double Y;
	
	public Punto(double x, double y) {
		super();
		X = x;
		Y = y;
	}

	public double getX() {
		return X;
	}

	public void setX(double x) {
		X = x;
	}

	public double getY() {
		return Y;
	}

	public void setY(double y) {
		Y = y;
	}

	@Override
	public String toString() {
		return "Punto [X=" + X + ", Y=" + Y + "]";
	}

	public double distancia(Punto punto) {
		return Math.sqrt(Math.pow(punto.getX()-X, 2.0)+
					Math.pow(punto.getY()-Y,2.0));
	}
	

	

}
