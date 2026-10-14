package library;

import java.time.LocalDate;

public class Loan {
    private final String id;
    private final String itemId;
    private final String memberId;
    private final LocalDate borrowedOn;
    private final LocalDate dueOn;
    private LocalDate returnedOn;

    public Loan(String id, String itemId, String memberId, LocalDate borrowedOn, LocalDate dueOn, LocalDate returnedOn) {
        this.id = Checks.required(id, "Loan id");
        this.itemId = Checks.required(itemId, "Item id");
        this.memberId = Checks.required(memberId, "Member id");
        if (borrowedOn == null || dueOn == null) {
            throw new IllegalArgumentException("Borrow date and due date are required.");
        }
        if (dueOn.isBefore(borrowedOn)) {
            throw new IllegalArgumentException("Due date cannot be before the borrow date.");
        }
        this.borrowedOn = borrowedOn;
        this.dueOn = dueOn;
        this.returnedOn = returnedOn;
    }

    public String getId() {
        return id;
    }

    public String getItemId() {
        return itemId;
    }

    public String getMemberId() {
        return memberId;
    }

    public LocalDate getBorrowedOn() {
        return borrowedOn;
    }

    public LocalDate getDueOn() {
        return dueOn;
    }

    public LocalDate getReturnedOn() {
        return returnedOn;
    }

    public boolean isOpen() {
        return returnedOn == null;
    }

    // Overdue the day after the due date. The due date itself is still on time. */
    public boolean isOverdue(LocalDate today) {
        return isOpen() && dueOn.isBefore(today);
    }

    public void markReturned(LocalDate date) {
        if (!isOpen()) {
            throw new IllegalStateException("Loan " + id + " was already returned.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Return date is required.");
        }
        this.returnedOn = date;
    }

    @Override
    public String toString() {
        String state = isOpen() ? "open" : "returned " + returnedOn;
        return id + " | item " + itemId + " | member " + memberId
                + " | borrowed " + borrowedOn + " | due " + dueOn + " | " + state;
    }
}
