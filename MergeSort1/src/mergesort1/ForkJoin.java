package mergesort1;

import java.util.concurrent.RecursiveAction;

public class ForkJoin extends RecursiveAction {

    int[] A;
    int izq, der;

    public ForkJoin(int[] A, int izq, int der) {
        this.A = A;
        this.izq = izq;
        this.der = der;
    }

    @Override
    protected void compute() {
        if (izq < der) {
            int m = (izq + der) / 2;

            ForkJoin izquierda = new ForkJoin(A, izq, m);
            ForkJoin derecha = new ForkJoin(A, m + 1, der);
            invokeAll(izquierda, derecha);
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
