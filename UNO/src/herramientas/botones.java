package herramientas;

import java.awt.Font;
import javax.swing.JButton;

public class botones {

    public static JButton iniciarBotones(String texto, int width, int heigth){
        
        JButton boton = new JButton(imagenes.obtenerImagenEscalada("src/boton-1.png", width, heigth));
        boton.setContentAreaFilled(false);
        boton.setBorder(null);
        boton.setText(texto);
        boton.setHorizontalTextPosition(JButton.CENTER);
        boton.setPressedIcon(imagenes.obtenerImagenEscalada("src/boton-1-oscuro.png", width, heigth));
        boton.setFont(new Font("Arial", Font.BOLD, 15));
        
        return boton;
    }
}
