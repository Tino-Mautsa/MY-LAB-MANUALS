package library;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

// Menu for the library mini-project.
 // Run this class from the project folder. Data is stored in data/library.

public class LibraryApp {
    public static void main(String[] args) {
        Path directory = Path.of("data", "library");
        Library library = new Library(new DailyFineStrategy(0.50));
        library.addObserver(new ConsoleLibraryObserver());
        CsvLibraryRepository repository = new CsvLibraryRepository();

        try {
            if (repository.exists(directory)) {
                repository.load(library, directory);
                System.out.println("Loaded saved data from " + directory.toAbsolutePath());
            } else {
                System.out.println("No saved data found. Creating a demo library in " + directory.toAbsolutePath());
                library.seedDemoData();
                repository.save(library, directory);
            }
        } catch (IOException | ItemNotAvailableException | MemberLimitExceededException ex) {
            System.out.println("Could not start the library: " + ex.getMessage());
            return;
        }

        System.out.println("Fine rule: " + library.finePolicy());
        System.out.println("A member may have at most " + Member.MAX_LOANS + " loans at once.");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addItem(library, scanner);
                    case "2" -> registerMember(library, scanner);
                    case "3" -> borrow(library, scanner);
                    case "4" -> giveBack(library, scanner);
                    case "5" -> listOverdue(library);
                    case "6" -> search(library, scanner);
                    case "7" -> listItems(library);
                    case "8" -> listMembers(library);
                    case "0" -> running = false;
                    default -> System.out.println("Choose a number from the menu.");
                }
                if (running && !"5".equals(choice) && !"6".equals(choice) && !"7".equals(choice) && !"8".equals(choice)) {
                    repository.save(library, directory);
                }
            } catch (ItemNotAvailableException | MemberLimitExceededException ex) {
                System.out.println("Not allowed: " + ex.getMessage());
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("Check the input: " + ex.getMessage());
            } catch (IOException ex) {
                System.out.println("Could not save: " + ex.getMessage());
            }
        }

        try {
            repository.save(library, directory);
            System.out.println("Saved to " + directory.toAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Could not save on exit: " + ex.getMessage());
        }
        System.out.println("Goodbye.");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("Library");
        System.out.println("1  Add item");
        System.out.println("2  Register member");
        System.out.println("3  Borrow");
        System.out.println("4  Return");
        System.out.println("5  List overdue items");
        System.out.println("6  Search by title");
        System.out.println("7  List items");
        System.out.println("8  List members");
        System.out.println("0  Save and exit");
        System.out.print("> ");
    }

    private static void addItem(Library library, Scanner scanner) {
        String kind = ask(scanner, "Type book or dvd: ").toLowerCase();
        String title = ask(scanner, "Title: ");
        if ("book".equals(kind)) {
            String author = ask(scanner, "Author: ");
            String isbn = ask(scanner, "ISBN: ");
            Book book = library.addBook(title, author, isbn);
            System.out.println("Added " + book);
        } else if ("dvd".equals(kind)) {
            int runtime = askPositiveInt(scanner, "Runtime in minutes: ");
            Dvd dvd = library.addDvd(title, runtime);
            System.out.println("Added " + dvd);
        } else {
            throw new IllegalArgumentException("Type must be book or dvd.");
        }
    }

    private static void registerMember(Library library, Scanner scanner) {
        Member member = library.registerMember(ask(scanner, "Member name: "));
        System.out.println("Registered " + member);
    }

    private static void borrow(Library library, Scanner scanner) throws ItemNotAvailableException, MemberLimitExceededException {
        listItems(library);
        listMembers(library);
        Loan loan = library.borrow(ask(scanner, "Item id: "), ask(scanner, "Member id: "));
        System.out.println("Loan recorded: " + loan);
    }

    private static void giveBack(Library library, Scanner scanner) throws ItemNotAvailableException {
        listItems(library);
        Loan loan = library.giveBack(ask(scanner, "Item id to return: "));
        System.out.println("Closed " + loan);
    }

    private static void listOverdue(Library library) {
        LocalDate today = LocalDate.now();
        var overdue = library.overdueLoans(today);
        if (overdue.isEmpty()) {
            System.out.println("Nothing is overdue.");
            return;
        }
        for (Loan loan : overdue) {
            String title = library.findItem(loan.getItemId()).map(LibraryItem::getTitle).orElse(loan.getItemId());
            String member = library.findMember(loan.getMemberId()).map(Member::getName).orElse(loan.getMemberId());
            long days = ChronoUnit.DAYS.between(loan.getDueOn(), today);
            System.out.printf("%s | %s | %s | due %s | %d day(s) late | fine %.2f%n",
                    loan.getId(), title, member, loan.getDueOn(), days, library.fineFor(loan, today));
        }
    }

    private static void search(Library library, Scanner scanner) {
        String query = ask(scanner, "Title contains: ");
        var matches = library.searchByTitle(query);
        if (matches.isEmpty()) {
            System.out.println("No titles contain \"" + query + "\".");
            return;
        }
        for (LibraryItem item : matches) {
            System.out.println(item);
        }
    }

    private static void listItems(Library library) {
        if (library.getItems().isEmpty()) {
            System.out.println("No items yet.");
            return;
        }
        for (LibraryItem item : library.getItems()) {
            System.out.println(item);
        }
    }

    private static void listMembers(Library library) {
        if (library.getMembers().isEmpty()) {
            System.out.println("No members yet.");
            return;
        }
        for (Member member : library.getMembers()) {
            System.out.println(member);
        }
    }

    private static String ask(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int askPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            String text = ask(scanner, prompt);
            try {
                int value = Integer.parseInt(text);
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ex) {
                // Ask again below.
            }
            System.out.println("Enter a whole number greater than 0.");
        }
    }
}
