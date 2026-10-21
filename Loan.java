package library;

import java.time.LocalDate;

/** Connects one member to one borrowed library item. */
public class Loan {
    private final LibraryMember member;
    private final LibraryItem item;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(LibraryMember member, LibraryItem item) {
        if (member == null || item == null) {
            throw new IllegalArgumentException("Member and item are required.");
        }
        this.member = member;
        this.item = item;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(14);
    }

    public LibraryMember getMember() {
        return member;
    }

    public LibraryItem getItem() {
        return item;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isActive() {
        return returnDate == null;
    }

    public void markReturned() {
        if (!isActive()) {
            throw new IllegalStateException("This loan has already been returned.");
        }
        returnDate = LocalDate.now();
    }
}
