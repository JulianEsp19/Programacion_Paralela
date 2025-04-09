package mergesort1;

import java.awt.BorderLayout;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 *
 * @author Julian
 */
public class Main extends JFrame {

    JTextArea originalLabel,
            acomodadoLabel;

    JButton secuencialButton,
            forkJoinButton,
            executorButton;

    JLabel tiempoSecuencial,
            tiempoForkJoin,
            tiempoExecutor;

    JButton limpiarButton,
            borrarButton,
            iniciarButton;

    JTextArea cantidadDatos;

    private int[] array;

    private int[] copia;

    public Main() {
        setTitle("Merge Sort");
        setSize(900, 500);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        originalLabel = new JTextArea("");
        originalLabel.setBounds(20, 20, 500, 150);

        JScrollPane scroll = new JScrollPane(originalLabel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);
        originalLabel.setLineWrap(true);
        originalLabel.setWrapStyleWord(true);
        originalLabel.setEditable(false);

        add(originalLabel);

        acomodadoLabel = new JTextArea("");
        acomodadoLabel.setBounds(20, 180, 500, 150);

        JScrollPane scroll1 = new JScrollPane(acomodadoLabel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll1, BorderLayout.CENTER);
        acomodadoLabel.setLineWrap(true);
        acomodadoLabel.setWrapStyleWord(true);
        acomodadoLabel.setEditable(false);

        add(acomodadoLabel);

        limpiarButton = new JButton("Limpiar");
        limpiarButton.setBounds(750, 50, 100, 30);
        limpiarButton.addActionListener((e) -> {
            limpiar();
        });
        add(limpiarButton);

        borrarButton = new JButton("Borrar");
        borrarButton.setBounds(750, 100, 100, 30);
        borrarButton.addActionListener((e) -> {
            borrar();
        });
        add(borrarButton);

        iniciarButton = new JButton("Iniciar");
        iniciarButton.setBounds(750, 150, 100, 30);
        iniciarButton.addActionListener((e) -> {
            generarArray();
        });
        add(iniciarButton);

        cantidadDatos = new JTextArea();
        cantidadDatos.setBounds(750, 200, 100, 30);
        add(cantidadDatos);

        secuencialButton = new JButton("Secuencial");
        secuencialButton.setBounds(100, 400, 100, 30);
        secuencialButton.addActionListener((e) -> {
            mergeSortSecuencial();
        });
        add(secuencialButton);

        forkJoinButton = new JButton("Fork/Join");
        forkJoinButton.setBounds(250, 400, 100, 30);
        forkJoinButton.addActionListener((e) -> {
            mergeSortForkJoin();
        });
        add(forkJoinButton);

        executorButton = new JButton("Executor");
        executorButton.setBounds(400, 400, 100, 30);
        executorButton.addActionListener((e) -> {
            mergeSortExecutor();
        });
        add(executorButton);

        tiempoSecuencial = new JLabel("");
        tiempoSecuencial.setBounds(100, 350, 100, 30);
        add(tiempoSecuencial);

        tiempoForkJoin = new JLabel("");
        tiempoForkJoin.setBounds(250, 350, 100, 30);
        add(tiempoForkJoin);

        tiempoExecutor = new JLabel("");
        tiempoExecutor.setBounds(400, 350, 100, 30);
        add(tiempoExecutor);

        setVisible(true);
    }

    private void limpiar() {
        originalLabel.setText("");
        acomodadoLabel.setText("");
        cantidadDatos.setText("");
        tiempoExecutor.setText("");
        tiempoForkJoin.setText("");
        tiempoSecuencial.setText("");
    }

    private void borrar() {
        array = null;
    }
    
    private void mergeSortExecutor() {
        copia = Arrays.copyOf(array, array.length);
        long tiempo = System.nanoTime();
        
        ExecutorService executor = Executors.newFixedThreadPool(1);

        // Ejecutar MergeSort con Executor
        
        executor.submit(new Executor(copia, 0, copia.length-1));

        // Esperar a que se complete
        executor.shutdown();
        
        long tiempoFinal = System.nanoTime();
        tiempoExecutor.setText(String.valueOf((tiempoFinal - tiempo) / 1_000_000.00) + "ms");
        
        originalLabel.setText(Arrays.toString(array));
        acomodadoLabel.setText(Arrays.toString(copia));
    }

    private void mergeSortForkJoin() {
        copia = Arrays.copyOf(array, array.length);
        long tiempo = System.nanoTime();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        
        forkJoinPool.invoke(new ForkJoin(copia, 0, copia.length- 1));

        long tiempoFinal = System.nanoTime();
        tiempoForkJoin.setText(String.valueOf((tiempoFinal - tiempo) / 1_000_000.00) + "ms");
        
        originalLabel.setText(Arrays.toString(array));
        acomodadoLabel.setText(Arrays.toString(copia));
    }

    private void mergeSortSecuencial() {
        copia = Arrays.copyOf(array, array.length);
        long tiempo = System.nanoTime();

        Secuencial.mergeSort(copia);

        long tiempoFinal = System.nanoTime();
        tiempoSecuencial.setText(String.valueOf((tiempoFinal - tiempo) / 1_000_000.00) + "ms");
        
        originalLabel.setText(Arrays.toString(array));
        acomodadoLabel.setText(Arrays.toString(copia));
    }

    private void generarArray() {
        int size = Integer.parseInt(cantidadDatos.getText());

        int[] array = new int[size];

        for (int i = 0; i < size; i++) {
            array[i] = i + 1;
        }

        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            // Intercambiar elementos array[i] y array[j]
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        originalLabel.setText("");

        originalLabel.setText(Arrays.toString(array));

        this.array = array;
        copia = Arrays.copyOf(array, array.length);
    }

    public static void main(String[] args) {
        new Main();
    }

}
