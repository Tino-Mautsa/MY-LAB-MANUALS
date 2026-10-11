public class Student {
    private String name;
    private int marks;

    public Student(String name, int marks) {
        setName(name);
        setMarks(marks);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required.");
        }
        this.name = name.trim();
    }

    public int getMarks() {
        return marks;
    }

    //Marks are only stored when they fall in 0 to 100.
    //The constructor calls this setter, so it cannot bypass the rule
    public void setMarks(int marks) {
        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException("Marks must be from 0 to 100. Got " + marks + ".");
        }
        this.marks = marks;
    }

    @Override
    public String toString() {
        return name + " scored " + marks;
    }
}
