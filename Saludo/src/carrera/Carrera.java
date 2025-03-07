package carrera;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Carrera {

    public static void main(String[] args) {
        Segundos segundos = new Segundos();
        
        float metrosMosca = 0;
        float metrosHamster = 0;
        float metrosCaballo = 0;
        
        int posicion = 1;
        
        int[] posicionMosca = new int[2];
        int[] posicionHamster = new int[2];
        int[] posicionCaballo = new int[2];
        
        boolean carreraTerminada = false;
        
        segundos.start();
        while(!carreraTerminada){
            if(metrosMosca < 500){
                metrosMosca = (float) (segundos.segundos * 1.9444444);
                System.out.println("mosca :" + metrosMosca);
            }else if(posicionMosca[0] == 0){
                posicionMosca[0] = posicion;
                posicionMosca[1] = segundos.segundos;
                posicion++;
            }
            
            if(metrosHamster < 500){
                metrosHamster = (float) (segundos.segundos * 2.22222);
                System.out.println("Hamster :" + metrosHamster);
            }else if(posicionHamster[0] == 0){
                posicionHamster[0] = posicion;
                posicionHamster[1] = segundos.segundos;
                posicion++;
            }
            
            if(metrosCaballo < 500){
                metrosCaballo = (float) (segundos.segundos * 19.722222);
                System.out.println("Caballo :" + metrosCaballo);
            }else if(posicionCaballo[0] == 0){
                posicionCaballo[0] = posicion;
                posicionCaballo[1] = segundos.segundos;
                posicion++;
            }
            
            if(posicion == 4){
                carreraTerminada = true;
            }
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Carrera.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        System.out.println("Tabla de posiciones");
        System.out.println("Mosca : " + posicionMosca[0] + "    Tiempo: " +posicionMosca[1]);
        System.out.println("Hamster : " + posicionHamster[0]+ "    Tiempo: " +posicionHamster[1]);
        System.out.println("Caballo : " + posicionCaballo[0]+ "    Tiempo: " +posicionCaballo[1]);
    }
    
}
