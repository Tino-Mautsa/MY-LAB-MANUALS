public class CircleDemo {
    public static void main(String[] args) {
        Circle defaultCircle = new Circle();
        Circle customCircle = new Circle(4.5);

        System.out.println("No-argument constructor: " + defaultCircle);
        System.out.println("Radius constructor:      " + customCircle);
    }
}
