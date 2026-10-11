// A mixed Employee array including Intern
 //The payroll loop calls monthlyPay() and does not mention any subclass
public class PayrollDemo {
    public static void main(String[] args) {
        Employee[] staff = {
                new Manager("Kelvin Mautsa", 8000),
                new Developer("Fortunate Mautsa", 6000, 750),
                new Intern("Itai Mautsa", 1500),
                new Developer("Ngoni Mautsa", 5500, 400)
        };

        double total = totalPay(staff);
        for (Employee employee : staff) {
            System.out.printf("%-16s %8.2f%n", employee.getName(), employee.monthlyPay());
        }
        System.out.printf("Total payroll    %8.2f%n", total);
    }


     //Written against Employee only and adding Intern did not change this loop

    static double totalPay(Employee[] staff) {
        double total = 0;
        for (Employee employee : staff) {
            total += employee.monthlyPay();
        }
        return total;
    }
}