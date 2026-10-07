import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Water Board ===");
        System.out.println("1) Add reading");
        System.out.println("2) Bill");
              System.out.println("3) Report");
        System.out.println("4) Household Consumption Grid");
        System.out.print("Choice: ");
        int choice = scanner.nextInt();

        if (choice == 1) {

            System.out.print("Household ID: ");
            String householdId = scanner.next();

            System.out.print("Number of occupants: ");
            int occupants = scanner.nextInt();

            System.out.print("Previous reading: ");
            double previousReading = scanner.nextDouble();

            System.out.print("Current reading: ");
            double currentReading = scanner.nextDouble();

            System.out.print("Is there a leak? (true/false): ");
            boolean leakDetected = scanner.nextBoolean();

            double consumption = currentReading - previousReading;

            BillCalculator calculator = new BillCalculator();
            double bill = calculator.calculateBill(consumption);

            double usagePerPerson = consumption / occupants;

            System.out.println();
            System.out.println("Household: " + householdId);
            System.out.println("Occupants: " + occupants);
            System.out.println("Consumption: " + consumption + " units");
            System.out.println("Bill: Rs." + bill);

            if (leakDetected) {
                System.out.println("Leak status: LIKELY LEAK");
            } else {
                System.out.println("Leak status: No leak detected");
            }

            if (consumption > 200) {
                System.out.println("Usage status: OVERUSE");
            } else {
                System.out.println("Usage status: Normal usage");
            }

            System.out.println("Usage per person: " + usagePerPerson + " units");

            if (usagePerPerson > 50) {
                System.out.println("Per-person status: HIGH USAGE");
            } else {
                System.out.println("Per-person status: Normal usage");
            }

            System.out.println("Reading added successfully.");

        } else if (choice == 2) {

            System.out.print("Enter consumption: ");
            double consumption = scanner.nextDouble();

            BillCalculator calculator = new BillCalculator();
            double bill = calculator.calculateBill(consumption);

            System.out.println();
            System.out.println("Consumption: " + consumption + " units");
            System.out.println("Bill: Rs." + bill);

        } else if (choice == 3) {

            System.out.println();
            System.out.println("=== Water Usage Report ===");
            System.out.println("The report summarizes household water usage.");
            System.out.println("Use option 1 to add a new household reading.");

        } else if (choice == 4) {

            System.out.println("Thank you for using Water Board.");

        } else if (choice == 4) {

            System.out.print("Number of households: ");
            int households = scanner.nextInt();

            System.out.print("Number of periods: ");
            int periods = scanner.nextInt();

            String[] householdIds = new String[households];
            double[][] consumptionGrid = new double[households][periods];

            for (int i = 0; i < households; i++) {

                System.out.print("Enter Household ID for household " + (i + 1) + ": ");
                householdIds[i] = scanner.next();

                for (int j = 0; j < periods; j++) {

                    System.out.print("Enter consumption for "
                            + householdIds[i] + " - Period " + (j + 1) + ": ");

                    consumptionGrid[i][j] = scanner.nextDouble();
                }
            }

            System.out.println();
            System.out.println("=== Household x Period Consumption Grid ===");

            System.out.printf("%-15s", "Household");

            for (int j = 0; j < periods; j++) {
                System.out.printf("%-12s", "Period " + (j + 1));
            }

            System.out.println();

            for (int i = 0; i < households; i++) {

                System.out.printf("%-15s", householdIds[i]);

                for (int j = 0; j < periods; j++) {
                    System.out.printf("%-12.2f", consumptionGrid[i][j]);
                }

                System.out.println();
            }

        } else {

            System.out.println("Invalid choice.");

        }

        scanner.close();
    }
}
