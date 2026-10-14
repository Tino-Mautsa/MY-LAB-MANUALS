//The loop calls area() and perimeter() without asking which shape it has.

public class ShapeDemo {
    public static void main(String[] args) {
        Shape[] shapes = {
                new Circle(3),
                new Rectangle(4, 5),
                new Square(6)
        };

        for (Shape shape : shapes) {
            System.out.println(shape);
        }
    }
}