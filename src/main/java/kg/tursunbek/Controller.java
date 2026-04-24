package kg.tursunbek;

import java.util.*;

/** Class with the method that takes user's input.
 */
public class Controller {

    private static final String[] VALID_INPUT = {"1", "2"};

    /**
     *class method for getting the user's input. The method insures that the input is either "1" or "2".
     */
    public static String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        List<String> temp = Arrays.asList(VALID_INPUT);

        while (true) {
            System.out.println("Please enter the number of action (1 or 2):");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Invalid input");
                continue;
            }

            if (input.contains(" ") || input.contains("\n")) {
                System.out.println("Invalid input");
            } else {
                if (temp.contains(input)) {
                    scanner.close();
                    return input;
                }
            }
        }
    }
}
