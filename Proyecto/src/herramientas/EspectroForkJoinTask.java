package herramientas;

import java.io.File;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EspectroForkJoinTask extends RecursiveAction {

    private static final int UMBRAL = 10; // Partición mínima (puedes ajustar)
    private final File archivos;

    public EspectroForkJoinTask(File archivos) {
        this.archivos = archivos;
    }

    @Override
    protected void compute() {
        
            // Caso base: procesar secuencialmente estos archivos
            
                if (archivos.getName().endsWith(".m4a")) {
                    new EspectroExecutor(archivos.getPath()).run(); // Reaprovechamos tu Callable
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(EspectroForkJoinTask.class.getName()).log(Level.SEVERE, null, ex);
                }
            
        
    }
}