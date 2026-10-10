package lab01;

// A car described by its brand, the year it was made and its mileage
public class Car {
    private final String brand;
    private final int year;
    private final double mileage;

    public Car(String brand, int year, double mileage) {
        this.brand = brand;
        this.year = year;
        this.mileage = mileage;
    }

    public void display() {
        System.out.println(brand + " (" + year + ") - " + mileage + " km");
    }
}
