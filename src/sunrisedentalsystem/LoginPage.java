/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sunrisedentalsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 *
 * @author Future_Mind
 */
public class LoginPage extends JFrame 
{

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginPage() 
    {
        setTitle("Sunrise Dental Clinic - User Login");
        setSize(420, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // 1. Header Title
        JLabel lblTitle = new JLabel("SUNRISE DENTAL", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(15, 23, 42));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 4, 0);
        mainPanel.add(lblTitle, gbc);

        // 2. Subtitle
        JLabel lblSub = new JLabel("Login to access clinic portal", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(new Color(100, 116, 139));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 25, 0);
        mainPanel.add(lblSub, gbc);

        // 3. Username Label (Left / Start Aligned)
        JLabel lblUser = new JLabel("Username / Email");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUser.setForeground(new Color(51, 65, 85));
        lblUser.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(lblUser, gbc);

        // 4. Username Text Field
        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(320, 36));
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(txtUsername, gbc);

        // 5. Password Label (Left / Start Aligned)
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblPass.setForeground(new Color(51, 65, 85));
        lblPass.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(lblPass, gbc);

        // 6. Password Field
        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(320, 36));
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(txtPassword, gbc);

        // 7. Login Button
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnLogin.setBackground(new Color(13, 110, 253));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(320, 38));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(btnLogin, gbc);

        // 8. Register Link
        JButton btnRegister = new JButton("Don't have an account? Register");
        btnRegister.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnRegister.setForeground(new Color(100, 116, 139));
        btnRegister.setContentAreaFilled(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(btnRegister, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Events
        btnLogin.addActionListener(e -> performLogin());
        btnRegister.addActionListener(e -> 
        {
            dispose();
            new SignupPage().setVisible(true);
        });
    }

    private void performLogin() 
    {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "Please enter both Username and Password!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try 
        {
            Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) 
            {
                String role = "STAFF";
                try 
                {
                    role = rs.getString("role");
                } catch (Exception ignored) {}

                JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + username + " (" + role + ")", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new MainDashboard(role).setVisible(true);
            } 
            else 
            {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
    }
}