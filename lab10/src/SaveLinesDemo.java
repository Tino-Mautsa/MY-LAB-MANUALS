import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Save five typed lines then read the file back.
// Run this from the project folder so data/five-lines.txt lands next to pom.xml.

public class SaveLinesDemo {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        List<String> lines = new ArrayList<>();
        System.out.println("Type five lines. Press Enter after each one.");
        for (int i = 1; i <= 5; i++) {
            System.out.print(i + ": ");
            lines.add(scanner.nextLine());
        }

        Path file = Path.of("data", "five-lines.txt");
        Files.createDirectories(file.getParent());
        Files.write(file, lines);

        System.out.println();
        System.out.println("Saved to " + file.toAbsolutePath());
        System.out.println("Read back:");
        int number = 1;
        for (String line : Files.readAllLines(file)) {
            System.out.println(number + ": " + line);
            number++;
        }
    }
}
