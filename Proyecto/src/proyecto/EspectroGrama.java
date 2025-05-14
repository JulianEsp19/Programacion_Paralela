package proyecto;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class EspectroGrama extends JFrame {

    private BufferedImage espectro;

    public EspectroGrama(String ruta) throws IOException {
        setTitle("espectro");
        setSize(1000, 700);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        List<double[]> spectrogramData = leerEspectro(ruta);
        if (spectrogramData != null) {
            espectro = crearImagenColormap(spectrogramData);
            setPreferredSize(new Dimension(1000, 700));
            pack();
            setLocationRelativeTo(null);

            guardarComoPNG(espectro, ruta.replace(".txt", ".png").replace("espectros", "imagenes"));
        } else {
            System.err.println("No se pudo leer el archivo de espectro.");
        }

    }

    private List<double[]> leerEspectro(String filePath) {
        List<double[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                double[] magnitudes = new double[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    magnitudes[i] = Double.parseDouble(parts[i]);
                }
                data.add(magnitudes);
            }
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private BufferedImage crearImagenColormap(List<double[]> spectrogramData) {
        int width = spectrogramData.size();
        int height = spectrogramData.get(0).length;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        double maxMagnitude = Double.MIN_VALUE;
        for (double[] frame : spectrogramData) {
            for (double mag : frame) {
                if (mag > maxMagnitude) {
                    maxMagnitude = mag;
                }
            }
        }

        for (int x = 0; x < width; x++) {
            double[] frame = spectrogramData.get(x);
            for (int y = 0; y < height; y++) {
                double value = frame[y] / maxMagnitude;
                Color color = getColormapColor(value);
                img.setRGB(x, height - 1 - y, color.getRGB());
            }
        }

        return img;
    }

    private Color getColormapColor(double normalizedValue) {
        normalizedValue = Math.min(1.0, Math.max(0.0, normalizedValue));
        float hue = (float) (0.66 - normalizedValue * 0.66); // De azul (0.66) a rojo (0.0)
        float saturation = 1.0f;
        float brightness = (float) Math.sqrt(normalizedValue);

        return Color.getHSBColor(hue, saturation, brightness);
    }

    private void guardarComoPNG(BufferedImage image, String outputPath) throws IOException {
        File outputFile = new File(outputPath);
        ImageIO.write(image, "png", outputFile);
        System.out.println("Imagen guardada en: " + outputPath);
        // Abrir imagen automáticamente
        Desktop.getDesktop().open(outputFile);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (espectro != null) {
            g.drawImage(espectro, 20, 40, null);
        }
    }
}
