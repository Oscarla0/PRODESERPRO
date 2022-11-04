package carreras;

public class Relevo {

	public static Carrera carrera=new Carrera();
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Carrera carrera=new Carrera();
		
		Thread[] relevistas=new Thread[4];
		for (int i=0; i<4; i++) {
			relevistas[i]=new Thread() {
				public void run() {
					carrera.correr();
				}
			};
		}
		for (int i=0; i<4; i++) {
			relevistas[i].start();
		}
		Thread.sleep(10000);
		carrera.darSalida();
		for (int i=0; i<4; i++) {
			relevistas[i].join();
		}
		System.out.println("Todos los hilos terminaro");
	
	}

}
