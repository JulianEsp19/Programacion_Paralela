package calculadora;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Calculadora extends JFrame implements ActionListener {

    JButton boton_1;
    JButton boton_2;
    JButton boton_3;
    JButton boton_4;
    JButton boton_5;
    JButton boton_6;
    JButton boton_7;
    JButton boton_8;
    JButton boton_9;
    JButton boton_0;
    JButton boton_mas;
    JButton boton_menos;
    JButton boton_multiplicar;
    JButton boton_dividir;
    JButton boton_igual;
    JButton boton_ac;

    JTextArea entrada;

    String valorAtorgar;

    Operaciones op;

    int numero1;
    int numero2;

    public Calculadora() {
        setTitle("Calculadora");
        setSize(600, 500);
        setLayout(null);

        op = new Implementacion();

        numero1 = 0;
        numero2 = 0;
        valorAtorgar = "";

        entrada = new JTextArea();
        entrada.setBounds(50, 20, 420, 50);
        add(entrada);

        boton_7 = new JButton("7");
        boton_7.setBounds(50, 100, 100, 20);
        boton_7.addActionListener((e) -> {
            agregarNumeroCelda(e);
        });
        add(boton_7);

        boton_4 = new JButton("4");
        boton_4.setBounds(50, 130, 100, 20);
        boton_4.addActionListener((e) -> {
            agregarNumeroCelda(e);
        });
        add(boton_4);

        boton_1 = new JButton("1");
        boton_1.setBounds(50, 160, 100, 20);
        boton_1.addActionListener((e) -> {
            agregarNumeroCelda(e);
        });
        add(boton_1);

        boton_0 = new JButton("0");
        boton_0.setBounds(50, 190, 100, 20);
        boton_0.addActionListener((e) -> {
            agregarNumeroCelda(e);
        });
        add(boton_0);

        boton_8 = new JButton("8");
        boton_8.setBounds(160, 100, 100, 20);
        boton_8.addActionListener((e) -> {
            agregarNumeroCelda(e);
        });
        add(boton_8);

        boton_9 = new JButton("9");
        boton_9.setBounds(270, 100, 100, 20);
        boton_9.addActionListener((e) -> {
            agregarNumeroCelda(e);
        });
        add(boton_9);

        boton_5 = new JButton("5");
        boton_5.setBounds(160, 130, 100, 20);
        boton_5.addActionListener((e) -> {
            agregarNumeroCelda(e);
        });
        add(boton_5);

        boton_2 = new JButton("2");
        boton_2.setBounds(160, 160, 100, 20);
        boton_2.addActionListener((e) -> {
            agregarNumeroCelda(e);
        });
        add(boton_2);

        boton_6 = new JButton("6");
        boton_6.setBounds(270, 130, 100, 20);
        boton_6.addActionListener((e) -> {
            agregarNumeroCelda(e);
        });
        add(boton_6);

        boton_3 = new JButton("3");
        boton_3.setBounds(270, 160, 100, 20);
        boton_3.addActionListener((e) -> {
            agregarNumeroCelda(e);
        });
        add(boton_3);

        boton_ac = new JButton("ac");
        boton_ac.setBounds(100, 400, 100, 20);
        boton_ac.addActionListener((e) -> {
            numero1 = 0;
            numero2 = 0;
            valorAtorgar = "";
            entrada.setText("");
        });
        add(boton_ac);

        boton_dividir = new JButton("/");
        boton_dividir.setBounds(380, 100, 100, 20);
        boton_dividir.addActionListener((e) -> {
            agregarValor(e);
        });
        add(boton_dividir);

        boton_multiplicar = new JButton("*");
        boton_multiplicar.setBounds(380, 130, 100, 20);
        boton_multiplicar.addActionListener((e) -> {
            agregarValor(e);
        });
        add(boton_multiplicar);

        boton_menos = new JButton("-");
        boton_menos.setBounds(380, 160, 100, 20);
        boton_menos.addActionListener((e) -> {
            agregarValor(e);
        });
        add(boton_menos);

        boton_mas = new JButton("+");
        boton_mas.setBounds(380, 190, 100, 20);
        boton_mas.addActionListener((e) -> {
            agregarValor(e);
        });
        add(boton_mas);

        boton_igual = new JButton("=");
        boton_igual.setBounds(380, 220, 100, 20);
        boton_igual.addActionListener((e) -> {
            operacion();
        });
        add(boton_igual);

        setVisible(true);
    }

    private void agregarNumeroCelda(ActionEvent e) {
        String valorCelda = entrada.getText();
        JButton boton = (JButton) e.getSource();

        valorCelda += boton.getText();
        entrada.setText(valorCelda);
    }

    private void agregarValor(ActionEvent e) {
        if (numero1 == 0) {
            JButton botonAux = (JButton) e.getSource();
            numero1 = Integer.parseInt(entrada.getText());
            entrada.setText("");
            valorAtorgar = botonAux.getText();
        }
    }

    private void operacion() {
        numero2 = Integer.parseInt(entrada.getText());
        entrada.setText("");

        switch (valorAtorgar) {
            case "/":
                entrada.setText(String.valueOf(op.division(numero1, numero2)));
                break;
            case "*":
                entrada.setText(String.valueOf(op.multiplicacion(numero1, numero2)));
                break;
            case "+":
                entrada.setText(String.valueOf(op.suma(numero1, numero2)));
                break;
            case "-":
                entrada.setText(String.valueOf(op.resta(numero1, numero2)));
                break;
            default:
                throw new AssertionError();
        }
    }

    public static void main(String[] args) {
        new Calculadora();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
