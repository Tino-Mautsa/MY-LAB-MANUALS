package library;

import java.util.List;

// Callers ask for a book or a DVD by name. Reading a CSV row comes through here too,
 // so the repository does not construct Book or Dvd itself.

public final class LibraryItemFactory {
    private LibraryItemFactory() {
    }

    public static Book book(String id, String title, String author, String isbn) {
        return new Book(id, title, author, isbn);
    }

    public static Dvd dvd(String id, String title, int runtimeMinutes) {
        return new Dvd(id, title, runtimeMinutes);
    }

    public static LibraryItem fromCsv(List<String> fields) {
        String type = fields.isEmpty() ? "" : fields.get(0);
        if ("BOOK".equalsIgnoreCase(type)) {
            return book(text(fields, 1), text(fields, 2), text(fields, 3), text(fields, 4));
        }
        if ("DVD".equalsIgnoreCase(type)) {
            return dvd(text(fields, 1), text(fields, 2), Integer.parseInt(text(fields, 3)));
        }
        throw new IllegalArgumentException("Unknown item type: " + type);
    }

    private static String text(List<String> fields, int index) {
        if (index >= fields.size()) {
            return "";
        }
        return fields.get(index).trim();
    }
}
