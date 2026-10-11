public class Manager extends Employee {
    private final double salary;

    public Manager(String name, double salary) {
        super(name);
        this.salary = requirePay(salary);
    }

    @Override
    public double monthlyPay() {
        return salary;
    }
}