package library;

public class ConsoleLibraryObserver implements LibraryObserver {
    @Override
    public void onBorrowed(Loan loan, LibraryItem item, Member member) {
        System.out.println("[notice] " + member.getName() + " borrowed \"" + item.getTitle()
                + "\" due " + loan.getDueOn());
    }

    @Override
    public void onReturned(Loan loan, LibraryItem item, Member member) {
        System.out.println("[notice] " + member.getName() + " returned \"" + item.getTitle() + "\"");
    }
}
