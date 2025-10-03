package proyecto;

import RMI.Conexion;
import RMI.ConexionInterfaz;
import herramientas.EspectroExecutor;
import herramientas.EspectroForkJoinTask;
import herramientas.EspectroSecuencial;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
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
            espectroText,
            ipTextArea;

    private JButton carpetaButton,
            espectroButton,
            iniciarServidor,
            conectar;

    private File carpetaFile;

    private JLabel[] tiempos;

    private JLabel mensajes,
            mensajesServidor;

    private JButton secuencial,
            threadPool,
            executor;

    private Conexion servidor;

    private ConexionInterfaz cliente;

    private boolean isServidor = false;

    private String mensaje;

    private boolean modificacion = true;

    public Proyecto() {
        setTitle("Orcado");
        setSize(1000, 700);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        mensajes = new JLabel("");
        mensajes.setBounds(10, 550, 200, 40);
        add(mensajes);

        mensajesServidor = new JLabel("prueba");
        mensajesServidor.setBounds(10, 30, 700, 500);
        add(mensajesServidor);

        ipTextArea = new JTextArea();
        ipTextArea.setBounds(180, 500, 100, 30);
        ipTextArea.setText("localhost");
        add(ipTextArea);

        conectar = new JButton("Conectar");
        conectar.setBounds(290, 500, 100, 30);
        conectar.addActionListener((e) -> {
            try {
                Registry registro = LocateRegistry.getRegistry(ipTextArea.getText(), 300);
                cliente = (ConexionInterfaz) (registro.lookup("conexion"));
            } catch (RemoteException ex) {
                Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
            }
            isServidor = false;
            iniciarServidor.setEnabled(false);
            ipTextArea.setEnabled(false);
            conectar.setEnabled(false);
            mensajes.setText("conectado");
            actualizar();
        });
        add(conectar);

        iniciarServidor = new JButton("Inciar Servidor");
        iniciarServidor.setBounds(10, 500, 150, 30);
        iniciarServidor.addActionListener((e) -> {
            isServidor = true;
            try {
                servidor = new Conexion();
            } catch (RemoteException ex) {
                Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
            }
            iniciarServidor.setEnabled(false);
            ipTextArea.setEnabled(false);
            conectar.setEnabled(false);
            mensajes.setText("Servidor Iniciado");
            actualizar();
        });
        add(iniciarServidor);

        carpetaChooser = new JFileChooser();
        carpetaChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

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

    private void actualizar() {
        new Thread(() -> {
            String mensajeAux = "";
            while (true) {
                try {
                    if (mensajeAux != getMensaje()) {
                        mensajeAux = getMensaje();
                        mensajesServidor.setText(getMensaje());
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (isServidor && servidor.getContador() == 3 && modificacion != false) {
                    carpetaFile = new File(servidor.getRuta());
                    threadPoolClick();
                    executorButtonClick();
                    secuecialEspectro();
                    modificacion = false;
                }
            }
        }).start();
    }

    private void espectroButtonClick() throws IOException {
        int respuesta = espectroChooser.showOpenDialog(this);

        if (respuesta == JFileChooser.APPROVE_OPTION) {

            new EspectroGrama(espectroChooser.getSelectedFile().getPath());

        }
    }

    private void threadPoolClick() {

        if (isServidor) {

        } else {
            try {
                cliente.anadirAudio(carpetaFile.getName() + ": Thread Pool", carpetaFile.getPath());
            } catch (RemoteException ex) {
                Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        borrarCarpeta(new File(System.getProperty("user.dir") + "\\src\\espectros\\"));
        borrarCarpeta(new File(System.getProperty("user.dir") + "\\src\\src\\"));
        long start = System.currentTimeMillis();

        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        EspectroForkJoinTask task = new EspectroForkJoinTask(carpetaFile);
        pool.invoke(task);

        long end = System.currentTimeMillis();
        tiempos[1].setText((end - start) + "ms");
    }

    private void executorButtonClick() {

        if (isServidor) {

        } else {
            try {
                cliente.anadirAudio(carpetaFile.getName() + ": executor", carpetaFile.getPath());
            } catch (RemoteException ex) {
                Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        borrarCarpeta(new File(System.getProperty("user.dir") + "\\src\\espectros\\"));
        borrarCarpeta(new File(System.getProperty("user.dir") + "\\src\\src\\"));
        long start = System.currentTimeMillis();

        int hilos = Runtime.getRuntime().availableProcessors() * 2;
        ExecutorService executor = Executors.newFixedThreadPool(hilos);

        executor.submit(new EspectroExecutor(carpetaFile.getPath() ));

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

        if (isServidor) {

        } else {
            try {
                cliente.anadirAudio(carpetaFile.getName() + ": secuencial", carpetaFile.getPath());
            } catch (RemoteException ex) {
                Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }

        long start = System.currentTimeMillis();
        try {
            sleep(4000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Proyecto.class.getName()).log(Level.SEVERE, null, ex);
        }
        borrarCarpeta(new File(System.getProperty("user.dir") + "\\src\\espectros\\"));
        borrarCarpeta(new File(System.getProperty("user.dir") + "\\src\\src\\"));

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
                    if (archivo.isFile() ) {
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

    private String getMensaje() throws RemoteException {
        if (isServidor) {
            return servidor.getMensaje();
        } else {
            return cliente.getMensaje();
        }
    }

    public static void main(String[] args) {
        new Proyecto();
        new Proyecto();
        new Proyecto();
        new Proyecto();
    }

}
