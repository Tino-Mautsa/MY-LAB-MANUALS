//validateAge throws a checked exception outside 0 to 120.

public class AgeValidationDemo {
    public static void validateAge(int age) throws InvalidAgeException {
        if (age < 0 || age > 120) {
            throw new InvalidAgeException("Age must be from 0 to 120. Got " + age + ".");
        }
    }

    public static void main(String[] args) {
        int[] samples = {0, 20, 120, -1, 121};
        for (int age : samples) {
            try {
                validateAge(age);
                System.out.println(age + " is valid.");
            } catch (InvalidAgeException ex) {
                System.out.println(age + " is invalid: " + ex.getMessage());
            }
        }
    }
}