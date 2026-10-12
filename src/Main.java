import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Water Board ===");
        System.out.println("1) Add reading");
        System.out.println("2) Bill");
        System.out.println("3) Report");
        System.out.println("4) Household Consumption Grid");
        System.out.println("5) Exit");
        System.out.print("Choice: ");

        int choice = scanner.nextInt();

        if (choice == 1) {

            System.out.print("Household ID: ");
            String householdId = scanner.next();

            System.out.print("Number of occupants: ");
            int occupants = scanner.nextInt();

            if (occupants <= 0) {
                System.out.println(
                        "Number of occupants must be greater than zero.");
                scanner.close();
                return;
            }

            System.out.print("Previous reading: ");
            double previousReading = scanner.nextDouble();

            System.out.print("Current reading: ");
            double currentReading = scanner.nextDouble();

            System.out.print("Is there a leak? (true/false): ");
            boolean leakDetected = scanner.nextBoolean();

            try {
                Household household =
                        new Household(householdId, occupants);

                Meter meter =
                        new Meter(previousReading, currentReading);

                Reading reading =
                        new Reading(household, meter, leakDetected);

                BillCalculator calculator = new BillCalculator();
                double consumption = reading.getConsumption();
                double bill = calculator.calculateBill(consumption);

                System.out.println("\n=== Reading Details ===");
                System.out.println("Household: "
                        + household.getHouseholdId());
                System.out.println("Occupants: "
                        + household.getOccupants());
                System.out.println("Previous reading: "
                        + meter.getPreviousReading());
                System.out.println("Current reading: "
                        + meter.getCurrentReading());
                System.out.println("Consumption: "
                        + consumption + " units");
                System.out.printf("Standard tariff bill: Rs.%.2f%n", bill);

                if (reading.isLeakDetected()) {
                    System.out.println("Leak status: LIKELY LEAK");
                } else {
                    System.out.println("Leak status: No leak detected");
                }

                if (consumption > 200) {
                    System.out.println("Usage status: OVERUSE");
                } else {
                    System.out.println("Usage status: Normal usage");
                }

                System.out.printf("Usage per person: %.2f units%n",
                        reading.getUsagePerPerson());

                if (reading.getUsagePerPerson() > 50) {
                    System.out.println("Per-person status: HIGH USAGE");
                } else {
                    System.out.println("Per-person status: Normal usage");
                }

                System.out.println("Reading added successfully.");

            } catch (InvalidReadingException
                    | InvalidConsumptionException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } else if (choice == 2) {

            System.out.print("Enter consumption: ");
            double consumption = scanner.nextDouble();

            System.out.println("Choose tariff:");
            System.out.println("1) Standard tariff (Rs.5 / Rs.8)");
            System.out.println("2) Premium tariff (Rs.7 / Rs.10)");
            System.out.print("Tariff choice: ");

            int tariffChoice = scanner.nextInt();

            Tariff selectedTariff;

            if (tariffChoice == 1) {
                selectedTariff = new StandardTariff();
            } else if (tariffChoice == 2) {
                selectedTariff = new PremiumTariff();
            } else {
                System.out.println("Invalid tariff choice.");
                scanner.close();
                return;
            }

            try {
                // Polymorphism: the same interface supports either tariff.
                BillCalculator calculator =
                        new BillCalculator(selectedTariff);

                double bill = calculator.calculateBill(consumption);

                System.out.println("Consumption: "
                        + consumption + " units");
                System.out.printf("Bill: Rs.%.2f%n", bill);

            } catch (InvalidConsumptionException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } else if (choice == 3) {

            System.out.println("\n=== Water Usage Report ===");
            System.out.println(
                    "The report summarizes household water usage.");
            System.out.println(
                    "Use option 1 to enter a household reading.");

        } else if (choice == 4) {

            System.out.print("Number of households: ");
            int households = scanner.nextInt();

            System.out.print("Number of periods: ");
            int periods = scanner.nextInt();

            if (households <= 0 || periods <= 0) {
                System.out.println(
                        "Households and periods must be greater than zero.");
            } else {
                String[] householdIds = new String[households];
                double[][] consumptionGrid =
                        new double[households][periods];

                for (int i = 0; i < households; i++) {
                    System.out.print("Enter Household ID for household "
                            + (i + 1) + ": ");
                    householdIds[i] = scanner.next();

                    for (int j = 0; j < periods; j++) {
                        System.out.print("Enter consumption for "
                                + householdIds[i] + " - Period "
                                + (j + 1) + ": ");
                        consumptionGrid[i][j] = scanner.nextDouble();
                    }
                }

                System.out.println(
                        "\n=== Household x Period Consumption Grid ===");

                System.out.printf("%-15s", "Household");

                for (int j = 0; j < periods; j++) {
                    System.out.printf("%-12s", "Period " + (j + 1));
                }

                System.out.println();

                for (int i = 0; i < households; i++) {
                    System.out.printf("%-15s", householdIds[i]);

                    for (int j = 0; j < periods; j++) {
                        System.out.printf("%-12.2f",
                                consumptionGrid[i][j]);
                    }

                    System.out.println();
                }
            }

        } else if (choice == 5) {

            System.out.println("Thank you for using Water Board.");

        } else {

            System.out.println("Invalid choice.");
        }

        scanner.close();
    }
}