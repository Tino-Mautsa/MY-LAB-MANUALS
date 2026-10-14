import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//US coins. values() is declaration order, smallest first.
//fewestCoins walks from the largest value, which is optimal for these denominations.

public enum Coin {
    PENNY(1),
    NICKEL(5),
    DIME(10),
    QUARTER(25);

    private final int cents;

    Coin(int cents) {
        this.cents = cents;
    }

    public int cents() {
        return cents;
    }

    public static List<Coin> fewestCoins(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        List<Coin> result = new ArrayList<>();
        Coin[] coins = values();
        int remaining = amount;
        for (int i = coins.length - 1; i >= 0 && remaining > 0; i--) {
            while (remaining >= coins[i].cents) {
                result.add(coins[i]);
                remaining -= coins[i].cents;
            }
        }
        return Collections.unmodifiableList(result);
    }
}