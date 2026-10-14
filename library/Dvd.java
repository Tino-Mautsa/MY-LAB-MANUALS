package library;

import java.util.List;

public class Dvd extends LibraryItem {
    private final int runtimeMinutes;

    public Dvd(String id, String title, int runtimeMinutes) {
        super(id, title);
        if (runtimeMinutes <= 0) {
            throw new IllegalArgumentException("Runtime must be a positive number of minutes.");
        }
        this.runtimeMinutes = runtimeMinutes;
    }

    public int getRuntimeMinutes() {
        return runtimeMinutes;
    }

    @Override
    public int loanPeriodDays() {
        return 7;
    }

    @Override
    public String typeCode() {
        return "DVD";
    }

    @Override
    public List<String> csvExtras() {
        return List.of(Integer.toString(runtimeMinutes), "");
    }

    @Override
    public String toString() {
        return getId() + " | DVD | " + getTitle() + " | " + runtimeMinutes + " min"
                + " | " + (isAvailable() ? "available" : "on loan")
                + " | " + loanPeriodDays() + " days";
    }
}
