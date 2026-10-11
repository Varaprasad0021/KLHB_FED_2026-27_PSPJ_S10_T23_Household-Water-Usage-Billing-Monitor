public class Meter {

    private double previousReading;
    private double currentReading;

    public Meter(double previousReading, double currentReading) {
        this.previousReading = previousReading;
        this.currentReading = currentReading;
    }

    public double getPreviousReading() {
        return previousReading;
    }

    public double getCurrentReading() {
        return currentReading;
    }

    public double calculateConsumption() {
        return currentReading - previousReading;
    }
}