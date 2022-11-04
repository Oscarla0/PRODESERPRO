package deberesTEMA1;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Tarea1 {
	private final static int N = 1_500_000;
	static Punto[] hiloA = new Punto[N];
	static Punto[] hiloB = new Punto[N];

	public static void main(String[] args) throws InterruptedException {
		final Punto[] a = createArray(N);
		Punto[] Resultado = new Punto[N];
		long Empezar = 0;
		long TiemposNano = 0;

//Forma iterativa
		Empezar = System.nanoTime();
		Resultado = quitarRepetidosITERATIVO(a);
		TiemposNano = System.nanoTime() - Empezar;
		printResult("Algoritmo iterativo:", TiemposNano, Resultado);
		System.out.println("");

//				Hilo1 Hilo2
		Empezar = System.nanoTime();
		Resultado = quitarRepetidosHILOS(a);
		TiemposNano = System.nanoTime() - Empezar;
		printResult("Algoritmo iterativo con 2 hilos:", TiemposNano, Resultado);
		System.out.println("");
//            Recursiva
		Empezar = System.nanoTime();
		Resultado = quitarRepetidosRECURSIVO(a, 0, a.length - 1);
		TiemposNano = System.nanoTime() - Empezar;
		printResult("Algoritmo recursivo:", TiemposNano, Resultado);

// 				Fork

		Empezar = System.nanoTime();
		QuitarRepesnciaAction p = new QuitarRepesnciaAction(a, 0, a.length - 1);
		ForkJoinPool.commonPool().invoke(p);
		Resultado = p.Resultado;
		TiemposNano = System.nanoTime() - Empezar;
		printResult("Algoritmo recursivo API Fork/Join java :", TiemposNano, Resultado);

	}

//							ARRAY
	private static Punto[] createArray(final int N) {
		final Punto[] input = new Punto[N];
		final Random ram = new Random(1000);
		for (int i = 0; i < N; i++) {
			int X = ram.nextInt(1000);
			int Y = ram.nextInt(1000);
			input[i] = new Punto(X, Y);
		}
		return input;
	}

//Forma iterativa
	public static Punto[] quitarRepetidosITERATIVO(Punto[] a) {
		HashSet<Punto> Hash = new HashSet<Punto>(Arrays.asList(a));
		Punto[] result = new Punto[Hash.size()];
		return Hash.toArray(result);
	}

//CON DOS HILOS
	public static Punto[] quitarRepetidosHILOS(Punto[] a) throws InterruptedException {
		
		//			Primer
		Thread Hilo1 = new Thread() {
			public void run() {
				Punto[] input = new Punto[a.length / 2];
				input = Arrays.copyOfRange(a, 0, a.length / 2);
				HashSet<Punto> has = new HashSet<Punto>(Arrays.asList(input));
				Punto[] result = new Punto[has.size()];
				result = has.toArray(result);
				hiloA = result;
			}
		};
		//           Segundo
		Thread Hilo2 = new Thread() {
			public void run() {
				Punto[] input = new Punto[a.length / 2];
				input = Arrays.copyOfRange(a, a.length / 2, a.length);
				HashSet<Punto> has = new HashSet<Punto>(Arrays.asList(input));
				Punto[] result = new Punto[has.size()];
				result = has.toArray(result);
				hiloB = result;
			}
		};

		Hilo1.start();
		Hilo2.start();

		Hilo1.join();
		Hilo2.join();

		return unirPuntos(hiloA, hiloB);
	}
//						Repetidos
	private static Punto[] quitarRepetidosRECURSIVO(Punto[] a, int ini, int fin) {
		Punto[] Resultado = null;
// 					Recursivo`t
		if (fin - ini == 1) {
			Resultado = new Punto[2];
			Resultado[0] = a[ini];
			Resultado[1] = a[fin];
			return Resultado;
		} else if (fin == ini) {
			Resultado = new Punto[1];
			Resultado[0] = a[ini];
			return Resultado;
		}

// 						Recursivo
		int medio = (ini + fin) / 2;
		Punto[] primeraParte = quitarRepetidosRECURSIVO(a, ini, medio);
		Punto[] segundaParte = quitarRepetidosRECURSIVO(a, medio, fin);

		return unirPuntos(primeraParte, segundaParte);

	}

	private static class QuitarRepesnciaAction extends RecursiveAction {
		private Punto[] a;
		private int ini;
		private int fin;
		Punto[] Resultado;

		public QuitarRepesnciaAction(Punto[] a, int ini, int fin) {
			this.a = a;
			this.ini = ini;
			this.fin = fin;
		}

		@Override
		protected void compute() {
			if (fin - ini == 1) {
				Resultado = new Punto[2];
				Resultado[0] = a[ini];
				Resultado[1] = a[fin];

			} else if (fin == ini) {
				Resultado = new Punto[1];
				Resultado[0] = a[ini];

			} else {
				int medio = (ini + fin) / 2;
				QuitarRepesnciaAction primeraMitad = new QuitarRepesnciaAction(a, ini, medio);
				QuitarRepesnciaAction segundaMitad = new QuitarRepesnciaAction(a, medio, fin);
				primeraMitad.fork();
				segundaMitad.compute();
				primeraMitad.join();

				Resultado = unirPuntos(primeraMitad.Resultado, segundaMitad.Resultado);
			}

// 									Recursivo

		}

	}
//									UnirPuntos
	public static Punto[] unirPuntos(Punto[] a, Punto[] b) {

		HashSet<Punto> has = new HashSet<Punto>(Arrays.asList(a));

		
		
		for (int i = 0; i < b.length - 1; i++) {
			has.add(b[i]);
		}
		Punto[] result = new Punto[has.size()];
		return has.toArray(result);
	}

	
	private static void printResult(String name, long TiemposNano, Punto[] Resultado) {
		System.out.printf(" %s completado en %8.3f milisegundos con Resultado(%d) \n", name, TiemposNano / 1e6,
				Resultado.length);
	}

}