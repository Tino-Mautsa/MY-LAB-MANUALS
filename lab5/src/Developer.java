public class Developer extends Employee {
    private final double salary;
    private final double allowance;

    public Developer(String name, double salary, double allowance) {
        super(name);
        this.salary = requirePay(salary);
        this.allowance = requirePay(allowance);
    }

    @Override
    public double monthlyPay() {
        return salary + allowance;
    }
}
