import java.time.LocalDate;

public class Reading {

    private Household household;
    private Meter meter;
    private boolean leakDetected;
    private LocalDate readingDate;

    public Reading(Household household, Meter meter,
                   boolean leakDetected) {
        this(household, meter, leakDetected, LocalDate.now());
    }

    public Reading(Household household, Meter meter,
                   boolean leakDetected, LocalDate readingDate) {
        this.household = household;
        this.meter = meter;
        this.leakDetected = leakDetected;
        this.readingDate = readingDate;
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

    public LocalDate getReadingDate() {
        return readingDate;
    }

    public double getConsumption() {
        return meter.calculateConsumption();
    }

    public double getUsagePerPerson() {
        return getConsumption() / household.getOccupants();
    }
}