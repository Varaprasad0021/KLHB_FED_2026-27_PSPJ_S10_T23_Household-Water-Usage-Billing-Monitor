import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Water Board ===");
        System.out.println("1) Add reading");
        System.out.println("2) Bill");
        System.out.println("3) Exit");

        System.out.print("Choice: ");
        int choice = scanner.nextInt();

        if (choice == 1) {

            System.out.print("Household ID: ");
            String householdId = scanner.next();

            System.out.print("Previous reading: ");
            double previousReading = scanner.nextDouble();

            System.out.print("Current reading: ");
            double currentReading = scanner.nextDouble();

            double consumption = currentReading - previousReading;

            BillCalculator calculator = new BillCalculator();
double bill = calculator.calculateBill(consumption);

            System.out.println();
            System.out.println("Household: " + householdId);
            System.out.println("Consumption: " + consumption + " units");
            System.out.println("Bill: ₹" + bill);
            System.out.println("Reading added successfully.");

        } else if (choice == 2) {

            System.out.println("Bill calculation will be added next.");

        } else if (choice == 3) {

            System.out.println("Thank you for using Water Board.");

        } else {

            System.out.println("Invalid choice.");

        }

        scanner.close();
    }
}