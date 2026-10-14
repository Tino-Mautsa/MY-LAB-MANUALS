package library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DailyFineStrategy implements FineStrategy {
    private final double amountPerDay;

    public DailyFineStrategy(double amountPerDay) {
        if (amountPerDay < 0) {
            throw new IllegalArgumentException("Fine per day cannot be negative.");
        }
        this.amountPerDay = amountPerDay;
    }

    @Override
    public double calculateFine(Loan loan, LocalDate today) {
        if (loan == null || today == null || !loan.isOverdue(today)) {
            return 0;
        }
        long daysLate = ChronoUnit.DAYS.between(loan.getDueOn(), today);
        return daysLate * amountPerDay;
    }

    @Override
    public String name() {
        return amountPerDay + " per overdue day";
    }
}
