package nubePuntos;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class NubeDePuntos {
	
	private final static int N=100_000;
	
	private static Punto[] minPrimeraMitad=new Punto[2];
	private static Punto[] minSegundaMitad=new Punto[2];
	private static Punto[] minUnionDosMitades=new Punto[2];
	
	public static void main(String[] args) throws InterruptedException {
		
		final Punto[] a=createArray(N);

		long startTime=0;
		long timeNanos=0;
		Punto[] resultado=new Punto[2];
		
		startTime=System.nanoTime();
		resultado=hallarMinimaDistanciaIter(a);		
		timeNanos=System.nanoTime()-startTime;
		printResult("Algoritmo iterativo:",timeNanos,resultado);
		
		
		startTime=System.nanoTime();
		resultado=hallarMinDistanciaTresForks(a);		
		timeNanos=System.nanoTime()-startTime;
		printResult("Algoritmo iterativo con 3 hilos:",timeNanos,resultado);

		startTime=System.nanoTime();
		resultado=hallarMinDistanciaRec(a,0,a.length-1);		
		timeNanos=System.nanoTime()-startTime;
		printResult("Algoritmo recursivo:",timeNanos,resultado);

		startTime=System.nanoTime();
		MinDistanciaAction p=new MinDistanciaAction(a,0,a.length-1);
		ForkJoinPool.commonPool().invoke(p);	
		resultado=p.resultado;		
		timeNanos=System.nanoTime()-startTime;
		printResult("Algoritmo recursivo API Fork/Join java :",timeNanos,resultado);

		
		
	}
	
	public static Punto[] hallarMinimaDistanciaIter(Punto[] a) {
		Punto[] resultado=new Punto[2];
		
		double distanciaMinima=a[0].distancia(a[1]);
		resultado[0]=a[0];
		resultado[1]=a[1];
		for (int i=0; i< a.length; i++) {
			for (int j=0; j< a.length; j++) {
				double distancia=a[i].distancia(a[j]);
				if (distancia<distanciaMinima) {
					distanciaMinima=distancia;
					resultado[0]=a[i];
					resultado[1]=a[j];
				}
			}
		}
		
		return resultado;
	}
	
	
	public static Punto[] hallarMinDistanciaTresForks(Punto[] a) throws InterruptedException {
		
		Thread primeraMitad=new Thread(){
			public void run() {
				double distanciaMinima=a[0].distancia(a[1]);
				minPrimeraMitad[0]=a[0];
				minPrimeraMitad[1]=a[1];
				for (int i=0; i<a.length/2; i++) {
					for (int j=0; j<a.length/2; j++) {
						double distancia=a[i].distancia(a[j]);
						if (distancia<distanciaMinima) {
							distanciaMinima=distancia;
							minPrimeraMitad[0]=a[i];
							minPrimeraMitad[1]=a[j];
						}
					}
				}
			}
		};
		
		Thread segundaMitad=new Thread(){
			public void run() {
				double distanciaMinima=a[0].distancia(a[1]);
				minSegundaMitad[0]=a[0];
				minSegundaMitad[1]=a[1];
				for (int i=a.length/2; i<a.length; i++) {
					for (int j=a.length/2; j<a.length; j++) {
						double distancia=a[i].distancia(a[j]);
						if (distancia<distanciaMinima) {
							distanciaMinima=distancia;
							minSegundaMitad[0]=a[i];
							minSegundaMitad[1]=a[j];
						}
					}
				}
	
			}
		};
		
		Thread terceraMitad=new Thread(){
			public void run() {
				double distanciaMinima=a[0].distancia(a[1]);
				minUnionDosMitades[0]=a[0];
				minUnionDosMitades[1]=a[1];
				for (int i=0; i<a.length/2; i++) {
					for (int j=a.length/2; j<a.length; j++) {
						double distancia=a[i].distancia(a[j]);
						if (distancia<distanciaMinima) {
							distanciaMinima=distancia;
							minUnionDosMitades[0]=a[i];
							minUnionDosMitades[1]=a[j];
						}
					}
				}
			}
		};
		primeraMitad.start();
		segundaMitad.start();
		terceraMitad.start();
		
		primeraMitad.join();
		segundaMitad.join();
		terceraMitad.join();
		
		
		return min3(minPrimeraMitad,minSegundaMitad,minUnionDosMitades);
	}
	
		
	private static Punto[] min3(Punto[] a, Punto[] b, Punto[] c) {
		double distanciaA=a[0].distancia(a[1]);
		double distanciaB=b[0].distancia(b[1]);
		double distanciaC=c[0].distancia(c[1]);
		
		if (distanciaA<=distanciaB && distanciaA<=distanciaC)
			return a;
		if (distanciaB<=distanciaA && distanciaB<=distanciaC)
			return b;
		return c;
				
	}
	
	private static Punto[] hallarMinDistanciaRec(Punto[] a, int ini, int fin) {
		Punto[] resultado=null;
		// Caso no recursivo
		if (fin-ini==1) {
			resultado=new Punto[2];
			resultado[0]=a[ini];
			resultado[1]=a[fin];
			return resultado;
		}
		else if (fin==ini) {
			resultado=new Punto[1];
			resultado[0]=a[ini];
			return resultado;
		}
		
		
		// Caso recursivo
		int medio=(ini+fin)/2;
		Punto[] primeraMitad=hallarMinDistanciaRec(a,ini,medio);
		Punto[] segundaMitad=hallarMinDistanciaRec(a,medio,fin);
		
				
		// Combinado
		Punto[] minCombinado=new Punto[2];
		minCombinado[0]=primeraMitad[0];
		minCombinado[1]=segundaMitad[0];
		double distanciaMimina=minCombinado[0].distancia(minCombinado[1]);
		for (int i=ini; i<medio; i++) {
			for (int j=medio; j<=fin; j++) {
				double distancia=a[i].distancia(a[j]);
				if (distancia<distanciaMimina) {
					minCombinado[0]=a[i];
					minCombinado[1]=a[j];
				}
				
			}
		}
		
		return min3Rec(primeraMitad,segundaMitad,minCombinado);
		
		
	}
	
	private static Punto[] min3Rec(Punto[] a, Punto[] b, Punto[] c) {
		double distanciaA=a.length==1?-1.0:a[0].distancia(a[1]);
		double distanciaB=b.length==1?-1.0:b[0].distancia(b[1]);
		double distanciaC=c.length==1?-1.0:c[0].distancia(c[1]);
		
		if (distanciaA<=distanciaB && distanciaA<=distanciaC)
			return a;
		if (distanciaB<=distanciaA && distanciaB<=distanciaC)
			return b;
		return c;
				
	}
	
	
	private static class MinDistanciaAction extends RecursiveAction{
		
		private Punto[] a;
		private int ini;
		private int fin;
		Punto[] resultado;
		
		public MinDistanciaAction(Punto[] a, int ini, int fin) {
			this.a=a;
			this.ini=ini;
			this.fin=fin;
		}
		

		@Override
		protected void compute() {
			// TODO Auto-generated method stub
			// Caso no recursivo
			if (fin-ini==1) {
				resultado=new Punto[2];
				resultado[0]=a[ini];
				resultado[1]=a[fin];
			}
			else if (fin==ini) {
				resultado=new Punto[1];
				resultado[0]=a[ini];
			}
			else 
			{
			
				// Caso recursivo
				int medio=(ini+fin)/2;		
				MinDistanciaAction primeraMitad=new MinDistanciaAction(a,ini,medio);
				MinDistanciaAction segundaMitad=new MinDistanciaAction(a,medio,fin);
				primeraMitad.fork();
				segundaMitad.compute();
				primeraMitad.join();
					
				// Combinado
				Punto[] minCombinado=new Punto[2];
				minCombinado[0]=primeraMitad.resultado[0];
				minCombinado[1]=segundaMitad.resultado[0];
				double distanciaMimina=minCombinado[0].distancia(minCombinado[1]);
				for (int i=ini; i<medio; i++) {
					for (int j=medio; j<=fin; j++) {
						double distancia=a[i].distancia(a[j]);
						if (distancia<distanciaMimina) {
							minCombinado[0]=a[i];
							minCombinado[1]=a[j];
						}
					
					}
				}
						
				resultado=min3Rec(primeraMitad.resultado,segundaMitad.resultado,minCombinado);
			}

			
			
		}
		
	}
	
 	
	private static void printResult(String name, 
			long timeNanos, Punto[] resultado) {
			System.out.printf(" %s completado en %8.3f milisegundos con distancia(%s,%s) %8.5f\n",
				name,timeNanos/1e6,resultado[0],resultado[1],resultado[0].distancia(resultado[1]));			
	}
	
	private static Punto[] createArray(final int N) {
		final Punto[] input=new Punto[N];
		final Random rand=new Random(314);
		for (int i=0; i<N; i++) {
			int X=rand.nextInt(100);
			int Y=rand.nextInt(100);
			input[i]=new Punto(X,Y);
		}		
		return input; 
	}

}
