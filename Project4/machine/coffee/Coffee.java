package machine.coffee;

import machine.resources.CoffeeBeans;
import machine.resources.Milk;
import machine.resources.Money;
import machine.resources.Water;

public abstract class Coffee {

    protected Water water = new Water();
    protected CoffeeBeans coffeeBeans = new CoffeeBeans();
    protected Milk milk = new Milk();
    protected Money money = new Money();
    /**
     * @return the water
     */
    public int getCountWater() {
        return water.getCount();
    }
    /**
     * @return the milk
     */
    public int getCountMilk() {
        return milk.getCount();
    }
    /**
     * @return the coffeeBeans
     */
    public int getCountCoffeeBeans() {
        return coffeeBeans.getCount();
    }

    public int getPrice() {
        return money.getCount();
    }
}
