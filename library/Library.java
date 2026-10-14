package library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Items, members and loans. Borrowing uses the item's own loan period,
// the member's limit, and the current fine strategy.

public class Library {
    private final Map<String, LibraryItem> items = new LinkedHashMap<>();
    private final Map<String, Member> members = new LinkedHashMap<>();
    private final Map<String, Loan> loans = new LinkedHashMap<>();
    private final Map<String, Integer> nextNumbers = new LinkedHashMap<>();
    private final FineStrategy fineStrategy;
    private final List<LibraryObserver> observers = new ArrayList<>();

    public Library(FineStrategy fineStrategy) {
        if (fineStrategy == null) {
            throw new IllegalArgumentException("A fine strategy is required.");
        }
        this.fineStrategy = fineStrategy;
    }

    public void addObserver(LibraryObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer is required.");
        }
        observers.add(observer);
    }

    public String finePolicy() {
        return fineStrategy.name();
    }

    public Book addBook(String title, String author, String isbn) {
        Book book = LibraryItemFactory.book(nextId("B"), title, author, isbn);
        items.put(book.getId(), book);
        return book;
    }

    public Dvd addDvd(String title, int runtimeMinutes) {
        Dvd dvd = LibraryItemFactory.dvd(nextId("D"), title, runtimeMinutes);
        items.put(dvd.getId(), dvd);
        return dvd;
    }

    public Member registerMember(String name) {
        Member member = new Member(nextId("M"), name);
        members.put(member.getId(), member);
        return member;
    }

    public Loan borrow(String itemId, String memberId) throws ItemNotAvailableException, MemberLimitExceededException {
        return borrowOn(itemId, memberId, LocalDate.now());
    }

    public Loan borrowOn(String itemId, String memberId, LocalDate borrowedOn)
            throws ItemNotAvailableException, MemberLimitExceededException {
        if (borrowedOn == null) {
            throw new IllegalArgumentException("Borrow date is required.");
        }
        LibraryItem item = requireItem(itemId);
        Member member = requireMember(memberId);
        int period = item.loanPeriodDays();
        if (period <= 0) {
            throw new IllegalStateException(item.getId() + " has an invalid loan period.");
        }
        item.borrow(member);
        Loan loan = new Loan(nextId("L"), item.getId(), member.getId(), borrowedOn,
                borrowedOn.plusDays(period), null);
        loans.put(loan.getId(), loan);
        for (LibraryObserver observer : observers) {
            observer.onBorrowed(loan, item, member);
        }
        return loan;
    }

    public Loan giveBack(String itemId) throws ItemNotAvailableException {
        Loan loan = openLoanForItem(itemId).orElseThrow(() ->
                new ItemNotAvailableException("Item " + itemId + " is not out on loan."));
        LibraryItem item = requireItem(loan.getItemId());
        Member member = requireMember(loan.getMemberId());
        item.giveBack();
        member.releaseLoanSlot();
        loan.markReturned(LocalDate.now());
        for (LibraryObserver observer : observers) {
            observer.onReturned(loan, item, member);
        }
        return loan;
    }

    public List<Loan> overdueLoans(LocalDate today) {
        return loans.values().stream()
                .filter(loan -> loan.isOverdue(today))
                .toList();
    }

    public List<LibraryItem> searchByTitle(String query) {
        String needle = Checks.required(query, "Search text").toLowerCase();
        return items.values().stream()
                .filter(item -> item.getTitle().toLowerCase().contains(needle))
                .toList();
    }

    public double fineFor(Loan loan, LocalDate today) {
        return fineStrategy.calculateFine(loan, today);
    }

    public Collection<LibraryItem> getItems() {
        return Collections.unmodifiableCollection(items.values());
    }

    public Collection<Member> getMembers() {
        return Collections.unmodifiableCollection(members.values());
    }

    public Collection<Loan> getLoans() {
        return Collections.unmodifiableCollection(loans.values());
    }

    public Optional<LibraryItem> findItem(String id) {
        return Optional.ofNullable(items.get(id));
    }

    public Optional<Member> findMember(String id) {
        return Optional.ofNullable(members.get(id));
    }

    // First-run sample. Dates are relative to today, so one loan stays overdue
    // whenever the program is started.

    public void seedDemoData() throws ItemNotAvailableException, MemberLimitExceededException {
        Book cleanCode = addBook("Clean Code", "Robert C. Martin", "9780132350884");
        addBook("Effective Java", "Joshua Bloch", "9780134685991");
        addBook("The Pragmatic Programmer", "David Thomas", "9780135957059");
        addBook("Design Patterns", "Erich Gamma", "9780201633610");
        Dvd inception = addDvd("Inception", 148);

        Member amina = registerMember("Amina Yusuf");
        Member brian = registerMember("Brian Otieno");
        registerMember("Chipo Mwale");

        borrowOn(cleanCode.getId(), amina.getId(), LocalDate.now().minusDays(40));
        borrowOn(inception.getId(), brian.getId(), LocalDate.now().minusDays(1));
    }

    void clear() {
        items.clear();
        members.clear();
        loans.clear();
        nextNumbers.clear();
    }

    void loadItem(LibraryItem item) {
        putNew(items, item.getId(), item, "item");
    }

    void loadMember(Member member) {
        putNew(members, member.getId(), member, "member");
    }

    void loadLoan(Loan loan) {
        putNew(loans, loan.getId(), loan, "loan");
    }

    // Saved loans are history. Open ones are applied to items and members
    // with the same rules as a normal borrow, without creating a second loan.

    void restoreOpenLoans() throws ItemNotAvailableException, MemberLimitExceededException {
        for (Loan loan : loans.values()) {
            if (!loan.isOpen()) {
                continue;
            }
            requireItem(loan.getItemId()).borrow(requireMember(loan.getMemberId()));
        }
    }

    private <T> void putNew(Map<String, T> map, String id, T value, String label) {
        if (map.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate " + label + " id " + id + ".");
        }
        map.put(id, value);
        observeId(id);
    }

    private LibraryItem requireItem(String id) {
        LibraryItem item = items.get(Checks.required(id, "Item id"));
        if (item == null) {
            throw new IllegalArgumentException("No item with id " + id + ".");
        }
        return item;
    }

    private Member requireMember(String id) {
        Member member = members.get(Checks.required(id, "Member id"));
        if (member == null) {
            throw new IllegalArgumentException("No member with id " + id + ".");
        }
        return member;
    }

    private Optional<Loan> openLoanForItem(String itemId) {
        return loans.values().stream()
                .filter(loan -> loan.isOpen() && loan.getItemId().equals(itemId))
                .findFirst();
    }

    private String nextId(String prefix) {
        int number = nextNumbers.getOrDefault(prefix, 1);
        nextNumbers.put(prefix, number + 1);
        return prefix + String.format("%03d", number);
    }

    private void observeId(String id) {
        int index = 0;
        while (index < id.length() && Character.isLetter(id.charAt(index))) {
            index++;
        }
        if (index == 0 || index == id.length()) {
            return;
        }
        try {
            String prefix = id.substring(0, index);
            int number = Integer.parseInt(id.substring(index));
            nextNumbers.put(prefix, Math.max(nextNumbers.getOrDefault(prefix, 1), number + 1));
        } catch (NumberFormatException ex) {
            // An id that is not letters-plus-digits does not move the counter.
        }
    }
}
