package proyecto;

import herramientas.EspectroExecutor;
import herramientas.EspectroForkJoinTask;
import herramientas.EspectroSecuencial;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Proyecto extends JFrame {

    private JFileChooser carpetaChooser,
            espectroChooser;

    private JTextArea carpetaText,
            espectroText;

    private JButton carpetaButton,
            espectroButton;

    private File carpetaFile;

    private JLabel[] tiempos;

    private JButton secuencial,
            threadPool,
            executor;

    public Proyecto() {
        setTitle("Orcado");
        setSize(1000, 700);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        carpetaChooser = new JFileChooser();
        carpetaChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        espectroChooser = new JFileChooser(System.getProperty("user.dir") + "\\src\\espectros\\");

        tiempos = new JLabel[3];

        espectroText = new JTextArea("Visualizar espectro: ");
        espectroText.setEnabled(false);
        espectroText.setBounds(20, 200, 500, 20);
        add(espectroText);

        espectroButton = new JButton("Seleccionar Carpeta");
        espectroButton.setBounds(530, 200, 200, 20);
        espectroButton.addActionListener((e) -> {
            try {
                espectroButtonClick();
            } catch (IOException ex) {
                Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        add(espectroButton);

        carpetaText = new JTextArea();
        carpetaText.setBounds(20, 50, 500, 20);
        add(carpetaText);

        carpetaButton = new JButton("Seleccionar Carpeta");
        carpetaButton.setBounds(530, 50, 200, 20);
        carpetaButton.addActionListener((e) -> {
            carpetaButtonClick();
        });
        add(carpetaButton);

        secuencial = new JButton("Secuencial");
        secuencial.setBounds(20, 120, 100, 30);
        secuencial.addActionListener((e) -> {
            secuecialEspectro();
        });
        add(secuencial);

        threadPool = new JButton("Thread Pool");
        threadPool.setBounds(140, 120, 100, 30);
        threadPool.addActionListener((e) -> {
            threadPoolClick();
        });
        add(threadPool);

        executor = new JButton("Executor");
        executor.setBounds(270, 120, 100, 30);
        executor.addActionListener((e) -> {
            executorButtonClick();
        });
        add(executor);

        for (int i = 0; i < 3; i++) {
            tiempos[i] = new JLabel("");
            tiempos[i].setBounds(50 + (i * 120), 100, 100, 20);
            add(tiempos[i]);
        }

        setVisible(true);
    }

    private void espectroButtonClick() throws IOException {
        int respuesta = espectroChooser.showOpenDialog(this);

        if (respuesta == JFileChooser.APPROVE_OPTION) {

            new EspectroGrama(espectroChooser.getSelectedFile().getPath());

        }
    }
    
    private void threadPoolClick(){
        borrarCarpeta(new File(System.getProperty("user.dir") + "\\src\\espectros\\"));
        borrarCarpeta(new File(System.getProperty("user.dir") + "\\src\\src\\"));
        long start = System.currentTimeMillis();

        File[] files = carpetaFile.listFiles();
        if (files == null) {
            return;
        }

        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        EspectroForkJoinTask task = new EspectroForkJoinTask(Arrays.asList(files));
        pool.invoke(task);

        long end = System.currentTimeMillis();
        tiempos[1].setText((end - start) + "ms");
    }

    private void executorButtonClick() {
        borrarCarpeta(new File(System.getProperty("user.dir") + "\\src\\espectros\\"));
        borrarCarpeta(new File(System.getProperty("user.dir") + "\\src\\src\\"));
        long start = System.currentTimeMillis();
        
        int hilos = Runtime.getRuntime().availableProcessors() * 2;
        ExecutorService executor = Executors.newFixedThreadPool(hilos);

        

        for (String nombre : carpetaFile.list()) {
            if (nombre.endsWith(".m4a")) {
                System.out.println(carpetaFile.getPath() + "\\" + nombre);
                executor.submit(new EspectroExecutor(carpetaFile.getPath() + "\\" + nombre));
            }
        }

        executor.shutdown();
        
        try {
            if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                System.err.println("Executor no terminó en 1 hora.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        long tiempofinal = System.currentTimeMillis();

        tiempos[2].setText((tiempofinal - start) + "ms");
    }

    private void secuecialEspectro() {
        
        borrarCarpeta(new File(System.getProperty("user.dir") + "\\src\\espectros\\"));
        borrarCarpeta(new File(System.getProperty("user.dir") + "\\src\\src\\"));
        
        long start = System.currentTimeMillis();

        try {
            EspectroSecuencial.convertM4AToRaw(carpetaFile);
        } catch (IOException ex) {
            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
        }

        long tiempofinal = System.currentTimeMillis();

        tiempos[0].setText((tiempofinal - start) + "ms");

    }
    
    private void borrarCarpeta(File carpeta) {
        if (carpeta.exists() && carpeta.isDirectory()) {
            File[] archivos = carpeta.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    if (archivo.isFile()) {
                        archivo.delete();
                    }
                }
            }
        }
    }

    private void carpetaButtonClick() {
        int respuesta = carpetaChooser.showOpenDialog(this);

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            carpetaFile = carpetaChooser.getSelectedFile();

            carpetaText.setText(carpetaFile.getPath());
        }
    }

    public static void main(String[] args) {
        new Proyecto();
    }

}
