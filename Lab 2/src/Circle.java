public class Circle {
    private final double radius;

    /**
     * No-argument constructor. The radius defaults to 1.0.
     * this(1.0) sends that work to the other constructor so the check lives in one place.
     */
    public Circle() {
        this(1.0);
    }

    public Circle(double radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Radius cannot be negative.");
        }
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public String toString() {
        return "Circle radius=" + radius + ", area=" + area();
    }
}
