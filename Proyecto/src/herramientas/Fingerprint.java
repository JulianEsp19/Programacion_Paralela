package herramientas;

class Fingerprint {

    int freq1;
    int freq2;
    long deltaTime;
    long timeIndex;

    Fingerprint(Peak p1, Peak p2, long timeIndex) {
        this.freq1 = p1.frequencyBin;
        this.freq2 = p2.frequencyBin;
        this.deltaTime = 1; // Simplicidad (en real sería p2.time - p1.time)
        this.timeIndex = timeIndex;
    }

    @Override
    public String toString() {
        return "Fingerprint{f1=" + freq1 + ", f2=" + freq2 + ", Δt=" + deltaTime + "}";
    }
}
