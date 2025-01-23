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

        outputInformation();

        System.out.println();

        selectAction();

        System.out.println();

        outputInformation();

        scanner.close();

    }

    private static void outputInformation() {
        System.out.printf("""
                The coffee machine has:
                %d ml of water
                %d ml of milk
                %d g of coffee beans
                %d disposable cups
                $%d of money
                """, countWater, countMilk, countCoffee, countCups, countMoney);
    }

    private static void selectAction() {
        System.out.println("Write action (buy, fill, take):");
        switch (scanner.nextLine()) {
            case "buy" -> buyCoffee();
            case "fill" -> fillIngridients();
            case "take" -> takeMoney();
            default -> throw new UnsupportedOperationException("Action may be only buy, fill or take");
        }
    }

    private static void buyCoffee() {
        String coffee = chooseCoffee();
        getMoneyFor(coffee);
        makeCoffee(coffee);
    }

    private static void getMoneyFor(String coffee) {
        switch (coffee) {
            case ESPRESSO -> countMoney += 4;
            case LATTE -> countMoney += 7;
            case CAPPUCCINO -> countMoney += 6;
        }
    }

    private static void makeCoffee(String coffee) {
        switch (coffee) {
            case ESPRESSO -> makeEspresso();
            case LATTE -> makeLatte();
            case CAPPUCCINO -> makeCapuccino();
        }
    }

    private static void makeEspresso() {
        countWater -= 250;
        countCoffee -= 16;
        countCups--;
    }

    private static void makeLatte() {
        countWater -= 350;
        countMilk -= 75;
        countCoffee -= 20;
        countCups--;
    }

    private static void makeCapuccino() {
        countWater -= 200;
        countMilk -= 100;
        countCoffee -= 12;
        countCups--;
    }

    private static String chooseCoffee() {
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:");
        var bufScanner = new Scanner(scanner.nextLine());
        String coffee = switch (bufScanner.nextInt()) {
            case 1 -> ESPRESSO;
            case 2 -> LATTE;
            case 3 -> CAPPUCCINO;
            default -> {
                bufScanner.close();
                throw new UnsupportedOperationException("You should select one of 3 coffees");
            }
        };
        bufScanner.close();
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
    }

    private static void takeMoney() {
        System.out.println("I gave you $" + countMoney);
        countMoney = 0;
    }
}
