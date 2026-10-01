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

System.out.println();
System.out.println("Household: " + householdId);
System.out.println("Occupants: " + occupants);
System.out.println("Consumption: " + consumption + " units");
System.out.println("Leak detected: " + leakDetected);
System.out.println("Bill: ₹" + bill);
System.out.println("Reading added successfully.");

} else if (choice == 2) {

System.out.print("Enter consumption: ");
double consumption = scanner.nextDouble();

BillCalculator calculator = new BillCalculator();
double bill = calculator.calculateBill(consumption);

System.out.println();
System.out.println("Consumption: " + consumption + " units");
System.out.println("Bill: ₹" + bill);

} else if (choice == 3) {

System.out.println("Thank you for using Water Board.");

} else {

System.out.println("Invalid choice.");

}

scanner.close();
}
}