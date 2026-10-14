public class Circle extends Shape {
    private final double radius;

    public Circle(double radius) {
        super("Circle");
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative.");
        }
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }
}