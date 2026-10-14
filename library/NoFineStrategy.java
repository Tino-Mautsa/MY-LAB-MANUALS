package library;

import java.time.LocalDate;

// An alternate rule. Construct the Library with this instead of DailyFineStrategy. */
public class NoFineStrategy implements FineStrategy {
    @Override
    public double calculateFine(Loan loan, LocalDate today) {
        return 0;
    }

    @Override
    public String name() {
        return "no fines";
    }
}
