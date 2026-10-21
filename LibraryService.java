package library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Application logic; the GUI calls this class instead of managing data itself. */
public class LibraryService {
    private final List<LibraryItem> items = new ArrayList<LibraryItem>();
    private final List<LibraryMember> members = new ArrayList<LibraryMember>();
    private final List<Loan> loans = new ArrayList<Loan>();

    public List<LibraryItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<LibraryMember> getMembers() {
        return Collections.unmodifiableList(members);
    }

    public List<Loan> getLoans() {
        return Collections.unmodifiableList(loans);
    }

    public void addItem(LibraryItem item) {
        if (item == null) throw new IllegalArgumentException("Item is required.");
        if (findItem(item.getId()) != null) {
            throw new IllegalArgumentException("An item with that ID already exists.");
        }
        items.add(item);
    }

    public void updateItem(String id, String title, String creator) {
        LibraryItem item = findItem(id);
        if (item == null) throw new IllegalArgumentException("Item not found.");
        if (!item.isAvailable()) {
            throw new IllegalStateException("Return this item before editing it.");
        }
        item.setTitle(title);
        item.setCreator(creator);
    }

    public void deleteItem(String id) {
        LibraryItem item = findItem(id);
        if (item == null) throw new IllegalArgumentException("Item not found.");
        for (Loan loan : loans) {
            if (loan.getItem() == item) {
                throw new IllegalStateException("Cannot delete an item with loan history.");
            }
        }
        items.remove(item);
    }

    public void registerMember(LibraryMember member) {
        if (member == null) throw new IllegalArgumentException("Member is required.");
        if (findMember(member.getMemberId()) != null) {
            throw new IllegalArgumentException("A member with that ID already exists.");
        }
        members.add(member);
    }

    public void updateMember(String id, String name, String email) {
        LibraryMember member = findMember(id);
        if (member == null) throw new IllegalArgumentException("Member not found.");
        member.setName(name);
        member.setEmail(email);
    }

    public void deleteMember(String id) {
        LibraryMember member = findMember(id);
        if (member == null) throw new IllegalArgumentException("Member not found.");
        for (Loan loan : loans) {
            if (loan.getMember() == member && loan.isActive()) {
                throw new IllegalStateException("Return all borrowed items before deleting this member.");
            }
        }
        members.remove(member);
    }

    public LibraryItem findItem(String id) {
        if (id == null) return null;
        for (LibraryItem item : items) {
            if (item.getId().equalsIgnoreCase(id.trim())) return item;
        }
        return null;
    }

    public LibraryMember findMember(String id) {
        if (id == null) return null;
        for (LibraryMember member : members) {
            if (member.getMemberId().equalsIgnoreCase(id.trim())) return member;
        }
        return null;
    }

    public List<LibraryItem> searchItems(String query) {
        List<LibraryItem> results = new ArrayList<LibraryItem>();
        String q = query == null ? "" : query.trim().toLowerCase();
        for (LibraryItem item : items) {
            if (item.getId().toLowerCase().contains(q)
                    || item.getTitle().toLowerCase().contains(q)
                    || item.getCreator().toLowerCase().contains(q)
                    || item.getItemType().toLowerCase().contains(q)) {
                results.add(item);
            }
        }
        return results;
    }

    public Loan borrowItem(String memberId, String itemId) {
        LibraryMember member = findMember(memberId);
        LibraryItem item = findItem(itemId);
        if (member == null) throw new IllegalArgumentException("Member ID was not found.");
        if (item == null) throw new IllegalArgumentException("Item ID was not found.");
        if (!item.isAvailable()) throw new IllegalStateException("This item is already borrowed.");

        item.setAvailable(false);
        Loan loan = new Loan(member, item);
        loans.add(loan);
        return loan;
    }

    public void returnItem(String itemId) {
        for (int i = loans.size() - 1; i >= 0; i--) {
            Loan loan = loans.get(i);
            if (loan.isActive() && loan.getItem().getId().equalsIgnoreCase(itemId.trim())) {
                loan.markReturned();
                loan.getItem().setAvailable(true);
                return;
            }
        }
        throw new IllegalArgumentException("No active loan found for that item ID.");
    }

    public int countAvailableItems() {
        int count = 0;
        for (LibraryItem item : items) if (item.isAvailable()) count++;
        return count;
    }

    public int countActiveLoans() {
        int count = 0;
        for (Loan loan : loans) if (loan.isActive()) count++;
        return count;
    }
}
