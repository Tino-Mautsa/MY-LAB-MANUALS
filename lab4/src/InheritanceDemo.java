public class InheritanceDemo {
    public static void main(String[] args) {
        Person person = new Person("Tino Mautsa", 21);
        Student student = new Student("Tino Mautsa", 20, "Software Engineering");

        System.out.println("Person.display()");
        person.display();
        System.out.println("Student.display()");
        student.display();
    }
}

