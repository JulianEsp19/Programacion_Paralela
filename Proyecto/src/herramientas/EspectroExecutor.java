package herramientas;

import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import javax.sound.sampled.*;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class EspectroExecutor implements Runnable{

    private final File archivoM4A;

    public EspectroExecutor(String archivoM4A) {
        this.archivoM4A = new File(archivoM4A);
    }

    @Override
    public void run() {
        try {
            String outputWavPath = System.getProperty("user.dir") + "\\src\\src\\" + archivoM4A.getName().replace(".m4a", ".wav");
            String espectroPath = System.getProperty("user.dir") + "\\src\\espectros\\" + archivoM4A.getName().replace(".m4a", ".txt");

            convertirM4AToWav(archivoM4A.getPath(), outputWavPath);
            generarEspectroYGuardar(outputWavPath, espectroPath);
            generarFingerprints(outputWavPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convertirM4AToWav(String inputFile, String outputFilePath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg", "-threads", "20", "-i", inputFile, "-ac", "1", "-ar", "44100", outputFilePath
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("ffmpeg failed with exit code " + exitCode);
        }
    }

    private void generarEspectroYGuardar(String wavFilePath, String outputFilePath) {
        try {
            byte[] audioBytes = readAudioBytes(wavFilePath);
            double[] samples = bytesToSamples(audioBytes);

            int frameSize = 4096;
            int hopSize = 2048;

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(outputFilePath), "UTF-8"),
                    65536
            );
            for (int i = 0; i < samples.length - frameSize; i += hopSize) {
                double[] frame = Arrays.copyOfRange(samples, i, i + frameSize);
                double[] windowed = applyHannWindow(frame);
                double[] magnitudes = computeFFT(windowed);

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

    private byte[] readAudioBytes(String filePath) throws Exception {
        File file = new File(filePath);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

        AudioFormat format = audioInputStream.getFormat();
        System.out.println("Formato: " + format);

        int bytesPerFrame = format.getFrameSize();
        long numFrames = audioInputStream.getFrameLength();
        long numBytes = numFrames * bytesPerFrame;

        byte[] audioBytes = new byte[(int) numBytes];
        audioInputStream.read(audioBytes);

        return audioBytes;
    }

    private double[] bytesToSamples(byte[] audioBytes) {
        int sampleCount = audioBytes.length / 2;
        double[] samples = new double[sampleCount];

        for (int i = 0; i < sampleCount; i++) {
            int low = audioBytes[2 * i] & 0xFF;
            int high = audioBytes[2 * i + 1];
            int sample = (high << 8) | low;
            if (sample > 32767) {
                sample -= 65536;
            }
            samples[i] = sample / 32768.0;
        }
        return samples;
    }

    private double[] applyHannWindow(double[] frame) {
        int N = frame.length;
        double[] windowed = new double[N];
        for (int n = 0; n < N; n++) {
            double w = 0.5 * (1 - Math.cos(2 * Math.PI * n / (N - 1)));
            windowed[n] = frame[n] * w;
        }
        return windowed;
    }

    private double[] computeFFT(double[] samples) {
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.UNITARY);
        Complex[] fftResult = fft.transform(samples, TransformType.FORWARD);

        double[] magnitudes = new double[fftResult.length / 2];
        for (int i = 0; i < magnitudes.length; i++) {
            magnitudes[i] = fftResult[i].abs();
        }
        return magnitudes;
    }

    private void generarFingerprints(String wavFilePath) throws Exception {
        byte[] audioBytes = readAudioBytes(wavFilePath);
        double[] samples = bytesToSamples(audioBytes);

        int frameSize = 4096;
        int hopSize = 2048;

        Map<Long, List<Fingerprint>> fingerprints = new HashMap<>();
        long timeIndex = 0;

        for (int i = 0; i < samples.length - frameSize; i += hopSize) {
            double[] frame = Arrays.copyOfRange(samples, i, i + frameSize);
            double[] windowedFrame = applyHannWindow(frame);
            double[] magnitudes = computeFFT(windowedFrame);

            List<Peak> peaks = detectarPicos(magnitudes, 5);

            for (int j = 0; j < peaks.size(); j++) {
                for (int k = j + 1; k < peaks.size(); k++) {
                    Fingerprint fp = new Fingerprint(peaks.get(j), peaks.get(k), timeIndex);
                    fingerprints.computeIfAbsent(timeIndex, x -> new ArrayList<>()).add(fp);
                }
            }

            timeIndex++;
        }

        // Imprimir fingerprints
        for (Map.Entry<Long, List<Fingerprint>> entry : fingerprints.entrySet()) {
            System.out.println("TimeIndex " + entry.getKey() + ":");
            for (Fingerprint fp : entry.getValue()) {
                System.out.println("  " + fp);
            }
        }
    }

    private List<Peak> detectarPicos(double[] magnitudes, int topN) {
        List<Peak> peaks = new ArrayList<>();

        for (int i = 1; i < magnitudes.length - 1; i++) {
            if (magnitudes[i] > magnitudes[i - 1] && magnitudes[i] > magnitudes[i + 1]) {
                peaks.add(new Peak(i, magnitudes[i]));
            }
        }

        peaks.sort((a, b) -> Double.compare(b.magnitude, a.magnitude));

        return peaks.subList(0, Math.min(topN, peaks.size()));
    }

    void call() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
