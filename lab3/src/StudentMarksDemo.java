public class StudentMarksDemo {
    public static void main(String[] args) {
        Student student = new Student("Tinotenda", 87);
        System.out.println(student);

        student.setMarks(73);
        System.out.println("Updated: " + student);

        trySet(student, -3);
        trySet(student, 105);
        System.out.println("Marks stayed " + student.getMarks() + " after the rejected updates.");
    }

    private static void trySet(Student student, int marks) {
        try {
            student.setMarks(marks);
        } catch (IllegalArgumentException ex) {
            System.out.println("Rejected " + marks + ": " + ex.getMessage());
        }
    }
}
