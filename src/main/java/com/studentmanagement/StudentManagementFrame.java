package com.studentmanagement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StudentManagementFrame extends JFrame {

    private final StudentDAO studentDAO = new StudentDAO();

    private final JTextField nameField = new JTextField();
    private final JComboBox<String> departmentBox = new JComboBox<>(
            new String[]{"CSE", "EEE", "BBA", "English", "Economics"}
    );
    private final JTextField emailField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField searchField = new JTextField();

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Department", "Email", "Phone"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable studentTable = new JTable(tableModel);
    private int selectedStudentId = -1;

    public StudentManagementFrame() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        setContentPane(createMainPanel());
        loadStudents();
    }

    private JPanel createMainPanel() {
        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBackground(new Color(245, 247, 250));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createFormPanel(), BorderLayout.WEST);
        root.add(createTablePanel(), BorderLayout.CENTER);

        return root;
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 64, 175));
        panel.setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel title = new JLabel("STUDENT MANAGEMENT SYSTEM");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 25));

        JLabel subtitle = new JLabel("Java Swing • MySQL • JDBC");
        subtitle.setForeground(new Color(219, 234, 254));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(subtitle);

        panel.add(textPanel, BorderLayout.WEST);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(315, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 232)),
                new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel heading = new JLabel("Student Information");
        heading.setFont(new Font("SansSerif", Font.BOLD, 19));
        heading.setForeground(new Color(30, 41, 59));

        gbc.gridy = 0;
        panel.add(heading, gbc);

        addField(panel, gbc, 1, "Full Name", nameField);
        addField(panel, gbc, 3, "Department", departmentBox);
        addField(panel, gbc, 5, "Email", emailField);
        addField(panel, gbc, 7, "Phone", phoneField);

        JPanel firstRow = new JPanel(new GridLayout(1, 2, 8, 0));
        firstRow.setOpaque(false);
        JButton addButton = createPrimaryButton("Add");
        JButton updateButton = createSecondaryButton("Update");
        firstRow.add(addButton);
        firstRow.add(updateButton);

        gbc.gridy = 9;
        gbc.insets = new Insets(18, 0, 5, 0);
        panel.add(firstRow, gbc);

        JPanel secondRow = new JPanel(new GridLayout(1, 2, 8, 0));
        secondRow.setOpaque(false);
        JButton deleteButton = createDangerButton("Delete");
        JButton clearButton = createSecondaryButton("Clear");
        secondRow.add(deleteButton);
        secondRow.add(clearButton);

        gbc.gridy = 10;
        gbc.insets = new Insets(5, 0, 5, 0);
        panel.add(secondRow, gbc);

        gbc.gridy = 11;
        gbc.weighty = 1;
        panel.add(Box.createVerticalGlue(), gbc);

        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearForm());

        return panel;
    }

    private void addField(
            JPanel panel,
            GridBagConstraints gbc,
            int labelRow,
            String labelText,
            JComponent component
    ) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(new Color(51, 65, 85));

        gbc.gridy = labelRow;
        gbc.insets = new Insets(10, 0, 3, 0);
        panel.add(label, gbc);

        component.setFont(new Font("SansSerif", Font.PLAIN, 14));
        component.setPreferredSize(new Dimension(0, 34));

        gbc.gridy = labelRow + 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(component, gbc);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        JButton searchButton = createPrimaryButton("Search");
        JButton refreshButton = createSecondaryButton("Show All");

        JPanel searchButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchButtons.setOpaque(false);
        searchButtons.add(searchButton);
        searchButtons.add(refreshButton);

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButtons, BorderLayout.EAST);

        studentTable.setRowHeight(30);
        studentTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        studentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225)));

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> searchStudents());
        refreshButton.addActionListener(e -> {
            searchField.setText("");
            loadStudents();
        });
        searchField.addActionListener(e -> searchStudents());

        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });

        return panel;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(37, 99, 235));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(30, 64, 175));
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        return button;
    }

    private JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(220, 38, 38));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        return button;
    }

    private void addStudent() {
        if (!isFormValid()) {
            return;
        }

        Student student = new Student(
                nameField.getText().trim(),
                departmentBox.getSelectedItem().toString(),
                emailField.getText().trim(),
                phoneField.getText().trim()
        );

        try {
            studentDAO.addStudent(student);
            showMessage("Student added successfully.");
            clearForm();
            loadStudents();
        } catch (SQLException exception) {
            showDatabaseError(exception);
        }
    }

    private void updateStudent() {
        if (selectedStudentId == -1) {
            showWarning("Select a student from the table first.");
            return;
        }

        if (!isFormValid()) {
            return;
        }

        Student student = new Student(
                selectedStudentId,
                nameField.getText().trim(),
                departmentBox.getSelectedItem().toString(),
                emailField.getText().trim(),
                phoneField.getText().trim()
        );

        try {
            studentDAO.updateStudent(student);
            showMessage("Student updated successfully.");
            clearForm();
            loadStudents();
        } catch (SQLException exception) {
            showDatabaseError(exception);
        }
    }

    private void deleteStudent() {
        if (selectedStudentId == -1) {
            showWarning("Select a student from the table first.");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this student?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            studentDAO.deleteStudent(selectedStudentId);
            showMessage("Student deleted successfully.");
            clearForm();
            loadStudents();
        } catch (SQLException exception) {
            showDatabaseError(exception);
        }
    }

    private void loadStudents() {
        try {
            displayStudents(studentDAO.getAllStudents());
        } catch (SQLException exception) {
            showDatabaseError(exception);
        }
    }

    private void searchStudents() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            loadStudents();
            return;
        }

        try {
            displayStudents(studentDAO.searchStudents(keyword));
        } catch (SQLException exception) {
            showDatabaseError(exception);
        }
    }

    private void displayStudents(List<Student> students) {
        tableModel.setRowCount(0);

        for (Student student : students) {
            tableModel.addRow(new Object[]{
                    student.getId(),
                    student.getName(),
                    student.getDepartment(),
                    student.getEmail(),
                    student.getPhone()
            });
        }
    }

    private void fillFormFromSelectedRow() {
        int selectedRow = studentTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        selectedStudentId = Integer.parseInt(
                tableModel.getValueAt(selectedRow, 0).toString()
        );

        nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        departmentBox.setSelectedItem(
                tableModel.getValueAt(selectedRow, 2).toString()
        );
        emailField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        phoneField.setText(tableModel.getValueAt(selectedRow, 4).toString());
    }

    private boolean isFormValid() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showWarning("Name, email, and phone cannot be empty.");
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showWarning("Enter a valid email address.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        selectedStudentId = -1;
        nameField.setText("");
        departmentBox.setSelectedIndex(0);
        emailField.setText("");
        phoneField.setText("");
        studentTable.clearSelection();
        nameField.requestFocus();
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Warning",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showDatabaseError(SQLException exception) {
        JOptionPane.showMessageDialog(
                this,
                "Database error:\n" + exception.getMessage()
                        + "\n\nCheck MySQL, the database name, and your password.",
                "Database Connection Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
