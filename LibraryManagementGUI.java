package library;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Swing GUI. Buttons call the service layer and refresh the tables. */
public class LibraryManagementGUI extends JFrame {
    private final LibraryService service;
    private final DefaultTableModel itemModel = model("ID", "Title", "Type", "Author / Publisher", "Status");
    private final DefaultTableModel memberModel = model("Member ID", "Name", "Email");
    private final DefaultTableModel loanModel = model("Item ID", "Title", "Member ID", "Member Name", "Borrowed", "Due Date", "Status");

    private final JTable itemTable = new JTable(itemModel);
    private final JTable memberTable = new JTable(memberModel);
    private final JTable loanTable = new JTable(loanModel);
    private final JTextField searchField = new JTextField(20);
    private final JLabel summaryLabel = new JLabel();

    public LibraryManagementGUI(LibraryService service) {
        super("Library Management System");
        this.service = service;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel heading = new JLabel("LIBRARY MANAGEMENT SYSTEM", SwingConstants.CENTER);
        heading.setFont(heading.getFont().deriveFont(22f));
        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.add(heading, BorderLayout.NORTH);
        top.add(summaryLabel, BorderLayout.CENTER);
        top.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        add(top, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Books & Items", buildItemsPanel());
        tabs.addTab("Members", buildMembersPanel());
        tabs.addTab("Loans", buildLoansPanel());
        add(tabs, BorderLayout.CENTER);

        refreshAll();
    }

    private JPanel buildItemsPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.add(new JLabel("Search:"));
        search.add(searchField);
        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");
        search.add(searchButton);
        search.add(showAllButton);
        searchButton.addActionListener(e -> refreshItems(service.searchItems(searchField.getText())));
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            refreshItems(service.getItems());
        });

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBook = new JButton("Add Book");
        JButton addMagazine = new JButton("Add Magazine");
        JButton edit = new JButton("Edit Selected");
        JButton delete = new JButton("Delete Selected");
        JButton borrow = new JButton("Borrow Selected");
        actions.add(addBook);
        actions.add(addMagazine);
        actions.add(edit);
        actions.add(delete);
        actions.add(borrow);

        addBook.addActionListener(e -> addItem(false));
        addMagazine.addActionListener(e -> addItem(true));
        edit.addActionListener(e -> editSelectedItem());
        delete.addActionListener(e -> deleteSelectedItem());
        borrow.addActionListener(e -> borrowSelectedItem());

        JPanel north = new JPanel(new BorderLayout());
        north.add(search, BorderLayout.NORTH);
        north.add(actions, BorderLayout.SOUTH);
        panel.add(north, BorderLayout.NORTH);
        panel.add(new JScrollPane(itemTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Register Member");
        JButton edit = new JButton("Edit Selected");
        JButton delete = new JButton("Delete Selected");
        actions.add(add);
        actions.add(edit);
        actions.add(delete);
        add.addActionListener(e -> addMember());
        edit.addActionListener(e -> editSelectedMember());
        delete.addActionListener(e -> deleteSelectedMember());
        panel.add(actions, BorderLayout.NORTH);
        panel.add(new JScrollPane(memberTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildLoansPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton borrow = new JButton("Borrow Item");
        JButton returnItem = new JButton("Return by Item ID");
        actions.add(borrow);
        actions.add(returnItem);
        borrow.addActionListener(e -> borrowDialog());
        returnItem.addActionListener(e -> returnDialog());
        panel.add(actions, BorderLayout.NORTH);
        panel.add(new JScrollPane(loanTable), BorderLayout.CENTER);
        return panel;
    }

    private void addItem(boolean magazine) {
        JTextField id = new JTextField();
        JTextField title = new JTextField();
        JTextField creator = new JTextField();
        String creatorLabel = magazine ? "Publisher:" : "Author:";
        JPanel form = formPanel(new String[]{"ID:", "Title:", creatorLabel},
                new JTextField[]{id, title, creator});
        int result = JOptionPane.showConfirmDialog(this, form,
                magazine ? "Add Magazine" : "Add Book", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;
        try {
            LibraryItem item = magazine
                    ? new Magazine(id.getText(), title.getText(), creator.getText())
                    : new Book(id.getText(), title.getText(), creator.getText());
            service.addItem(item);
            refreshAll();
            message("Item added successfully.");
        } catch (RuntimeException ex) {
            error(ex.getMessage());
        }
    }

    private void editSelectedItem() {
        int row = itemTable.getSelectedRow();
        if (row < 0) { error("Select an item first."); return; }
        String id = itemModel.getValueAt(row, 0).toString();
        LibraryItem item = service.findItem(id);
        JTextField title = new JTextField(item.getTitle());
        JTextField creator = new JTextField(item.getCreator());
        JPanel form = formPanel(new String[]{"Title:", item.getCreatorLabel() + ":"},
                new JTextField[]{title, creator});
        if (JOptionPane.showConfirmDialog(this, form, "Edit Item",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            service.updateItem(id, title.getText(), creator.getText());
            refreshAll();
            message("Item updated successfully.");
        } catch (RuntimeException ex) { error(ex.getMessage()); }
    }

    private void deleteSelectedItem() {
        int row = itemTable.getSelectedRow();
        if (row < 0) { error("Select an item first."); return; }
        String id = itemModel.getValueAt(row, 0).toString();
        if (confirm("Delete item " + id + "?")) {
            try { service.deleteItem(id); refreshAll(); }
            catch (RuntimeException ex) { error(ex.getMessage()); }
        }
    }

    private void addMember() {
        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JPanel form = formPanel(new String[]{"Member ID:", "Full name:", "Email:"},
                new JTextField[]{id, name, email});
        if (JOptionPane.showConfirmDialog(this, form, "Register Member",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            service.registerMember(new LibraryMember(id.getText(), name.getText(), email.getText()));
            refreshAll();
            message("Member registered successfully.");
        } catch (RuntimeException ex) { error(ex.getMessage()); }
    }

    private void editSelectedMember() {
        int row = memberTable.getSelectedRow();
        if (row < 0) { error("Select a member first."); return; }
        String id = memberModel.getValueAt(row, 0).toString();
        LibraryMember member = service.findMember(id);
        JTextField name = new JTextField(member.getName());
        JTextField email = new JTextField(member.getEmail());
        JPanel form = formPanel(new String[]{"Full name:", "Email:"},
                new JTextField[]{name, email});
        if (JOptionPane.showConfirmDialog(this, form, "Edit Member",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try { service.updateMember(id, name.getText(), email.getText()); refreshAll(); }
        catch (RuntimeException ex) { error(ex.getMessage()); }
    }

    private void deleteSelectedMember() {
        int row = memberTable.getSelectedRow();
        if (row < 0) { error("Select a member first."); return; }
        String id = memberModel.getValueAt(row, 0).toString();
        if (confirm("Delete member " + id + "?")) {
            try { service.deleteMember(id); refreshAll(); }
            catch (RuntimeException ex) { error(ex.getMessage()); }
        }
    }

    private void borrowSelectedItem() {
        int row = itemTable.getSelectedRow();
        if (row < 0) { error("Select an item first."); return; }
        String itemId = itemModel.getValueAt(row, 0).toString();
        borrowDialog(itemId);
    }

    private void borrowDialog() { borrowDialog(""); }

    private void borrowDialog(String presetItemId) {
        JTextField memberId = new JTextField();
        JTextField itemId = new JTextField(presetItemId);
        JPanel form = formPanel(new String[]{"Member ID:", "Item ID:"},
                new JTextField[]{memberId, itemId});
        if (JOptionPane.showConfirmDialog(this, form, "Borrow Item",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            service.borrowItem(memberId.getText(), itemId.getText());
            refreshAll();
            message("Item borrowed successfully. Standard loan period: 14 days.");
        } catch (RuntimeException ex) { error(ex.getMessage()); }
    }

    private void returnDialog() {
        JTextField itemId = new JTextField();
        JPanel form = formPanel(new String[]{"Item ID:"}, new JTextField[]{itemId});
        if (JOptionPane.showConfirmDialog(this, form, "Return Item",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try { service.returnItem(itemId.getText()); refreshAll(); message("Item returned successfully."); }
        catch (RuntimeException ex) { error(ex.getMessage()); }
    }

    private void refreshAll() {
        refreshItems(service.getItems());
        memberModel.setRowCount(0);
        for (LibraryMember member : service.getMembers()) {
            memberModel.addRow(new Object[]{member.getMemberId(), member.getName(), member.getEmail()});
        }
        loanModel.setRowCount(0);
        for (Loan loan : service.getLoans()) {
            loanModel.addRow(new Object[]{
                loan.getItem().getId(), loan.getItem().getTitle(),
                loan.getMember().getMemberId(), loan.getMember().getName(),
                loan.getBorrowDate(), loan.getDueDate(),
                loan.isActive() ? "Borrowed" : "Returned (" + loan.getReturnDate() + ")"
            });
        }
        summaryLabel.setText("Items: " + service.getItems().size()
                + "   |   Available: " + service.countAvailableItems()
                + "   |   Members: " + service.getMembers().size()
                + "   |   Active loans: " + service.countActiveLoans());
    }

    private void refreshItems(List<LibraryItem> items) {
        itemModel.setRowCount(0);
        for (LibraryItem item : items) {
            itemModel.addRow(new Object[]{
                item.getId(), item.getTitle(), item.getItemType(),
                item.getCreator(), item.isAvailable() ? "Available" : "Borrowed"
            });
        }
    }

    private static DefaultTableModel model(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private static JPanel formPanel(String[] labels, JTextField[] fields) {
        JPanel panel = new JPanel(new GridLayout(labels.length, 2, 8, 8));
        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]));
            panel.add(fields[i]);
        }
        return panel;
    }

    private boolean confirm(String text) {
        return JOptionPane.showConfirmDialog(this, text, "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private void message(String text) {
        JOptionPane.showMessageDialog(this, text, "Library System", JOptionPane.INFORMATION_MESSAGE);
    }

    private void error(String text) {
        JOptionPane.showMessageDialog(this, text, "Input / Operation Error", JOptionPane.ERROR_MESSAGE);
    }
}
