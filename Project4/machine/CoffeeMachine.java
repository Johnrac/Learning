package machine;

import java.util.Scanner;

public class CoffeeMachine {

    private static final String CAPPUCCINO = "cappuccino";
    private static final String LATTE = "latte";
    private static final String ESPRESSO = "espresso";

    private static final Scanner scanner = new Scanner(System.in);

    private static int countWater = 400;
    private static int countMoney = 550;
    private static int countMilk = 540;
    private static int countCoffee = 120;
    private static int countCups = 9;

    public static void main(String[] args) {

        while (isSelectAction()) {
            System.out.println();
        }

        scanner.close();

    }

    private static void outputRemaining() {
        System.out.printf("""
                The coffee machine has:
                %d ml of water
                %d ml of milk
                %d g of coffee beans
                %d disposable cups
                $%d of money
                """, countWater, countMilk, countCoffee, countCups, countMoney);
    }

    private static boolean isSelectAction() {
        System.out.println("Write action (buy, fill, take, remaining, exit):");

        boolean isAvailableActions = true;
        switch (scanner.nextLine()) {
            case "buy" -> {
                System.out.println();
                buyCoffee();
            }
            case "fill" -> {
                System.out.println();
                fillIngridients();
            }
            case "take" -> {
                System.out.println();
                takeMoney();
            }
            case "remaining" -> {
                System.out.println();
                outputRemaining();
            }
            case "exit" -> isAvailableActions = false;
            default -> throw new UnsupportedOperationException("Action may be only buy, fill or take");
        }

        return isAvailableActions;
    }

    private static void buyCoffee() {
        String coffee = chooseCoffee();
        if (!coffee.equals("") && checkResourcesFor(coffee)) {
            getMoneyFor(coffee);
            makeCoffee(coffee);
        }
    }

    private static boolean checkResourcesFor(String typeCoffee) {
        final String NOT_ENOUGH_RESOURCES = "Sorry, not enough ";
        final String ENOUGH_RESOURCES = "I have enough resources, making you a coffee!";
        if (countMilk - getNeedCount("milk", typeCoffee) < 0) {
            System.out.println(NOT_ENOUGH_RESOURCES + "milk!");
            return false;
        }
        if (countWater - getNeedCount("water", typeCoffee) < 0) {
            System.out.println(NOT_ENOUGH_RESOURCES + "water!");
            return false;
        }
        if (countCoffee - getNeedCount("coffee", typeCoffee) < 0) {
            System.out.println(NOT_ENOUGH_RESOURCES + "coffee!");
        }
        System.out.println(ENOUGH_RESOURCES);
        return true;
    }

    private static int getNeedCount(String component, String typeCoffee) {
        return switch (component) {
            case "milk" -> switch (typeCoffee) {
                case ESPRESSO -> 0;
                case LATTE -> 75;
                case CAPPUCCINO -> 100;
                default -> throw new UnsupportedOperationException("Milk does not exists");
            };
            case "water" -> switch (typeCoffee) {
                case ESPRESSO -> 250;
                case LATTE -> 350;
                case CAPPUCCINO -> 200;
                default -> throw new UnsupportedOperationException("Water does not exists");
            };
            case "coffee" -> switch (typeCoffee) {
                case ESPRESSO -> 16;
                case LATTE -> 20;
                case CAPPUCCINO -> 12;
                default -> throw new UnsupportedOperationException("Coffee does not exists");
            };
            case "money" -> switch (typeCoffee) {
                case ESPRESSO -> 4;
                case LATTE -> 7;
                case CAPPUCCINO -> 6;
                default -> throw new UnsupportedOperationException("Coffee does not exists");
            };
            default -> throw new UnsupportedOperationException("Component does not exists");
        };
    }

    private static void getMoneyFor(String typeCoffee) {
        countMoney += getNeedCount("money", typeCoffee);
    }

    private static void makeCoffee(String typeCoffee) {
        countWater -= getNeedCount("water", typeCoffee);
        countMilk -= getNeedCount("milk", typeCoffee);
        countCoffee -= getNeedCount("coffee", typeCoffee);
        countCups--;
    }

    private static String chooseCoffee() {
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");

        String coffee = switch (scanner.nextLine()) {
            case "1" -> ESPRESSO;
            case "2" -> LATTE;
            case "3" -> CAPPUCCINO;
            case "back" -> "";
            default -> {
                throw new UnsupportedOperationException("You should select one of 3 coffees or action back");
            }
        };

        return coffee;
    }

    private static void fillIngridients() {
        System.out.println("Write how many ml of water you want to add:");
        countWater += scanner.nextInt();
        System.out.println("Write how many ml of milk you want to add:");
        countMilk += scanner.nextInt();
        System.out.println("Write how many grams of coffee beans you want to add:");
        countCoffee += scanner.nextInt();
        System.out.println("Write how many disposable cups you want to add:");
        countCups += scanner.nextInt();
        scanner.nextLine();
    }

    private static void takeMoney() {
        System.out.println("I gave you $" + countMoney);
        countMoney = 0;
    }
}
