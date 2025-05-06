package mergesort1;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Executor implements Runnable {

    private int[] A;
    private int izq, der;

    public Executor(int[] A, int izq, int der) {
        this.A = A;
        this.izq = izq;
        this.der = der;
    }

    @Override
    public void run(){
        if (izq < der) {
            int m = (izq + der) / 2;

            // Recursivamente dividir el array
            Executor izquierda = new Executor(A, izq, m);
            Executor derecha = new Executor(A, m + 1, der);

            // Si la división es pequeña, no crear nuevos hilos, procesar de forma secuencial
            if (der - izq <= 100) {
                izquierda.run();
                derecha.run();
            } else {
                // Usamos ExecutorService para tareas más grandes
                ExecutorService executor = Executors.newFixedThreadPool(2);
                List<Future<Void>> futures = new ArrayList<>();
                futures.add((Future<Void>) executor.submit(izquierda));
                futures.add((Future<Void>) executor.submit(derecha));

                // Esperamos a que ambas tareas terminen
                for (Future<Void> future : futures) {
                    try {
                        future.get();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Executor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ExecutionException ex) {
                        Logger.getLogger(Executor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                executor.shutdown();
            }

            // Fusionamos los resultados
            merge(A, izq, m, der);
        }
        
    }

    public void merge(int[] A, int izq, int m, int der) {
        int n1 = m - izq + 1;
        int n2 = der - m;

        int[] izquierda = new int[n1];
        int[] derecha = new int[n2];

        System.arraycopy(A, izq, izquierda, 0, n1);
        System.arraycopy(A, m + 1, derecha, 0, n2);

        int i = 0, j = 0, k = izq;

        while (i < n1 && j < n2) {
            if (izquierda[i] <= derecha[j]) {
                A[k] = izquierda[i];
                i++;
            } else {
                A[k] = derecha[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            A[k] = izquierda[i];
            i++;
            k++;
        }

        while (j < n2) {
            A[k] = derecha[j];
            j++;
            k++;
        }
    }
}
