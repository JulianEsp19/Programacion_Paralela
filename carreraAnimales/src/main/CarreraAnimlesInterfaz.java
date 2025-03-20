package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CarreraAnimlesInterfaz extends JFrame {

    JPanel carriles[];

    JButton reiniciarButton,
            iniciarButton;

    JLabel animal1,
            animal2,
            animal3;
    
    MovimientoAnimal movAnimal1,
            movAnimal2,
            movAnimal3;
    
    private volatile int posicion = 0;

    public CarreraAnimlesInterfaz() {
        setTitle("Carrera de animales 2");
        setSize(900, 400);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        //Animales (JLabel)
        ImageIcon imagenAnimal1 = new ImageIcon("src/Src/jirafa.png");
        Image imagen = imagenAnimal1.getImage();
        imagen = imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        imagenAnimal1 = new ImageIcon(imagen);
        animal1 = new JLabel(imagenAnimal1);
        animal1.setBounds(0, 0, 130, 100);
        animal1.setFont(new Font("Arial", Font.BOLD, 20));
        add(animal1);
        
        
        ImageIcon imagenAnimal2 = new ImageIcon("src/Src/leon.png");
        Image imagen1 = imagenAnimal2.getImage();
        imagen1 = imagen1.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        imagenAnimal2 = new ImageIcon(imagen1);
        animal2 = new JLabel(imagenAnimal2);
        animal2.setBounds(0, 125, 130, 100);
        animal2.setFont(new Font("Arial", Font.BOLD, 20));
        add(animal2);
        
        ImageIcon imagenAnimal3 = new ImageIcon("src/Src/guepardo.png");
        Image imagen3 = imagenAnimal3.getImage();
        imagen3 = imagen3.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        imagenAnimal3 = new ImageIcon(imagen3);
        animal3 = new JLabel(imagenAnimal3);
        animal3.setBounds(0, 250, 130, 100);
        animal3.setFont(new Font("Arial", Font.BOLD, 20));
        add(animal3);
        

        //carriles
        carriles = new JPanel[3];
        for (int i = 0; i < 3; i++) {
            carriles[i] = new JPanel();
            carriles[i].setBounds(0, i * 125, 600, 100);
            carriles[i].setBackground(Color.GRAY);
            add(carriles[i]);
        }

        //Botones
        reiniciarButton = new JButton("Reiniciar");
        reiniciarButton.setBounds(750, 20, 100, 25);
        reiniciarButton.addActionListener((e) -> {reiniciar();});
        add(reiniciarButton);

        iniciarButton = new JButton("Iniciar");
        iniciarButton.setBounds(750, 55, 100, 25);
        iniciarButton.addActionListener((e) -> {iniciarPrimero();});
        add(iniciarButton);


        setVisible(true);

    }
    
    private void reiniciar(){
        animal1.setLocation(0, animal1.getY());
        animal2.setLocation(0, animal2.getY());
        animal3.setLocation(0, animal3.getY());
    }
    
    private void iniciarPrimero(){
        movAnimal1 = new MovimientoAnimal(animal1, 60);
        movAnimal1.start();
        
        movAnimal2 = new MovimientoAnimal(animal2, 80);
        movAnimal2.start();
        
        movAnimal3 = new MovimientoAnimal(animal3, 130);
        movAnimal3.start();
    }

    public static void main(String[] args) {
        new CarreraAnimlesInterfaz();
    }

}