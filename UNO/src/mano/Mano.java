package mano;

import herramientas.imagenes;
import javax.swing.JLabel;

public class Mano {
    
    private int length;
    private Nodo inicio;

    public Mano() {
        length = 0;
        inicio = null;
    }
    
    public JLabel agregar(int numero){
        JLabel auxLabel = new JLabel(imagenes.obtenerImagenEscalada("src/cartas" + numero + ".jpg", 70, 105));
        auxLabel.setBounds(50 + (length * 80), 320, 70, 105);
        Nodo siguiente = new Nodo(auxLabel, numero);
        if(length == 0) inicio = siguiente;
        else{
            Nodo aux = inicio;
            
            while(aux.getSiguiente() != null){
                aux = aux.getSiguiente();
            }
            
            aux.setSiguiente(siguiente);
        }
        
        length ++;
        
        return auxLabel;
    }
    
    public int buscarValorCarta(JLabel carta){
        Nodo aux = inicio;
        while(aux != null){
            if(aux.getCarta() == carta) return aux.getNumero();
            aux = aux.getSiguiente();
        }
        return -1;
    }
    
    public void eliminarCarta(JLabel carta){
        if(inicio.getCarta() == carta){
            System.out.println("numero carta: " + inicio.getNumero());
            inicio = inicio.getSiguiente();
            carta.setVisible(false);
            length--;
            acomodarCartas();
            return;
        }
        Nodo aux = inicio;
        Nodo siguiente = aux.getSiguiente();
        while(siguiente != null){
            if(siguiente.getCarta() == carta){
                System.out.println("numero carta: " + siguiente.getNumero());
                carta.setVisible(false);
                aux.setSiguiente(siguiente.getSiguiente());
                length--;
                acomodarCartas();
            }
            siguiente = siguiente.getSiguiente();
            aux = aux.getSiguiente();
        }
    }
    
    public void acomodarCartas(){
        Nodo aux = inicio;
        for (int i = 0; i < length; i++) {
            aux.getCarta().setBounds(50 + (i * 80), 320, 70, 105);
            aux = aux.getSiguiente();
        }
    }
        
    public int getLength(){
        return length;
    }
    
    
    
}
