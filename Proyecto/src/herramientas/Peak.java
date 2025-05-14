package herramientas;

class Peak {

    int frequencyBin;
    double magnitude;

    Peak(int frequencyBin, double magnitude) {
        this.frequencyBin = frequencyBin;
        this.magnitude = magnitude;
    }

    @Override
    public String toString() {
        return "Peak{bin=" + frequencyBin + ", mag=" + magnitude + "}";
    }
}
