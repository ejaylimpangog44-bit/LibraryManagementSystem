package library;

/** Example of a second item type, used to demonstrate polymorphism. */
public class Magazine extends LibraryItem {
    private String publisher;

    public Magazine(String id, String title, String publisher) {
        super(id, title);
        setCreator(publisher);
    }

    @Override
    public String getItemType() {
        return "Magazine";
    }

    @Override
    public String getCreatorLabel() {
        return "Publisher";
    }

    @Override
    public String getCreator() {
        return publisher;
    }

    @Override
    public void setCreator(String creator) {
        if (creator == null || creator.trim().isEmpty()) {
            throw new IllegalArgumentException("Publisher is required.");
        }
        publisher = creator.trim();
    }
}
