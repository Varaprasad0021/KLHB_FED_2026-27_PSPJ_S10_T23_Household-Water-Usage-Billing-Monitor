public class BillCalculator {

    private Tariff tariff;

    public BillCalculator() {
        this.tariff = new StandardTariff();
    }

    public BillCalculator(Tariff tariff) {
        if (tariff == null) {
            throw new IllegalArgumentException(
                    "Tariff cannot be null.");
        }

        this.tariff = tariff;
    }

    public double calculateBill(double consumption)
            throws InvalidConsumptionException {
        return tariff.calculateBill(consumption);
    }
}