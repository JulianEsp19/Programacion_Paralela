package mergesort;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class MergeSort extends JFrame{
    
    JLabel originalLabel,
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
    
    public MergeSort(){
        setTitle("Merge Sort");
        setSize(900, 500);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        originalLabel = new JLabel("111111111111111111111111111111111111111111111");
        originalLabel.setBounds(20, 20, 500, 40);
        add(originalLabel);
        
        acomodadoLabel = new JLabel("11111111111111111111111111111111111111111111");
        acomodadoLabel.setBounds(20, 100, 500, 40);
        add(acomodadoLabel);
        
        limpiarButton = new JButton("Limpiar");
        limpiarButton.setBounds(750, 50, 100, 30);
        limpiarButton.addActionListener((e) -> { limpiar(); });
        add(limpiarButton);
        
        borrarButton = new JButton("Borrar");
        borrarButton.setBounds(750, 100, 100, 30);
        borrarButton.addActionListener((e) -> { borrar(); });
        add(borrarButton);
        
        iniciarButton = new JButton("Iniciar");
        iniciarButton.setBounds(750, 150, 100, 30);
        iniciarButton.addActionListener((e) -> { generarArray(); });
        add(iniciarButton);

        cantidadDatos = new JTextArea();
        cantidadDatos.setBounds(750, 200, 100, 30);
        add(cantidadDatos);
        
        
        secuencialButton = new JButton("Secuencial");
        secuencialButton.setBounds(100, 400, 100, 30);
        add(secuencialButton);
        
        forkJoinButton = new JButton("Fork/Join");
        forkJoinButton.setBounds(250, 400, 100, 30);
        add(forkJoinButton);
        
        executorButton = new JButton("Executor");
        executorButton.setBounds(400, 400, 100, 30);
        add(executorButton);
        
        tiempoSecuencial = new JLabel("1");
        tiempoSecuencial.setBounds(100, 350, 100, 30);
        add(tiempoSecuencial);
        
        tiempoForkJoin = new JLabel("1");
        tiempoForkJoin.setBounds(250, 350, 100, 30);
        add(tiempoForkJoin);
        
        tiempoExecutor = new JLabel("1");
        tiempoExecutor.setBounds(400, 350, 100, 30);
        add(tiempoExecutor);
        
        setVisible(true);
    }
    
    private void limpiar(){
        originalLabel.setText("");
        acomodadoLabel.setText("");
        cantidadDatos.setText("");
        tiempoExecutor.setText("");
        tiempoForkJoin.setText("");
        tiempoSecuencial.setText("");
    }
    
    private void borrar(){
        array = null;
    }
    
    private void generarArray() {
        int size = Integer.parseInt(cantidadDatos.getText());
        
        
        
        int[] array = new int[size];
        
        for (int i = 0; i < size; i++) {
            array[i] = i+1;
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
        
        for (int num : array) {
            originalLabel.setText(originalLabel.getText()+num+",");
        }
        
        this.array = array;
    }

    public static void main(String[] args) {
        new MergeSort();
    }
    
}
