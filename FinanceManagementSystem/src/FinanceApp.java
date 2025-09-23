import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FinanceApp extends JFrame {
    Connection conn;
    JTextField nameField, emailField, loginEmailField, loanAmountField, interestRateField, termField;
    JPasswordField passwordField, loginPasswordField;
    JButton registerBtn, loginBtn, addUserBtn, calcInterestBtn, cibilBtn, logoutBtn;
    JLabel statusLabel;
    String currentUser = "", currentRole = "";
    int currentUserId = -1;

    public FinanceApp() {
        setTitle("Finance Management System");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initDB();
        showLoginScreen();
    }
    

    void initDB() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance_db", "root", "123prasanna");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Error: " + e.getMessage());
        }
    }
    
    

    void showLoginScreen() {
        JPanel panel = new JPanel(new GridLayout(6, 2));
        loginEmailField = new JTextField();        loginPasswordField = new JPasswordField();

        loginBtn = new JButton("Login");
        JButton switchToRegister = new JButton("Register");

        panel.add(new JLabel("Email:"));
        panel.add(loginEmailField);
        panel.add(new JLabel("Password:"));
        panel.add(loginPasswordField);
        panel.add(loginBtn);
        panel.add(switchToRegister);

        add(panel, BorderLayout.CENTER);
        loginBtn.addActionListener(e -> login());
        switchToRegister.addActionListener(e -> {
            getContentPane().removeAll();
            showRegisterScreen();
            revalidate();
            repaint();
        });
    }

    void showRegisterScreen() {
        JPanel panel = new JPanel(new GridLayout(7, 2));
        nameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        registerBtn = new JButton("Register");
        JButton backToLogin = new JButton("Back");

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(registerBtn);
        panel.add(backToLogin);

        add(panel, BorderLayout.CENTER);
        registerBtn.addActionListener(e -> register());
        backToLogin.addActionListener(e -> {
            getContentPane().removeAll();
            showLoginScreen();
            revalidate();
            repaint();
        });
    }

    void showAdminScreen() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        nameField = new JTextField();
        emailField = new JTextField();
        addUserBtn = new JButton("Add User");
        logoutBtn = new JButton("Logout");

        panel.add(new JLabel("User Name:"));
        panel.add(nameField);
        panel.add(new JLabel("User Email:"));
        panel.add(emailField);
        panel.add(addUserBtn);
        panel.add(logoutBtn);

        add(panel, BorderLayout.CENTER);
        addUserBtn.addActionListener(e -> addUser());
        logoutBtn.addActionListener(e -> logout());
    }

    void showUserScreen() {
        JPanel panel = new JPanel(new GridLayout(7, 2));
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser + " (" + currentRole + ")");
        panel.add(welcomeLabel);

        loanAmountField = new JTextField();
        interestRateField = new JTextField();
        termField = new JTextField();
        calcInterestBtn = new JButton("Calculate Interest");
        cibilBtn = new JButton("Check CIBIL Score");
        logoutBtn = new JButton("Logout");
        statusLabel = new JLabel("Status:");

        panel.add(new JLabel("Loan Amount:"));
        panel.add(loanAmountField);
        panel.add(new JLabel("Interest Rate (%):"));
        panel.add(interestRateField);
        panel.add(new JLabel("Term (months):"));
        panel.add(termField);
        panel.add(calcInterestBtn);
        panel.add(cibilBtn);
        panel.add(logoutBtn);
        panel.add(statusLabel);

        add(panel, BorderLayout.CENTER);

        calcInterestBtn.addActionListener(e -> calculateInterest());
        cibilBtn.addActionListener(e -> showCIBIL());
        logoutBtn.addActionListener(e -> logout());
    }

    void login() {
        String email = loginEmailField.getText();
        String pass = String.valueOf(loginPasswordField.getPassword());
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE email=? AND password=?")) {
            ps.setString(1, email);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                currentUserId = rs.getInt("id");
                currentUser = rs.getString("name");
                currentRole = rs.getString("role");

                getContentPane().removeAll();
                if (currentRole.equals("admin")) {
                    showAdminScreen();
                } else {
                    showUserScreen();
                }
                revalidate();
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Login error: " + e.getMessage());
        }
    }

    void register() {
        String name = nameField.getText();
        String email = emailField.getText();
        String pass = String.valueOf(passwordField.getPassword());

        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, 'user')")) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, pass);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration successful");
            getContentPane().removeAll();
            showLoginScreen();
            revalidate();
            repaint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Register error: " + e.getMessage());
        }
    }

    void addUser() {
        String name = nameField.getText();
        String email = emailField.getText();

        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, 'user')")) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, "default123");
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "User added successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage());
        }
    }

    void calculateInterest() {
        try {
            double principal = Double.parseDouble(loanAmountField.getText());
            double rate = Double.parseDouble(interestRateField.getText());
            int term = Integer.parseInt(termField.getText());

            double interest = (principal * rate * term) / (100 * 12);
            double total = principal + interest;

            statusLabel.setText("Total Payable: ₹" + total);
        } catch (Exception e) {
            statusLabel.setText("Invalid input: " + e.getMessage());
        }
    }

    void showCIBIL() {
        JOptionPane.showMessageDialog(this, "Your CIBIL Score is: " + (600 + (int) (Math.random() * 300)));
    }

    void logout() {
        currentUser = "";
        currentUserId = -1;
        currentRole = "";
        getContentPane().removeAll();
        showLoginScreen();
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FinanceApp().setVisible(true));
    }
}