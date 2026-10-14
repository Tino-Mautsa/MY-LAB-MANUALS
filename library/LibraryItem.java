package library;

import java.util.List;

// Shared identity of every catalogue item. Book and Dvd fill in their own loan period.

public abstract class LibraryItem implements Borrowable {
    private final String id;
    private final String title;
    private boolean available;

    protected LibraryItem(String id, String title) {
        this.id = Checks.required(id, "Id");
        this.title = Checks.required(title, "Title");
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAvailable() {
        return available;
    }

    // How many days a member may keep this item. Each subclass decides
    public abstract int loanPeriodDays();

    public abstract String typeCode();

    // Extra CSV columns after type, id and title
    public abstract List<String> csvExtras();

    @Override
    public void borrow(Member member) throws ItemNotAvailableException, MemberLimitExceededException {
        if (member == null) {
            throw new IllegalArgumentException("Member is required.");
        }
        if (!available) {
            throw new ItemNotAvailableException("Item " + id + " (" + title + ") is already on loan.");
        }
        member.reserveLoanSlot();
        available = false;
    }

    @Override
    public void giveBack() {
        if (available) {
            throw new IllegalStateException("Item " + id + " is not on loan.");
        }
        available = true;
    }

    @Override
    public String toString() {
        return id + " | " + title + " | " + (available ? "available" : "on loan");
    }
}
