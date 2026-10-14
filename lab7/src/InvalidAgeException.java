//A checked exception and callers must catch it or declare throws

public class InvalidAgeException extends Exception {
    public InvalidAgeException(String message) {
        super(message);
    }
}