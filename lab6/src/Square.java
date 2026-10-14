//Square implements Shape directly.
//Extending Rectangle would let a square be treated as a rectangle with two

public class Square extends Shape {
    private final double side;

    public Square(double side) {
        super("Square");
        if (side < 0) {
            throw new IllegalArgumentException("Side cannot be negative.");
        }
        this.side = side;
    }

    @Override
    public double area() {
        return side * side;
    }

    @Override
    public double perimeter() {
        return 4 * side;
    }
}