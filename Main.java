package library;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Application entry point. */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Use the default Swing look and feel if the system theme is unavailable.
            }

            LibraryService service = new LibraryService();
            addDemoData(service);
            new LibraryManagementGUI(service).setVisible(true);
        });
    }

    private static void addDemoData(LibraryService service) {
        service.addItem(new Book("B001", "Java Programming", "John Smith"));
        service.addItem(new Book("B002", "Object-Oriented Programming", "Jane Doe"));
        service.addItem(new Magazine("M001", "Science Monthly", "Science Press"));
        service.registerMember(new LibraryMember("MB001", "Juan Dela Cruz", "juan@example.com"));
        service.registerMember(new LibraryMember("MB002", "Maria Santos", "maria@example.com"));
    }
}
