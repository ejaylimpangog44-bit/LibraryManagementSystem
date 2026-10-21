package library;

/** Represents a registered library member. */
public class LibraryMember {
    private final String memberId;
    private String name;
    private String email;

    public LibraryMember(String memberId, String name, String email) {
        if (memberId == null || memberId.trim().isEmpty()) {
            throw new IllegalArgumentException("Member ID is required.");
        }
        this.memberId = memberId.trim();
        setName(name);
        setEmail(email);
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Member name is required.");
        }
        this.name = name.trim();
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("Enter a valid email address.");
        }
        this.email = email.trim();
    }
}
