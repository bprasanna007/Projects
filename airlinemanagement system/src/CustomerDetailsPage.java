import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerDetailsPage extends JFrame {
    private JTextField nameField, addressField, phoneField, emailField;
    private JButton saveButton;

    public CustomerDetailsPage() {
        setTitle("Customer Details");
        setSize(400, 300);
        setLayout(new GridLayout(5, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Address:"));
        addressField = new JTextField();
        add(addressField);

        add(new JLabel("Phone:"));
        phoneField = new JTextField();
        add(phoneField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        saveButton = new JButton("Save Customer");
        add(saveButton);

        saveButton.addActionListener(e -> saveCustomer());

        setVisible(true);
    }

    private void saveCustomer() {
        String name = nameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO customers (name, address, phone, email) VALUES (?, ?, ?, ?)")) {

            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, phone);
            stmt.setString(4, email);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer Added Successfully!");
            dispose();
            new TicketBookingPage();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
