package library;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Writes items.csv, members.csv and loans.csv, then reads them back on the next run.

public class CsvLibraryRepository {
    public boolean exists(Path directory) {
        return Files.isRegularFile(directory.resolve("items.csv"));
    }

    public void save(Library library, Path directory) throws IOException {
        Files.createDirectories(directory);
        write(directory.resolve("items.csv"), itemLines(library));
        write(directory.resolve("members.csv"), memberLines(library));
        write(directory.resolve("loans.csv"), loanLines(library));
    }

    public void load(Library library, Path directory) throws IOException {
        List<LibraryItem> loadedItems = readItems(directory.resolve("items.csv"));
        List<Member> loadedMembers = readMembers(directory.resolve("members.csv"));
        List<Loan> loadedLoans = readLoans(directory.resolve("loans.csv"));
        library.clear();
        try {
            for (LibraryItem item : loadedItems) {
                library.loadItem(item);
            }
            for (Member member : loadedMembers) {
                library.loadMember(member);
            }
            for (Loan loan : loadedLoans) {
                library.loadLoan(loan);
            }
            library.restoreOpenLoans();
        } catch (RuntimeException | ItemNotAvailableException | MemberLimitExceededException ex) {
            library.clear();
            throw new IOException("Saved library data is inconsistent: " + ex.getMessage(), ex);
        }
    }

    private static List<String> itemLines(Library library) {
        List<String> lines = new ArrayList<>();
        lines.add("type,id,title,extra1,extra2");
        for (LibraryItem item : library.getItems()) {
            List<String> fields = new ArrayList<>();
            fields.add(item.typeCode());
            fields.add(item.getId());
            fields.add(item.getTitle());
            fields.addAll(item.csvExtras());
            lines.add(Csv.line(fields));
        }
        return lines;
    }

    private static List<String> memberLines(Library library) {
        List<String> lines = new ArrayList<>();
        lines.add("id,name");
        for (Member member : library.getMembers()) {
            lines.add(Csv.line(List.of(member.getId(), member.getName())));
        }
        return lines;
    }

    private static List<String> loanLines(Library library) {
        List<String> lines = new ArrayList<>();
        lines.add("id,itemId,memberId,borrowedOn,dueOn,returnedOn");
        for (Loan loan : library.getLoans()) {
            String returned = loan.getReturnedOn() == null ? "" : loan.getReturnedOn().toString();
            lines.add(Csv.line(List.of(
                    loan.getId(),
                    loan.getItemId(),
                    loan.getMemberId(),
                    loan.getBorrowedOn().toString(),
                    loan.getDueOn().toString(),
                    returned)));
        }
        return lines;
    }

    private static List<LibraryItem> readItems(Path file) throws IOException {
        List<LibraryItem> items = new ArrayList<>();
        for (List<String> fields : rows(file)) {
            try {
                items.add(LibraryItemFactory.fromCsv(fields));
            } catch (RuntimeException ex) {
                throw new IOException("Could not read an item row: " + ex.getMessage(), ex);
            }
        }
        return items;
    }

    private static List<Member> readMembers(Path file) throws IOException {
        List<Member> members = new ArrayList<>();
        for (List<String> fields : rows(file)) {
            members.add(new Member(field(fields, 0), field(fields, 1)));
        }
        return members;
    }

    private static List<Loan> readLoans(Path file) throws IOException {
        List<Loan> loans = new ArrayList<>();
        for (List<String> fields : rows(file)) {
            loans.add(new Loan(
                    field(fields, 0),
                    field(fields, 1),
                    field(fields, 2),
                    LocalDate.parse(field(fields, 3)),
                    LocalDate.parse(field(fields, 4)),
                    dateOrNull(field(fields, 5))));
        }
        return loans;
    }

    private static List<List<String>> rows(Path file) throws IOException {
        List<List<String>> rows = new ArrayList<>();
        if (!Files.isRegularFile(file)) {
            return rows;
        }
        boolean header = true;
        for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            if (line.isBlank()) {
                continue;
            }
            if (header) {
                header = false;
                continue;
            }
            rows.add(Csv.parse(line));
        }
        return rows;
    }

    private static String field(List<String> fields, int index) {
        if (index >= fields.size()) {
            return "";
        }
        return fields.get(index).trim();
    }

    private static LocalDate dateOrNull(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        return LocalDate.parse(text.trim());
    }

    private static void write(Path file, List<String> lines) throws IOException {
        Files.write(file, lines, StandardCharsets.UTF_8);
    }
}
