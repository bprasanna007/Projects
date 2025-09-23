import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PaymentPage extends JFrame {
    private JTextField amountField, paymentMethodField;
    private JButton payButton;
    private int bookingId;

    public PaymentPage(int bookingId) {
        this.bookingId = bookingId;

        setTitle("Payment");
        setSize(400, 200);
        setLayout(new GridLayout(3, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("Amount:"));
        amountField = new JTextField();
        add(amountField);

        add(new JLabel("Payment Method:"));
        paymentMethodField = new JTextField();
        add(paymentMethodField);

        payButton = new JButton("Pay");
        add(payButton);

        payButton.addActionListener(e -> makePayment());

        setVisible(true);
    }

    private void makePayment() {
        double amount = Double.parseDouble(amountField.getText());
        String method = paymentMethodField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO payments (booking_id, amount, payment_method) VALUES (?, ?, ?)")) {

            stmt.setInt(1, bookingId);
            stmt.setDouble(2, amount);
            stmt.setString(3, method);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Payment Successful!");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
