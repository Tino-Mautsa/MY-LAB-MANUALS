import java.util.EmptyStackException;

//The same Stack class holds strings, then integers.

public class GenericStackDemo {
    public static void main(String[] args) {
        Stack<String> words = new Stack<>();
        words.push("alpha");
        words.push("beta");
        System.out.println("Peek: " + words.peek());
        System.out.println("Pop: " + words.pop());
        System.out.println("Pop: " + words.pop());
        System.out.println("Empty: " + words.isEmpty());
        try {
            words.pop();
        } catch (EmptyStackException ex) {
            System.out.println("Pop on an empty stack throws EmptyStackException.");
        }

        Stack<Integer> numbers = new Stack<>();
        numbers.push(10);
        numbers.push(20);
        System.out.println("Integer stack pops " + numbers.pop() + " then " + numbers.pop());
    }
}