
import java.io.IOException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private static final ArrayList<Reading> history = new ArrayList<>();

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n=== Water Board ===");
                System.out.println("1) Add reading");
                System.out.println("2) Bill");
                System.out.println("3) Report");
                System.out.println("4) Household Consumption Grid");
                System.out.println("5) View saved readings");
                System.out.println("6) View saved bills");
                System.out.println("7) Exit");
                System.out.println("8) Reading History");
                System.out.print("Choice: ");

                int choice;
                try {
                    choice = scanner.nextInt();
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Please enter a valid menu number.");
                    scanner.nextLine();
                    continue;
                }

                switch (choice) {
                    case 1:
                        addReading(scanner);
                        break;
                    case 2:
                        generateBill(scanner);
                        break;
                    case 3:
                        showReport();
                        break;
                    case 4:
                        showConsumptionGrid(scanner);
                        break;
                    case 5:
                        showSavedReadings();
                        break;
                    case 6:
                        showSavedBills();
                        break;
                    case 7:
                        System.out.println("Thank you for using Water Board.");
                        return;
                    case 8:
                        showReadingHistory();
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        
                    case 9:
                        showHouseholdSummary();
                        break;
                    case 10:
                        showStreamsAnalytics();
                        break;
                    default:

                }
            }
        }
    }

    // W11: Household-wise consumption summary
  
        try {
            List<String> readings = WaterDataStore.loadReadings();

            Map<String, Double> totalConsumption = new HashMap<>();
            Map<String, Integer> readingCounts = new HashMap<>();

            for (String record : readings) {
                String[] fields = record.split(",");

                if (fields.length != 5) {
                    continue;
                }

                try {
                    String householdId = fields[0];
                    double previousReading =
                            Double.parseDouble(fields[2]);
                    double currentReading =
                            Double.parseDouble(fields[3]);

                    double consumption = currentReading - previousReading;

                    totalConsumption.put(
                            householdId,
                            totalConsumption.getOrDefault(
                                    householdId, 0.0) + consumption);

                    readingCounts.put(
                            householdId,
                            readingCounts.getOrDefault(
                                    householdId, 0) + 1);

                } catch (NumberFormatException e) {
                    System.out.println(
                            "Skipping an invalid reading record.");
                }
            }

            if (totalConsumption.isEmpty()) {
                System.out.println("No saved readings available.");
                return;
            }

            System.out.printf("%-18s %-20s %-15s%n",
                    "Household ID", "Total Consumption", "Readings");
            System.out.println(
                    "-------------------------------------------------------");

            for (String householdId : totalConsumption.keySet()) {
                System.out.printf("%-18s %-20.2f %-15d%n",
                        householdId,
                        totalConsumption.get(householdId),
                        readingCounts.get(householdId));
            }

        } catch (IOException e) {
            System.out.println(
                    "Could not load household summary: " + e.getMessage());
        }
    }

    private static void addReading(Scanner scanner) {
        System.out.print("Household ID: ");
        String householdId = scanner.next();

        System.out.print("Number of occupants: ");
        int occupants = scanner.nextInt();

        if (occupants <= 0) {
            System.out.println(
                    "Number of occupants must be greater than zero.");
            return;
        }

        System.out.print("Previous reading: ");
        double previousReading = scanner.nextDouble();

        System.out.print("Current reading: ");
        double currentReading = scanner.nextDouble();

        System.out.print("Is there a leak? (true/false): ");
        boolean leakDetected = scanner.nextBoolean();

        try {
            Household household = new Household(householdId, occupants);
            Meter meter = new Meter(previousReading, currentReading);
            Reading reading = new Reading(household, meter, leakDetected);

            double consumption = reading.getConsumption();
            BillCalculator calculator = new BillCalculator();
            double bill = calculator.calculateBill(consumption);

            WaterDataStore.saveReading(reading);
            WaterDataStore.saveBill(
                    householdId, consumption, "Standard", bill);

            history.add(reading);

            System.out.println("\n=== Reading Details ===");
            System.out.println("Household: " + household.getHouseholdId());
            System.out.println("Occupants: " + household.getOccupants());
            System.out.println("Previous reading: "
                    + meter.getPreviousReading());
            System.out.println("Current reading: "
                    + meter.getCurrentReading());
            System.out.printf("Consumption: %.2f units%n", consumption);
            System.out.printf("Standard tariff bill: Rs.%.2f%n", bill);

            System.out.println("Leak status: "
                    + (reading.isLeakDetected()
                    ? "LIKELY LEAK" : "No leak detected"));

            System.out.println("Usage status: "
                    + (consumption > 200 ? "OVERUSE" : "Normal usage"));

            System.out.printf("Usage per person: %.2f units%n",
                    reading.getUsagePerPerson());

            System.out.println("Per-person status: "
                    + (reading.getUsagePerPerson() > 50
                    ? "HIGH USAGE" : "Normal usage"));

            System.out.println(
                    "Reading and standard bill saved successfully.");

        } catch (InvalidReadingException
                | InvalidConsumptionException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }

    private static void generateBill(Scanner scanner) {
        System.out.print("Household ID: ");
        String householdId = scanner.next();

        System.out.print("Enter consumption: ");
        double consumption = scanner.nextDouble();

        System.out.println("Choose tariff:");
        System.out.println("1) Standard tariff");
        System.out.println("2) Premium tariff");
        System.out.print("Tariff choice: ");

        int tariffChoice = scanner.nextInt();
        Tariff selectedTariff;
        String tariffName;

        if (tariffChoice == 1) {
            selectedTariff = new StandardTariff();
            tariffName = "Standard";
        } else if (tariffChoice == 2) {
            selectedTariff = new PremiumTariff();
            tariffName = "Premium";
        } else {
            System.out.println("Invalid tariff choice.");
            return;
        }

        try {
            BillCalculator calculator =
                    new BillCalculator(selectedTariff);
            double bill = calculator.calculateBill(consumption);

            System.out.printf("Consumption: %.2f units%n", consumption);
            System.out.printf("Bill: Rs.%.2f%n", bill);

            WaterDataStore.saveBill(
                    householdId, consumption, tariffName, bill);

            System.out.println("Bill saved successfully.");

        } catch (InvalidConsumptionException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not save bill: " + e.getMessage());
        }
    }

    private static void showSavedReadings() {
        try {
            List<String> readings = WaterDataStore.loadReadings();

            System.out.println("\n=== Saved Meter Readings ===");
            if (readings.isEmpty()) {
                System.out.println("No saved readings yet.");
                return;
            }

            for (String reading : readings) {
                System.out.println(reading);
            }
        } catch (IOException e) {
            System.out.println("Could not load readings: " + e.getMessage());
        }
    }

    private static void showSavedBills() {
        try {
            List<String> bills = WaterDataStore.loadBills();

            System.out.println("\n=== Saved Bills ===");
            if (bills.isEmpty()) {
                System.out.println("No saved bills yet.");
                return;
            }

            for (String bill : bills) {
                System.out.println(bill);
            }
        } catch (IOException e) {
            System.out.println("Could not load bills: " + e.getMessage());
        }
    }

    private static void showReport() {
        try {
            List<String> readings = WaterDataStore.loadReadings();
            List<String> bills = WaterDataStore.loadBills();

            System.out.println("\n=== Water Usage Report ===");
            System.out.println("Saved meter readings: " + readings.size());
            System.out.println("Saved bills: " + bills.size());

            double totalConsumption = 0;
            for (Reading reading : history) {
                totalConsumption += reading.getConsumption();
            }

            System.out.println("Readings this session: " + history.size());
            System.out.printf("Session consumption: %.2f units%n",
                    totalConsumption);

            if (!history.isEmpty()) {
                System.out.printf("Session average consumption: %.2f units%n",
                        totalConsumption / history.size());
            }
        } catch (IOException e) {
            System.out.println("Could not load report: " + e.getMessage());
        }
    }

    private static void showReadingHistory() {
        System.out.println("\n=== Household Reading History ===");

        if (history.isEmpty()) {
            System.out.println("No readings recorded in this session yet.");
            return;
        }

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
                    ? "LIKELY LEAK" : "No leak detected"));
        }
    }

    private static void showConsumptionGrid(Scanner scanner) {
        System.out.print("Number of households: ");
        int households = scanner.nextInt();

        System.out.print("Number of periods: ");
        int periods = scanner.nextInt();

        if (households <= 0 || periods <= 0) {
            System.out.println(
                    "Households and periods must be greater than zero.");
            return;
        }

        String[] householdIds = new String[households];
        double[][] consumptionGrid = new double[households][periods];

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
                System.out.printf("%-12.2f", consumptionGrid[i][j]);
            }
            System.out.println();
        }
    }
        private static void showStreamsAnalytics() {
        System.out.println("\n=== W12: Streams Analytics ===");

        if (history.isEmpty()) {
            System.out.println(
                    "No readings in this session. Add readings first.");
            return;
        }

        // 1. Total consumption using Java Streams
        double totalConsumption = history.stream()
                .mapToDouble(Reading::getConsumption)
                .sum();

        System.out.printf("Total consumption: %.2f units%n",
                totalConsumption);

        // 2. Filter readings where leaks were detected
        List<Reading> leakReadings = history.stream()
                .filter(Reading::isLeakDetected)
                .collect(Collectors.toList());

        System.out.println("\n--- Leak-Detected Readings ---");

        if (leakReadings.isEmpty()) {
            System.out.println("No leak-detected readings.");
        } else {
            leakReadings.forEach(reading ->
                    System.out.printf(
                            "Household: %s | Date: %s | Consumption: %.2f units%n",
                            reading.getHousehold().getHouseholdId(),
                            reading.getReadingDate(),
                            reading.getConsumption()));
        }

        // 3. Group consumption by month
        Map<YearMonth, Double> monthlyUsage = history.stream()
                .collect(Collectors.groupingBy(
                        reading -> YearMonth.from(
                                reading.getReadingDate()),
                        Collectors.summingDouble(
                                Reading::getConsumption)));

        System.out.println("\n--- Monthly Consumption ---");

        monthlyUsage.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        System.out.printf("%s : %.2f units%n",
                                entry.getKey(),
                                entry.getValue()));
    }
}
