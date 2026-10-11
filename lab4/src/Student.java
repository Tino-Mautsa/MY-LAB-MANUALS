public class Student extends Person {
    private final String course;

    public Student(String name, int age, String course) {
        super(name, age);
        this.course = course;
    }

    public String getCourse() {
        return course;
    }


    //Uses the inherited display() and then adds the course that only a student has.
    @Override
    public void display() {
        super.display();
        System.out.println("Course: " + course);
    }
}