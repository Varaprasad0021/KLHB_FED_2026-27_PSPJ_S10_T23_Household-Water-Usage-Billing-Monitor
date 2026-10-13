
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ArrayList<Reading> history = new ArrayList<>();

        while (true) {

            System.out.println("\n=== Water Board ===");
            System.out.println("1) Add reading");
            System.out.println("2) Bill");
            System.out.println("3) Report");
            System.out.println("4) Household Consumption Grid");
            System.out.println("5) Exit");
            System.out.println("6) Reading History");
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
                    continue;
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
                    System.out.printf("Consumption: %.2f units%n",
                            consumption);
                    System.out.printf("Standard tariff bill: Rs.%.2f%n",
                            bill);

                    if (reading.isLeakDetected()) {
                        System.out.println("Leak status: LIKELY LEAK");
                    } else {
                        System.out.println(
                                "Leak status: No leak detected");
                    }

                    if (consumption > 200) {
                        System.out.println("Usage status: OVERUSE");
                    } else {
                        System.out.println("Usage status: Normal usage");
                    }

                    double perPerson = reading.getUsagePerPerson();

                    System.out.printf("Usage per person: %.2f units%n",
                            perPerson);

                    if (perPerson > 50) {
                        System.out.println(
                                "Per-person status: HIGH USAGE");
                    } else {
                        System.out.println(
                                "Per-person status: Normal usage");
                    }

                    history.add(reading);

                    System.out.println(
                            "Reading added to history successfully.");
                    System.out.println("Total saved readings this run: "
                            + history.size());

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
                    continue;
                }

                try {
                    BillCalculator calculator =
                            new BillCalculator(selectedTariff);

                    double bill = calculator.calculateBill(consumption);

                    System.out.printf("Consumption: %.2f units%n",
                            consumption);
                    System.out.printf("Bill: Rs.%.2f%n", bill);

                } catch (InvalidConsumptionException e) {
                    System.out.println("Error: " + e.getMessage());
                }

            } else if (choice == 3) {

                System.out.println("\n=== Water Usage Report ===");
                System.out.println("Total readings: " + history.size());

                double totalConsumption = 0;

                for (Reading reading : history) {
                    totalConsumption += reading.getConsumption();
                }

                System.out.printf("Total consumption: %.2f units%n",
                        totalConsumption);

                if (!history.isEmpty()) {
                    System.out.printf("Average consumption: %.2f units%n",
                            totalConsumption / history.size());
                } else {
                    System.out.println("No readings recorded yet.");
                }

            } else if (choice == 4) {

                System.out.print("Number of households: ");
                int households = scanner.nextInt();

                System.out.print("Number of periods: ");
                int periods = scanner.nextInt();

                if (households <= 0 || periods <= 0) {
                    System.out.println(
                            "Households and periods must be greater than zero.");
                    continue;
                }

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

            } else if (choice == 5) {

                System.out.println(
                        "Thank you for using Water Board.");
                break;

            } else if (choice == 6) {

                System.out.println("\n=== Household Reading History ===");

                if (history.isEmpty()) {
                    System.out.println("No readings recorded yet.");
                } else {
                    for (int i = 0; i < history.size(); i++) {

                        Reading reading = history.get(i);

                        System.out.println("\nReading " + (i + 1));
                        System.out.println("Household: "
                                + reading.getHousehold().getHouseholdId());
                        System.out.println("Occupants: "
                                + reading.getHousehold().getOccupants());
                        System.out.printf("Consumption: %.2f units%n",
                                reading.getConsumption());
                        System.out.printf("Usage per person: %.2f units%n",
                                reading.getUsagePerPerson());

                        System.out.println("Leak status: "
                                + (reading.isLeakDetected()
                                ? "LIKELY LEAK"
                                : "No leak detected"));
                    }
                }

            } else {

                System.out.println("Invalid choice.");
            }
        }

        scanner.close();
    }
}
