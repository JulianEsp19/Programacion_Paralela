package herramientas;

import java.awt.Image;
import javax.swing.ImageIcon;

public class imagenes {
    
    static public ImageIcon obtenerImagenEscalada(String ruta, int width, int heigth){
        ImageIcon imagenIcon = new ImageIcon("src/"+ruta);
        Image imagen = imagenIcon.getImage();
        imagen = imagen.getScaledInstance(width, heigth, Image.SCALE_SMOOTH);
        imagenIcon = new ImageIcon(imagen);
        
        return imagenIcon;
    }
    
}
