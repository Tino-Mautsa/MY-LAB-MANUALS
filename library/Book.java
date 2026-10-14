package library;

import java.util.List;

public class Book extends LibraryItem {
    private final String author;
    private final String isbn;

    public Book(String id, String title, String author, String isbn) {
        super(id, title);
        this.author = Checks.required(author, "Author");
        this.isbn = Checks.required(isbn, "ISBN");
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    @Override
    public int loanPeriodDays() {
        return 14;
    }

    @Override
    public String typeCode() {
        return "BOOK";
    }

    @Override
    public List<String> csvExtras() {
        return List.of(author, isbn);
    }

    @Override
    public String toString() {
        return getId() + " | Book | " + getTitle() + " | " + author
                + " | " + (isAvailable() ? "available" : "on loan")
                + " | " + loanPeriodDays() + " days";
    }
}
