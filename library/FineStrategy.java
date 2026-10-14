package library;

import java.time.LocalDate;

// The fine rule is separate from borrowing. Swap the strategy without editing Library.

public interface FineStrategy {
    double calculateFine(Loan loan, LocalDate today);

    String name();
}
