package machine;

import java.util.Scanner;

public class CoffeeMachine {

    private static final int WATER_ONE_CUP = 200;
    private static final int MILK_ONE_CUP = 50;
    private static final int COFFEE_ONE_CUP = 15;
    private static final int[] requiredIngridientsOneCup = { WATER_ONE_CUP, MILK_ONE_CUP, COFFEE_ONE_CUP };
    private static final String[] ingridientRequests = {
            "Write how many ml of water the coffee machine has:",
            "Write how many ml of milk the coffee machine has:",
            "Write how many grams of coffee beans the coffee machine has:"
    };
    private static final String INGRIDIENTS_ENOUGH = "Yes, I can make that amount of coffee";
    private static final String INGRIDIENTS_MORE = "Yes, I can make that amount of coffee (and even %d more than that)";
    private static final String INGRIDIENTS_NOT_ENOUGH = "No, I can make only %d cup(s) of coffee";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        
        int[] ingridients = getIngridients(scanner);

        int requestedCountCups = requestCountCups(scanner);

        int currentCountCups = calculateCupsFrom(ingridients);

        String message = getMessage(requestedCountCups, currentCountCups);

        System.out.println(message);

        scanner.close();

    }

    private static String getMessage(int requestedCountCups, int currentCountCups) {
        String message = INGRIDIENTS_NOT_ENOUGH;

        if (currentCountCups >= requestedCountCups) {
            message = currentCountCups == requestedCountCups ? INGRIDIENTS_ENOUGH : INGRIDIENTS_MORE;
            currentCountCups -= requestedCountCups;
        }
        return String.format(message, currentCountCups);
    }

    private static int calculateCupsFrom(int[] ingridients) {
        int minCountCups = Integer.MAX_VALUE;
        for (int i = 0; i < ingridients.length; i++) {
            minCountCups = Math.min(minCountCups, ingridients[i] / requiredIngridientsOneCup[i]);
        }
        return minCountCups;
    }

    private static int requestCountCups(Scanner scanner) {
        System.out.println("Write how many cups of coffee you will need:");
        return scanner.nextInt();
    }

    private static int[] getIngridients(Scanner scanner) {

        int[] ingridients = new int[ingridientRequests.length];

        for (int i = 0; i < ingridients.length; i++) {
            System.out.println(ingridientRequests[i]);
            ingridients[i] = scanner.nextInt();
        }

        return ingridients;
    }
}
