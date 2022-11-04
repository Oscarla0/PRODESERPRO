package carreras;

public class Carrera {

	private int contador=1;
	public synchronized void  correr() {
		try {
			wait();
			System.out.println("Soy el thread "+contador+" corriendo ...");
			Thread.sleep(10000);
			if (contador<4) {
				contador++;
				System.out.println("Terminé. Paso el testigo al hijo "+contador);
				notify();
			}
			else
				System.out.println("Termine.");
		} catch(InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		
		
	
	}
	
	public synchronized void darSalida() {
		System.out.println("Doy la salida");
		notify();
		
	}
}
