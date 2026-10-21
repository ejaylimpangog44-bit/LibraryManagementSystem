package library;

/** Abstract parent class for items that can be managed by the library. */
public abstract class LibraryItem {
    private final String id;
    private String title;
    private boolean available = true;

    protected LibraryItem(String id, String title) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID is required.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }
        this.id = id.trim();
        this.title = title.trim();
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

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required.");
        }
        this.title = title.trim();
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract String getItemType();

    public abstract String getCreatorLabel();

    public abstract String getCreator();

    public abstract void setCreator(String creator);
}
