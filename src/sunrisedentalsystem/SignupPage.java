/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sunrisedentalsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
/**
 *
 * @author Future_Mind
 */
public class SignupPage extends JFrame 
{

    private JTextField txtFullName;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnRegister;
    private JButton btnBackToLogin;

    public SignupPage() 
    {
        setTitle("Sunrise Dental Clinic - Staff Sign Up");
        setSize(420, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Background Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setLayout(new GridBagLayout());

        // Card Container Panel
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setPreferredSize(new Dimension(340, 440));
        cardPanel.setLayout(null);
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 224, 230), 1));

        // Header Title
        JLabel lblTitle = new JLabel("REGISTER STAFF ACCOUNT", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitle.setForeground(new Color(13, 110, 253));
        lblTitle.setBounds(10, 20, 320, 25);
        cardPanel.add(lblTitle);

        // 1. Full Name Label & Field
        JLabel lblFullName = new JLabel("Full Name:");
        lblFullName.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblFullName.setForeground(new Color(50, 50, 50));
        lblFullName.setBounds(30, 65, 280, 20);
        cardPanel.add(lblFullName);

        txtFullName = new JTextField();
        txtFullName.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtFullName.setBounds(30, 88, 280, 35);
        cardPanel.add(txtFullName);

        // 2. Username Label & Field
        JLabel lblUsername = new JLabel("Username / Staff ID:");
        lblUsername.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUsername.setForeground(new Color(50, 50, 50));
        lblUsername.setBounds(30, 133, 280, 20);
        cardPanel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtUsername.setBounds(30, 156, 280, 35);
        cardPanel.add(txtUsername);

        // 3. Password Label & Field
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblPassword.setForeground(new Color(50, 50, 50));
        lblPassword.setBounds(30, 201, 280, 20);
        cardPanel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtPassword.setBounds(30, 224, 280, 35);
        cardPanel.add(txtPassword);

        // Register Account Button
        btnRegister = new JButton("Register Account");
        btnRegister.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRegister.setBackground(new Color(13, 110, 253));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBounds(30, 280, 280, 40);
        cardPanel.add(btnRegister);

        // Back to Login Link
        btnBackToLogin = new JButton("Back to Login");
        btnBackToLogin.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnBackToLogin.setForeground(Color.GRAY);
        btnBackToLogin.setContentAreaFilled(false);
        btnBackToLogin.setBorderPainted(false);
        btnBackToLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBackToLogin.setBounds(30, 335, 280, 25);
        cardPanel.add(btnBackToLogin);

        mainPanel.add(cardPanel);
        add(mainPanel);

        // Event Listeners
        btnRegister.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                performRegistration();
            }
        });

        btnBackToLogin.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginPage().setVisible(true);
                dispose();
            }
        });
    }

    private void performRegistration() 
    {
        String fullName = txtFullName.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, "Staff");

            int rows = pst.executeUpdate();
            if (rows > 0) 
            {
                JOptionPane.showMessageDialog(this, "Account Registered Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new LoginPage().setVisible(true);
                dispose();
            }
        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, "Error Saving User: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> 
        {
            new SignupPage().setVisible(true);
        });
    }
}