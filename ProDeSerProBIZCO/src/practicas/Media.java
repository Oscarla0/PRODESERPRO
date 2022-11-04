package practicas;

import java.util.Random;

import java.util.Random;



public class Media {
	
	private final static int N=100_000;
	public double media=0;
	public int[] numero;
	public double resultado;
	final int[] a=createArray(N);
	//int[] b = {1,2,3,4,5};
	
	public double calcularMedia(int[] numeros) {
		//int contador = 0;
		
		for(int i = 0; i< numeros.length;i++) {
			media= media + numeros[i];
			//contador++;
		}
		//resultado = media/i;
		
//System.out.println("la media es : "+media);
		
		return media / numeros.length;
	}
	public double calcularMediaRecur(double[]numeros, int ini, int fin) {
		
		
		/*Caso no recursivo*/
		if((fin - ini) == 2) {
			
			double mediaNoRecu = 0;
			
			for(int i = ini;i<= fin; i++) {
				mediaNoRecu= mediaNoRecu + numeros[i];
			}
			return mediaNoRecu/(fin-ini+1);
		}
		/*
		else if ((fin - ini) == 1){
			return ((numeros[ini]+numeros[fin]/2));
		}
		
		else if (fin == ini){
			return numeros[ini];
		}*/
		
		
		/* recursivo*/
		int mitad = (ini+fin)/2;
		
		double media1 = calcularMediaRecur(numeros,ini,mitad);
		double media2 = calcularMediaRecur(numeros,mitad,fin);
		
		/*combinad*/
		
		return (media1+media2)/2;
		
	}
	
	private static int[] createArray(final int N) {
		final int[] input=new int[N];
		final Random rand=new Random(314);
		for (int i=0; i<N; i++) {
			int X=rand.nextInt(100);
			input[i]=X;
		}		
		
		return input; 
	}
	
	
	
	public static void main(String[] args) {
		Media pr1 = new Media();
		/*int [] hola = pr1.createArray(N);
		
		
//System.out.println
(hola);
		for(int i = 0; i< hola.length;i++) {
			System.out.println(hola[i]);
		}
		double resultado =pr1.calcularMedia(hola); 
		pr1.calcularMedia(hola);
		 
//System.out.println
(pr1.calcularMedia(hola));
		*/
		double[] b = {1,2,3,4,5};
		//double res = pr1.calcularMedia(b);
		double res2 = pr1.calcularMediaRecur(b, 0, b.length-1);
		
//System.out.println(res);
		System.out.println(res2);
		System.out.println();
	}

} 