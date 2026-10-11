public class Reading {

    private Household household;
    private Meter meter;
    private boolean leakDetected;

    public Reading(Household household, Meter meter,
                   boolean leakDetected) {
        this.household = household;
        this.meter = meter;
        this.leakDetected = leakDetected;
    }

    public Household getHousehold() {
        return household;
    }

    public Meter getMeter() {
        return meter;
    }

    public boolean isLeakDetected() {
        return leakDetected;
    }

    public double getConsumption() {
        return meter.calculateConsumption();
    }

    public double getUsagePerPerson() {
        return getConsumption() / household.getOccupants();
    }
}