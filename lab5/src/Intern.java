
  //Added onto the existing Manager / Developer hierarchy
 //Payroll.total was not changed to know about interns

public class Intern extends Employee {
    private final double stipend;

    public Intern(String name, double stipend) {
        super(name);
        this.stipend = requirePay(stipend);
    }

    @Override
    public double monthlyPay() {
        return stipend;
    }
}