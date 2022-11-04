package practicas;

import java.util.Random;

public class SerieMaxima {
	
	private final static int N=5_000;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	
		final int[] a=createArray(N);
		long startTime=0;
		long timeNanos=0;
		int suma=0;
		int[] subSerieMaxima=null;
		
		
		
		//Algoritmo O(n)
		startTime=System.nanoTime();
		subSerieMaxima=serieMaxima3(a);
		timeNanos=System.nanoTime()-startTime;
		printResult("Algoritmo O(n):",timeNanos,suma);

		//Algoritmo O(nlogn)
		startTime=System.nanoTime();
		subSerieMaxima=serieMaxRec(a,0,a.length-1);
		timeNanos=System.nanoTime()-startTime;
		printResult("Algoritmo Recursivo):",timeNanos,suma);

		
		//Algoritmo O(n2)
		startTime=System.nanoTime();
		subSerieMaxima=serieMaxima2(a);
		timeNanos=System.nanoTime()-startTime;
		printResult("Algoritmo O(n2):",timeNanos,suma);
		
		
		
		//Algoritmo O(n3)
		startTime=System.nanoTime();
		subSerieMaxima=serieMaxima(a);
		timeNanos=System.nanoTime()-startTime;
		printResult("Algoritmo O(n3):",timeNanos,suma);
			
	}
	
	public static int[] serieMaxima(int[] serieInicial) {
		int[] resultado=null;
		int sumaMaxima=0;
		int inicio=0,fin=0;
		for (int i=0; i<serieInicial.length; i++) {
			for (int j=i; j< serieInicial.length; j++) {
				int suma=0;
				for (int k=i; k<=j; k++) {
					suma+=serieInicial[k];
				}
				if (suma>sumaMaxima) {
					sumaMaxima=suma;
					inicio=i;
					fin=j;
				}
			}
			
		}		
		System.out.println("Suma"+sumaMaxima);
		resultado=mostrarResultado(serieInicial,inicio, fin);

		return resultado;
	}
	
	
	public static int[] serieMaxima2(int[] serieInicial) {
		int[] resultado=null;
		int sumaMaxima=0;
		int inicio=0,fin=0;
		for (int i=0; i<serieInicial.length; i++) {
			int suma=0;
			for (int j=i; j< serieInicial.length; j++) {
				suma+=serieInicial[j]; 
				if (suma>sumaMaxima) {
					sumaMaxima=suma;
					inicio=i;
					fin=j;
				}
			}
			
		}		
		System.out.println("Suma"+sumaMaxima);
		resultado=mostrarResultado(serieInicial,inicio, fin);

		return resultado;
	}
	
	
	public static int[] serieMaxima3(int[] serieInicial) {
		int[] resultado=null;
		int sumaMaxima=0;
		int suma=0;
		int inicio=0,fin=0;
		for (int i=0,j=0; j<serieInicial.length; j++) {
			suma+=serieInicial[j]; 
			if (suma>sumaMaxima) {
					sumaMaxima=suma;
					inicio=i;
					fin=j;
			}
			else if (suma<0) {
				i=j+1; 
				suma=0;
			}
						
		}		
		System.out.println("Suma"+sumaMaxima);
		resultado=mostrarResultado(serieInicial,inicio, fin);

		return resultado;
	}

	
	public static int[] mostrarResultado(int[] serieOriginal,
		int inicio, int fin) {
		int[] resultado=new int[fin-inicio+1];
		resultado=new int[fin-inicio+1];
		for (int i=inicio, j=0; i<=fin; i++,j++) {			
			resultado[j]=serieOriginal[i];
		}
		return resultado;
	}
	
	
	public static int[] serieMaxRec(int[] a, int left, int right) {
		
		int center=(left+right)/2;
		int sumaMaxIzd=0;
		int sumaIzd=0;
		int sumaMaxDcho=0;
		int sumaDcha=0;
		int inicio=0;
		int fin=0;
		
		//Caso no recursivo
		if (right==left) {
			inicio=left;
			fin=right;
			int[] casoNoRec=new int[1];
			casoNoRec[0]=a[left]<0?0:a[left];
			return casoNoRec;
		}
		
		int[] maxSerieLadoLeft=serieMaxRec(a,left,center);
		int[] maxSerieLadoRight=serieMaxRec(a,center+1,right);
		

		inicio=center;
		for (int i=center; i>=left; i--) {
			sumaIzd+=a[i];
			if (sumaIzd >sumaMaxIzd ) {
				sumaMaxIzd=sumaIzd;
				inicio=i;
			}
		}
		
		fin=center+1;
		for (int i=center+1; i<=right; i++) {
			sumaDcha+=a[i];
			if (sumaDcha >sumaMaxDcho ) {
				sumaMaxDcho=sumaDcha;
			}
			fin=i;
		}
		
		int[] subSerieMaxima=mostrarResultado(a,inicio,fin);

		
		return max3(maxSerieLadoLeft,maxSerieLadoRight,subSerieMaxima);
	}
	
	public static int[] max3(int[] a,  int[] b, int[] c) {
		int sumaA=0;
		int sumaB=0;
		int sumaC=0;
		
		for (int i=0; i<a.length; i++) {
			sumaA+=a[i];
		}
		for (int i=0; i<b.length; i++) {
			sumaB+=b[i];
		}

		for (int i=0; i<c.length; i++) {
			sumaC+=c[i];
		}

		if (sumaA>=sumaB && sumaA>=sumaC) 
			return a;
		else if (sumaB>=sumaA && sumaB>=sumaC)
			return b;
		else
			return c;
				
	}
	

	private static void printResult(String name, 
			long timeNanos, double sum) {
			System.out.printf(" %s completado en %8.3f milisegundos con suma %8.5f\n",
				name,timeNanos/1e6,sum);			
	}
	
	private static int[] createArray(final int N) {
		final int[] input=new int[N];
		final Random rand=new Random(314);
		for (int i=0; i<N; i++) {
			input[i]=rand.nextInt(100);
		}		
		return input; 
	}

}
