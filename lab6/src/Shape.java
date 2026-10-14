public abstract class Shape {
    private final String name;

    protected Shape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract double area();

    public abstract double perimeter();

    @Override
    public String toString() {
        return name + " area=" + area() + " perimeter=" + perimeter();
    }
}