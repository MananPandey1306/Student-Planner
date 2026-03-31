package studyplanner.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputHelper {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static String readNonEmpty(String prompt) {
        while (true) {
            String val = readString(prompt);
            if (!val.isEmpty()) return val;
            System.out.println("  ✗ Input cannot be empty. Try again.");
        }
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  ✗ Please enter a valid integer.");
            }
        }
    }

    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int val = readInt(prompt);
            if (val >= min && val <= max) return val;
            System.out.println("  ✗ Please enter a number between " + min + " and " + max + ".");
        }
    }

    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("today")) return LocalDate.now();
            try {
                return LocalDate.parse(input, FMT);
            } catch (DateTimeParseException e) {
                System.out.println("  ✗ Invalid date. Use format yyyy-MM-dd (e.g. 2025-06-15) or type 'today'.");
            }
        }
    }

    public static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) return true;
            if (input.equals("n") || input.equals("no")) return false;
            System.out.println("  ✗ Please enter y or n.");
        }
    }
}
