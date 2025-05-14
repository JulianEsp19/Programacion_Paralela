package herramientas;

import java.io.File;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EspectroForkJoinTask extends RecursiveAction {

    private static final int UMBRAL = 10; // Partición mínima (puedes ajustar)
    private final List<File> archivos;

    public EspectroForkJoinTask(List<File> archivos) {
        this.archivos = archivos;
    }

    @Override
    protected void compute() {
        if (archivos.size() <= UMBRAL) {
            // Caso base: procesar secuencialmente estos archivos
            for (File archivo : archivos) {
                if (archivo.getName().endsWith(".m4a")) {
                    new EspectroExecutor(archivo.getPath()).run(); // Reaprovechamos tu Callable
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(EspectroForkJoinTask.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            // Dividir en dos mitades
            int mid = archivos.size() / 2;
            List<File> left = archivos.subList(0, mid);
            List<File> right = archivos.subList(mid, archivos.size());

            invokeAll(new EspectroForkJoinTask(left), new EspectroForkJoinTask(right));
        }
    }
}