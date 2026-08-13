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
import java.sql.Statement;
/**
 *
 * @author Future_Mind
 */
public class AddPatientPage extends JPanel 
{

    private JTextField txtPatientID, txtName, txtAge, txtContact, txtAddress;
    private JComboBox<String> cbGender;

    public AddPatientPage() 
    {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(248, 250, 252));

        JLabel lblHeader = new JLabel("Add New Patient Record");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(15, 25, 0, 25));
        add(lblHeader, BorderLayout.NORTH);

        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createCompoundBorder
        (
                BorderFactory.createEmptyBorder(10, 25, 15, 25),
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 15, 10, 15);

        txtPatientID = new JTextField();
        txtName = new JTextField();
        txtAge = new JTextField();
        cbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        txtContact = new JTextField();
        txtAddress = new JTextField();

        addResponsiveRow(formContainer, gbc, 0, "Patient ID:", txtPatientID);
        addResponsiveRow(formContainer, gbc, 1, "Full Name:", txtName);
        addResponsiveRow(formContainer, gbc, 2, "Age:", txtAge);
        addResponsiveRow(formContainer, gbc, 3, "Gender:", cbGender);
        addResponsiveRow(formContainer, gbc, 4, "Contact Number:", txtContact);
        addResponsiveRow(formContainer, gbc, 5, "Address:", txtAddress);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnPanel.setOpaque(false);

        JButton btnClear = new JButton("Clear Fields");
        btnClear.setPreferredSize(new Dimension(120, 36));
        btnClear.setBackground(new Color(100, 116, 139));
        btnClear.setForeground(Color.WHITE);

        JButton btnSave = new JButton("Save Patient");
        btnSave.setPreferredSize(new Dimension(150, 36));
        btnSave.setBackground(new Color(13, 110, 253));
        btnSave.setForeground(Color.WHITE);

        btnPanel.add(btnClear);
        btnPanel.add(btnSave);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        formContainer.add(btnPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(formContainer);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        btnClear.addActionListener(e -> clearFields());
        btnSave.addActionListener(e -> savePatient());

        generateAutoPatientID();
    }

    private void addResponsiveRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent comp) 
    {
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.75;
        comp.setFont(new Font("SansSerif", Font.PLAIN, 13));
        comp.setPreferredSize(new Dimension(comp.getPreferredSize().width, 35));
        panel.add(comp, gbc);
    }

    private void clearFields() 
    {
        txtName.setText("");
        txtAge.setText("");
        txtContact.setText("");
        txtAddress.setText("");
        generateAutoPatientID();
    }

    private void generateAutoPatientID() 
    {
        try 
        {
            Connection conn = DBConnection.getInstance().getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM patients");
            if (rs.next()) 
            {
                int nextId = rs.getInt(1) + 1;
                txtPatientID.setText(String.format("P%04d", nextId));
            }
        } 
        catch (Exception ex) { txtPatientID.setText("P0001"); }
    }

    private void savePatient() 
    {
        if (txtName.getText().trim().isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "Please enter Patient Name!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try 
        {
            Connection conn = DBConnection.getInstance().getConnection();
            
            // Flexible Query: Column names not show auto save
            PreparedStatement pst = conn.prepareStatement("INSERT INTO patients VALUES (?, ?, ?, ?, ?, ?)");
            pst.setString(1, txtPatientID.getText().trim());
            pst.setString(2, txtName.getText().trim());
            
            int ageVal = 0;
            try { ageVal = Integer.parseInt(txtAge.getText().trim()); } catch (Exception ignored) {}
            pst.setInt(3, ageVal);
            
            pst.setString(4, cbGender.getSelectedItem().toString());
            pst.setString(5, txtContact.getText().trim());
            pst.setString(6, txtAddress.getText().trim());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient Saved Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, "Error Saving Patient: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}