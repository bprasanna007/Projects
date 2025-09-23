import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class TicketBookingPage extends JFrame {
    private JTextField customerIdField, flightIdField, seatNumberField;
    private JButton bookButton;

    public TicketBookingPage() {
        setTitle("Ticket Booking");
        setSize(400, 250);
        setLayout(new GridLayout(4, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new JLabel("Customer ID:"));
        customerIdField = new JTextField();
        add(customerIdField);

        add(new JLabel("Flight ID:"));
        flightIdField = new JTextField();
        add(flightIdField);

        add(new JLabel("Seat Number:"));
        seatNumberField = new JTextField();
        add(seatNumberField);

        bookButton = new JButton("Book Ticket");
        add(bookButton);

        bookButton.addActionListener(e -> bookTicket());

        setVisible(true);
    }

    private void bookTicket() {
        int customerId = Integer.parseInt(customerIdField.getText());
        int flightId = Integer.parseInt(flightIdField.getText());
        String seatNumber = seatNumberField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO bookings (customer_id, flight_id, seat_number) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, customerId);
            stmt.setInt(2, flightId);
            stmt.setString(3, seatNumber);

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int bookingId = generatedKeys.getInt(1);
                JOptionPane.showMessageDialog(this, "Ticket Booked! Booking ID: " + bookingId);
                dispose();
                new PaymentPage(bookingId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
