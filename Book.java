package library;

/** A book is a specific kind of library item. */
public class Book extends LibraryItem {
    private String author;

    public Book(String id, String title, String author) {
        super(id, title);
        setCreator(author);
    }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { setCreator(author); }

    @Override
    public String getItemType() { return "Book"; }

    @Override
    public String getCreatorLabel() { return "Author"; }

    @Override
    public String getCreator() { return author; }

    @Override
    public void setCreator(String creator) {
        if (creator == null || creator.trim().isEmpty()) {
            throw new IllegalArgumentException("Author is required.");
        }
        author = creator.trim();
    }
}
