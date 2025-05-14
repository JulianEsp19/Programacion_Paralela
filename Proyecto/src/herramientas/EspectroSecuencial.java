package herramientas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class EspectroSecuencial {

    public static void convertM4AToRaw(File carpeta) throws IOException, InterruptedException {

        for (String archivo : carpeta.list()) {
            String outputFilePath = System.getProperty("user.dir") + "\\src\\src\\" + archivo.replace(".m4a", ".wav");
            String inputFile = carpeta.getPath() + File.separator + archivo;
            String espectroPath = System.getProperty("user.dir") + "\\src\\espectros\\" + archivo.replace(".m4a", ".txt");

            System.out.println(inputFile);
            System.out.println(outputFilePath);

            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg", "-i", inputFile, "-ac", "1", "-ar", "44100", outputFilePath
            );

            pb.redirectErrorStream(true); // Capturar stderr también.
            Process process = pb.start();

            // Leer salida del proceso (debug)
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            
            Thread.sleep(300);

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("ffmpeg failed with exit code " + exitCode);
            }

            generarEspectroYGuardar(outputFilePath, espectroPath);
        }
    }

    private static void generarEspectroYGuardar(String wavFilePath, String outputFilePath) {
        try {
            byte[] audioBytes = readAudioBytes(wavFilePath);
            double[] samples = bytesToSamples(audioBytes);

            int frameSize = 4096;
            int hopSize = 2048;

            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

            for (int i = 0; i < samples.length - frameSize; i += hopSize) {
                double[] frame = Arrays.copyOfRange(samples, i, i + frameSize);
                double[] windowed = applyHannWindow(frame);
                double[] magnitudes = computeFFT(windowed);

                // Guardar magnitudes en archivo
                StringBuilder sb = new StringBuilder();
                for (double mag : magnitudes) {
                    sb.append(mag).append(",");
                }
                writer.write(sb.toString());
                writer.newLine();
            }

            writer.close();
            System.out.println("Espectro guardado en: " + outputFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] readAudioBytes(String filePath) throws Exception {
        File file = new File(filePath);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

        AudioFormat format = audioInputStream.getFormat();
        System.out.println("Formato: " + format);

        int bytesPerFrame = format.getFrameSize();
        long numFrames = audioInputStream.getFrameLength();
        long numBytes = numFrames * bytesPerFrame;

        byte[] audioBytes = new byte[(int) numBytes];
        int bytesRead = audioInputStream.read(audioBytes);

        System.out.println("Bytes leídos: " + bytesRead);

        return audioBytes;
    }

    private static double[] bytesToSamples(byte[] audioBytes) {
        int sampleCount = audioBytes.length / 2; // 16-bit PCM
        double[] samples = new double[sampleCount];

        for (int i = 0; i < sampleCount; i++) {
            int low = audioBytes[2 * i] & 0xFF;
            int high = audioBytes[2 * i + 1];
            int sample = (high << 8) | low;
            if (sample > 32767) {
                sample -= 65536; // Sign extension
            }
            samples[i] = sample / 32768.0;
        }
        return samples;
    }

    private static double[] applyHannWindow(double[] frame) {
        int N = frame.length;
        double[] windowed = new double[N];
        for (int n = 0; n < N; n++) {
            double w = 0.5 * (1 - Math.cos(2 * Math.PI * n / (N - 1)));
            windowed[n] = frame[n] * w;
        }
        return windowed;
    }

    private static double[] computeFFT(double[] samples) {
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.UNITARY);
        Complex[] fftResult = fft.transform(samples, TransformType.FORWARD);

        double[] magnitudes = new double[fftResult.length / 2];
        for (int i = 0; i < magnitudes.length; i++) {
            magnitudes[i] = fftResult[i].abs(); // Magnitud (espectro)
        }
        return magnitudes;
    }

    private static void generarFingerprints(String wavFilePath) throws Exception {
        byte[] audioBytes = readAudioBytes(wavFilePath);
        double[] samples = bytesToSamples(audioBytes);

        int frameSize = 4096;    // Tamaño de la ventana
        int hopSize = 2048;      // Solapamiento del 50%

        Map<Long, List<Fingerprint>> fingerprints = new HashMap<>();
        long timeIndex = 0;

        for (int i = 0; i < samples.length - frameSize; i += hopSize) {
            double[] frame = Arrays.copyOfRange(samples, i, i + frameSize);

            // Aplicar ventana de Hann
            double[] windowedFrame = applyHannWindow(frame);

            // FFT y magnitudes
            double[] magnitudes = computeFFT(windowedFrame);

            // Detectar picos en este frame
            List<Peak> peaks = detectarPicos(magnitudes, 5);

            // Generar fingerprints combinando picos
            for (int j = 0; j < peaks.size(); j++) {
                for (int k = j + 1; k < peaks.size(); k++) {
                    Fingerprint fp = new Fingerprint(peaks.get(j), peaks.get(k), timeIndex);
                    fingerprints.computeIfAbsent(timeIndex, x -> new ArrayList<>()).add(fp);
                }
            }

            timeIndex++;
        }

        // Mostrar fingerprints generados
        for (Map.Entry<Long, List<Fingerprint>> entry : fingerprints.entrySet()) {
            System.out.println("TimeIndex " + entry.getKey() + ":");
            for (Fingerprint fp : entry.getValue()) {
                System.out.println("  " + fp);
            }
        }
    }

    private static List<Peak> detectarPicos(double[] magnitudes, int topN) {

        List<Peak> peaks = new ArrayList<>();

        for (int i = 1; i < magnitudes.length - 1; i++) {
            if (magnitudes[i] > magnitudes[i - 1] && magnitudes[i] > magnitudes[i + 1]) {
                peaks.add(new Peak(i, magnitudes[i]));
            }
        }

        peaks.sort((a, b) -> Double.compare(b.magnitude, a.magnitude));

        return peaks.subList(0, Math.min(topN, peaks.size()));
    }
}
