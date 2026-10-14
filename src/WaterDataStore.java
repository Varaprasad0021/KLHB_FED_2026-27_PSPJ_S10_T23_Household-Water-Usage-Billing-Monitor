
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class WaterDataStore {

    private static final Path DATA_DIR = Paths.get("data");
    private static final Path READINGS_FILE =
            DATA_DIR.resolve("readings.csv");
    private static final Path BILLS_FILE =
            DATA_DIR.resolve("bills.csv");

    private static void prepareFiles() throws IOException {
        Files.createDirectories(DATA_DIR);

        if (Files.notExists(READINGS_FILE)) {
            Files.writeString(READINGS_FILE,
                    "householdId,occupants,previousReading,currentReading,leakDetected"
                    + System.lineSeparator());
        }

        if (Files.notExists(BILLS_FILE)) {
            Files.writeString(BILLS_FILE,
                    "householdId,consumption,tariff,billAmount"
                    + System.lineSeparator());
        }
    }

    public static void saveReading(Reading reading)
            throws IOException {
        prepareFiles();

        String id = reading.getHousehold().getHouseholdId();

        if (id.contains(",") || id.contains("\n") || id.contains("\r")) {
            throw new IOException("Invalid household ID.");
        }

        Meter meter = reading.getMeter();

        String record = id + ","
                + reading.getHousehold().getOccupants() + ","
                + meter.getPreviousReading() + ","
                + meter.getCurrentReading() + ","
                + reading.isLeakDetected();

        try (BufferedWriter writer = Files.newBufferedWriter(
                READINGS_FILE,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.write(record);
            writer.newLine();
        }
    }

    public static void saveBill(String householdId,
            double consumption, String tariff, double amount)
            throws IOException {
        prepareFiles();

        if (householdId.contains(",") || tariff.contains(",")) {
            throw new IOException("Invalid household ID or tariff.");
        }

        String record = householdId + ","
                + consumption + ","
                + tariff + ","
                + amount;

        try (BufferedWriter writer = Files.newBufferedWriter(
                BILLS_FILE,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.write(record);
            writer.newLine();
        }
    }

    public static List<String> loadReadings() throws IOException {
        return loadRecords(READINGS_FILE,
                "householdId,occupants,previousReading,currentReading,leakDetected");
    }

    public static List<String> loadBills() throws IOException {
        return loadRecords(BILLS_FILE,
                "householdId,consumption,tariff,billAmount");
    }

    private static List<String> loadRecords(Path file, String header)
            throws IOException {
        prepareFiles();

        List<String> records = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            reader.readLine(); // Skip CSV header.

            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    records.add(line);
                }
            }
        }

        return records;
    }
}
