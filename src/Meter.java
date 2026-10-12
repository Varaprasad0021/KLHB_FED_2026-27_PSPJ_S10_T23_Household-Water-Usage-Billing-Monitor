public class Meter {

    private double previousReading;
    private double currentReading;

    public Meter(double previousReading, double currentReading)
            throws InvalidReadingException {

        if (!Double.isFinite(previousReading)
                || !Double.isFinite(currentReading)) {
            throw new InvalidReadingException(
                    "Meter readings must be finite numbers.");
        }

        if (previousReading < 0) {
            throw new InvalidReadingException(
                    "Previous reading cannot be negative.");
        }

        if (currentReading < previousReading) {
            throw new InvalidReadingException(
                    "Current reading cannot be less than previous reading.");
        }

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