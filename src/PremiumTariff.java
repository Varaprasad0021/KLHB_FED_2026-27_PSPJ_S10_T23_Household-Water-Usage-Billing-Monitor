public class PremiumTariff implements Tariff {

    @Override
    public double calculateBill(double consumption)
            throws InvalidConsumptionException {

        if (!Double.isFinite(consumption) || consumption < 0) {
            throw new InvalidConsumptionException(
                    "Consumption must be a finite, non-negative number.");
        }

        if (consumption <= 100) {
            return consumption * 7;
        }

        return (100 * 7) + ((consumption - 100) * 10);
    }
}