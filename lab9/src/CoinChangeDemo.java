import java.util.List;

//The fewest coins that add up to a number of cents.

public class CoinChangeDemo {
    public static void main(String[] args) {
        printChange(0);
        printChange(1);
        printChange(30);
        printChange(41);
        printChange(99);
    }

    private static void printChange(int cents) {
        List<Coin> coins = Coin.fewestCoins(cents);
        System.out.println(cents + " cents (" + coins.size() + " coins): " + coins);
    }
}