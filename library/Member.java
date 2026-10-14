package library;

public class Member {
    public static final int MAX_LOANS = 3;

    private final String id;
    private final String name;
    private int activeLoans;

    public Member(String id, String name) {
        this.id = Checks.required(id, "Member id");
        this.name = Checks.required(name, "Member name");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getActiveLoans() {
        return activeLoans;
    }

    public void reserveLoanSlot() throws MemberLimitExceededException {
        if (activeLoans >= MAX_LOANS) {
            throw new MemberLimitExceededException(
                    "Member " + id + " (" + name + ") already has " + MAX_LOANS + " active loans.");
        }
        activeLoans++;
    }

    public void releaseLoanSlot() {
        if (activeLoans == 0) {
            throw new IllegalStateException("Member " + id + " has no active loan to release.");
        }
        activeLoans--;
    }

    @Override
    public String toString() {
        return id + " | " + name + " | loans " + activeLoans + "/" + MAX_LOANS;
    }
}
