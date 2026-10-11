public abstract class Employee {
    private final String name;

    protected Employee(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required.");
        }
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    public abstract double monthlyPay();

    protected static double requirePay(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Pay cannot be negative.");
        }
        return amount;
    }
}