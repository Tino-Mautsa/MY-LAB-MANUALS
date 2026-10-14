package library;

public interface LibraryObserver {
    void onBorrowed(Loan loan, LibraryItem item, Member member);

    void onReturned(Loan loan, LibraryItem item, Member member);
}
