package machine;

import java.util.Arrays;
import java.util.Scanner;

import machine.coffee.Capuccino;
import machine.coffee.Coffee;
import machine.coffee.Espresso;
import machine.coffee.Latte;
import machine.resources.CoffeeBeans;
import machine.resources.Cup;
import machine.resources.Milk;
import machine.resources.Money;
import machine.resources.Water;

public class CoffeeMachine {

    private CoffeeMachine() {
    }

    private static final CoffeeMachine coffeeMachine = new CoffeeMachine();
    private static final Scanner scanner = new Scanner(System.in);

    private enum Action {
        BUY("buy"),
        FILL("fill"),
        TAKE("take"),
        CLEAN("clean"),
        REMAINING("remaining"),
        EXIT("exit");

        private final String actionString;

        Action(String action) {
            this.actionString = action;
        }

        @Override
        public String toString() {
            return actionString;
        }
    }

    private final Water water = new Water(400);
    private final Milk milk = new Milk(540);
    private final CoffeeBeans coffeeBeans = new CoffeeBeans(120);
    private final Cup cup = new Cup(9);
    private final Money money = new Money(550);
    private boolean isNeedClean = false;
    private int countPreparedCups;

    public static void main(String[] args) {

        coffeeMachine.start();

        scanner.close();
    }

    private void start() {
        while (requestsAction()) {
            System.out.println();
        }
    }

    private void outputRemaining() {
        System.out.printf("""
                The coffee machine has:
                %d ml of water
                %d ml of milk
                %d g of coffee beans
                %d disposable cups
                $%d of money
                """,
                water.getCount(),
                milk.getCount(),
                coffeeBeans.getCount(),
                cup.getCount(),
                money.getCount());
    }

    private boolean requestsAction() {
        final String LIST_ACTIONS = getListActions();
        System.out.printf("Write action (%s):%n", LIST_ACTIONS);

        final String NO_ACTION = String.format("Action may be only %s", getListActions());

        boolean isAvailableActions = true;
        switch (getAction()) {

            case Action.BUY -> buyCoffee();

            case Action.FILL -> {
                System.out.println();
                coffeeMachine.fillIngridients();
            }

            case Action.TAKE -> {
                System.out.println();
                coffeeMachine.takeMoney();
            }

            case Action.REMAINING -> {
                System.out.println();
                coffeeMachine.outputRemaining();
            }

            case Action.CLEAN -> clean();

            case Action.EXIT -> isAvailableActions = false;

            case null -> throw new UnsupportedOperationException(NO_ACTION);

        }
        return isAvailableActions;
    }

    private void clean() {
        setCountPreparedCups(0);
        isNeedClean = false;
        System.out.println("I have been cleaned!");
    }

    private static Action getAction() {
        String actionString = scanner.nextLine().trim();
        for (Action action : Action.values()) {
            if (actionString.equals(action.toString())) {
                return action;
            }
        }
        return null;
    }

    private static String getListActions() {
        String regex = "[\\[\\]]";
        String arraysString = Arrays.toString(Action.values());
        return arraysString.replaceAll(regex, "");
    }

    private static void buyCoffee() {
        if (!coffeeMachine.isNeedClean) {
            System.out.println();

            Coffee coffee = chooseCoffee();

            if (coffee != null && coffeeMachine.checkResourcesFor(coffee)) {
                coffeeMachine.getMoneyFor(coffee);
                coffeeMachine.makeCoffee(coffee);
            }
        } else {
            System.out.println("I need cleaning!");
        }

    }

    private boolean checkResourcesFor(Coffee typeCoffee) {
        final String NOT_ENOUGH_RESOURCES = "Sorry, not enough ";
        final String ENOUGH_RESOURCES = "I have enough resources, making you a coffee!";

        int countWater = water.getCount() - typeCoffee.getCountWater();
        int countMilk = milk.getCount() - typeCoffee.getCountMilk();
        int countCoffee = coffeeBeans.getCount() - typeCoffee.getCountCoffeeBeans();
        int countCups = cup.getCount() - 1;

        if (countMilk < 0) {
            System.out.println(NOT_ENOUGH_RESOURCES + "milk!");
            return false;
        }

        if (countWater < 0) {
            System.out.println(NOT_ENOUGH_RESOURCES + "water!");
            return false;
        }

        if (countCoffee < 0) {
            System.out.println(NOT_ENOUGH_RESOURCES + "coffee!");
            return false;
        }

        if (countCups < 0) {
            System.out.println(NOT_ENOUGH_RESOURCES + "cups!");
            return false;
        }

        System.out.println(ENOUGH_RESOURCES);
        return true;
    }

    private void getMoneyFor(Coffee typeCoffee) {
        money.add(typeCoffee.getPrice());
    }

    private void makeCoffee(Coffee coffee) {
        water.reduce(coffee.getCountWater());
        milk.reduce(coffee.getCountMilk());
        coffeeBeans.reduce(coffee.getCountCoffeeBeans());
        cup.reduce(1);

        setCountPreparedCups(getCountPreparedCups() + 1);

        if (getCountPreparedCups() == 10) {
            isNeedClean = true;
        }
    }

    private static Coffee chooseCoffee() {
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");

        return switch (scanner.nextLine().trim()) {
            case "1" -> new Espresso();
            case "2" -> new Latte();
            case "3" -> new Capuccino();
            case "back" -> null;
            default -> throw new UnsupportedOperationException("You should select one of 3 coffees or action back");
        };
    }

    private void fillIngridients() {
        System.out.println("Write how many ml of water you want to add:");
        water.add(scanner.nextInt());

        System.out.println("Write how many ml of milk you want to add:");
        milk.add(scanner.nextInt());

        System.out.println("Write how many grams of coffee beans you want to add:");
        coffeeBeans.add(scanner.nextInt());

        System.out.println("Write how many disposable cups you want to add:");
        cup.add(scanner.nextInt());

        scanner.nextLine();
    }

    private void takeMoney() {
        int currentMoney = money.getCount();
        System.out.println("I gave you $" + currentMoney);
        money.reduce(currentMoney);
    }

    /**
     * @return the countPreparedCups
     */
    public int getCountPreparedCups() {
        return countPreparedCups;
    }

    /**
     * @param countPreparedCups the countPreparedCups to set
     */
    public void setCountPreparedCups(int countPreparedCups) {
        if (countPreparedCups >= 0) {
            this.countPreparedCups = countPreparedCups;
        }
    }

    public boolean isNeedClean() {
        return isNeedClean;
    }
}
