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
import java.sql.ResultSet;
/**
 *
 * @author Future_Mind
 */
public class LoginPage extends JFrame 
{

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSignup;

    public LoginPage() 
    {
        setTitle("Sunrise Dental Clinic - Login");
        setSize(420, 520);
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
        cardPanel.setPreferredSize(new Dimension(340, 420));
        cardPanel.setLayout(null);
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 224, 230), 1));

        // Header Title
        JLabel lblTitle = new JLabel("SUNRISE DENTAL CLINIC", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(new Color(13, 110, 253));
        lblTitle.setBounds(10, 25, 320, 25);
        cardPanel.add(lblTitle);

        JLabel lblSubTitle = new JLabel("Dental Reservation System", SwingConstants.CENTER);
        lblSubTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSubTitle.setForeground(Color.GRAY);
        lblSubTitle.setBounds(10, 50, 320, 20);
        cardPanel.add(lblSubTitle);

        // 1. Username Label & Field
        JLabel lblUsername = new JLabel("Username / Staff ID:");
        lblUsername.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUsername.setForeground(new Color(50, 50, 50));
        lblUsername.setBounds(30, 95, 280, 20);
        cardPanel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtUsername.setBounds(30, 120, 280, 35);
        cardPanel.add(txtUsername);

        // 2. Password Label & Field
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblPassword.setForeground(new Color(50, 50, 50));
        lblPassword.setBounds(30, 165, 280, 20);
        cardPanel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtPassword.setBounds(30, 190, 280, 35);
        cardPanel.add(txtPassword);

        // Sign In Button
        btnLogin = new JButton("Sign In");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setBackground(new Color(13, 110, 253));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBounds(30, 250, 280, 40);
        cardPanel.add(btnLogin);

        // Create Account Link
        btnSignup = new JButton("Create New Staff Account");
        btnSignup.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnSignup.setForeground(new Color(13, 110, 253));
        btnSignup.setContentAreaFilled(false);
        btnSignup.setBorderPainted(false);
        btnSignup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSignup.setBounds(30, 310, 280, 25);
        cardPanel.add(btnSignup);

        mainPanel.add(cardPanel);
        add(mainPanel);

        // Event Listeners
        btnLogin.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                performLogin();
            }
        });

        btnSignup.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                new SignupPage().setVisible(true);
                dispose();
            }
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
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) 
            {
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + username, "Success", JOptionPane.INFORMATION_MESSAGE);
                new MainDashboard().setVisible(true);
                dispose();
            } 
            else 
            {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> 
        {
            new LoginPage().setVisible(true);
        });
    }
}