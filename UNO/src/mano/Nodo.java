package mano;

import javax.swing.JLabel;

public class Nodo {

    private JLabel carta;
    private int numero;
    private Nodo siguiente;

    public Nodo(JLabel carta, int numero) {
        this.carta = carta;
        this.numero = numero;
        siguiente = null;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public JLabel getCarta() {
        return carta;
    }

    public void setCarta(JLabel carta) {
        this.carta = carta;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
}
