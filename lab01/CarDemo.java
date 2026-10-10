package lab01;

public class CarDemo {
    public static void main(String[] args) {
        Car first = new Car("Toyota", 2018, 45200);
        Car second = new Car("Honda", 2021, 18350.5);
        Car third = new Car("Ford", 2015, 98000);

        first.display();
        second.display();
        third.display();
    }
}
