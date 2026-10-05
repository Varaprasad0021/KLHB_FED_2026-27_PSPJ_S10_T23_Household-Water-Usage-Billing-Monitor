public class BillCalculator {

    double calculateBill(double consumption) {

        double bill;

        if (consumption <= 100) {
            bill = consumption * 5;
        } else {
            bill = (100 * 5) + ((consumption - 100) * 8);
        }

        return bill;
    }
}