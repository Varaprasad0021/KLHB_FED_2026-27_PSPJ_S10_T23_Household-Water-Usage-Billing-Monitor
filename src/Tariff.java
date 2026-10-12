public interface Tariff {
    double calculateBill(double consumption)
            throws InvalidConsumptionException;
}