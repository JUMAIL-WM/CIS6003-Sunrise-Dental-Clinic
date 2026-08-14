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
public class BookAppointmentPage extends JPanel 
{

    private JTextField txtAppNo, txtPatientName, txtAddress, txtContact, txtFee, txtDate;
    private JComboBox<String> cbDentist, cbTreatment;

    public BookAppointmentPage() 
    {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(248, 250, 252));

        JLabel lblHeader = new JLabel("Register New Appointment");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(15, 25, 0, 25));
        add(lblHeader, BorderLayout.NORTH);

        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 25, 15, 25),
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 15, 8, 15);

        txtAppNo = new JTextField();
        txtPatientName = new JTextField();
        txtAddress = new JTextField();
        txtContact = new JTextField();

        cbDentist = new JComboBox<>();
        cbTreatment = new JComboBox<>(new String[]{
            "Cleaning & Polishing", "Tooth Filling", "Root Canal Therapy",
            "Teeth Whitening", "Tooth Extraction", "Dental Implants",
            "Orthodontic Braces", "Crowns & Bridges"
        });

        txtDate = new JTextField("2026-08-07 10:00:00");
        txtFee = new JTextField(); 

        addResponsiveRow(formContainer, gbc, 0, "Appointment Number:", txtAppNo);
        addResponsiveRow(formContainer, gbc, 1, "Patient Name:", txtPatientName);
        addResponsiveRow(formContainer, gbc, 2, "Address:", txtAddress);
        addResponsiveRow(formContainer, gbc, 3, "Contact Number:", txtContact);
        addResponsiveRow(formContainer, gbc, 4, "Dentist Name:", cbDentist);
        addResponsiveRow(formContainer, gbc, 5, "Treatment Type:", cbTreatment);
        addResponsiveRow(formContainer, gbc, 6, "Appointment Date & Time:", txtDate);
        addResponsiveRow(formContainer, gbc, 7, "Consultation Fee (LKR):", txtFee);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnPanel.setOpaque(false);

        JButton btnClear = new JButton("Clear Form");
        btnClear.setPreferredSize(new Dimension(120, 36));
        btnClear.setBackground(new Color(100, 116, 139));
        btnClear.setForeground(Color.WHITE);

        JButton btnSave = new JButton("Save & Generate Bill");
        btnSave.setPreferredSize(new Dimension(170, 36));
        btnSave.setBackground(new Color(109, 40, 217));
        btnSave.setForeground(Color.WHITE);

        btnPanel.add(btnClear);
        btnPanel.add(btnSave);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        formContainer.add(btnPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(formContainer);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        btnClear.addActionListener(e -> clearFields());
        btnSave.addActionListener(e -> saveAppointment());

        loadDentistsDropdown();
        generateAutoAppNo();
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

    public void loadDentistsDropdown() 
    {
        try 
        {
            cbDentist.removeAllItems();
            Connection conn = DBConnection.getInstance().getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM dentists");

            while (rs.next()) 
            {
                String docName = rs.getString(2);
                String spec = "";
                try { spec = rs.getString(3); } catch (Exception ignored) {}
                cbDentist.addItem("Dr. " + docName + (spec.isEmpty() ? "" : " (" + spec + ")"));
            }

            if (cbDentist.getItemCount() == 0) 
            {
                cbDentist.addItem("Dr. Smith (General)");
            }
        } 
        catch (Exception ex) 
        {
            cbDentist.addItem("Dr. Smith (General)");
        }
    }

    public void clearFields() 
    {
        txtPatientName.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        txtFee.setText("");
        generateAutoAppNo();
    }

    private void generateAutoAppNo() 
    {
        try 
        {
            Connection conn = DBConnection.getInstance().getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM appointments");
            if (rs.next()) 
            {
                int count = rs.getInt(1) + 1;
                txtAppNo.setText(String.format("A%04d", count));
            } 
            else 
            {
                txtAppNo.setText("A0001");
            }
        } 
        catch (Exception ex) 
        {
            txtAppNo.setText("A0001");
        }
    }

    private void saveAppointment() 
    {
        if (txtPatientName.getText().trim().isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "Please enter Patient Name!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (txtFee.getText().trim().isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "Please enter Consultation Fee!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try 
        {
            Connection conn = DBConnection.getInstance().getConnection();

            // 1. Safe Save into Appointments (Tries explicitly with Primary Key Column)
            try 
            {
                PreparedStatement pst = conn.prepareStatement
                (
                    "INSERT INTO appointments (appointment_no, patient_name, address, dentist_name, treatment_type, appointment_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)"
                );
                pst.setString(1, txtAppNo.getText().trim());
                pst.setString(2, txtPatientName.getText().trim());
                pst.setString(3, txtAddress.getText().trim());
                pst.setString(4, cbDentist.getSelectedItem() != null ? cbDentist.getSelectedItem().toString() : "Dr. Smith");
                pst.setString(5, cbTreatment.getSelectedItem().toString());
                pst.setString(6, txtDate.getText().trim());
                pst.setString(7, "Pending");
                pst.executeUpdate();
            } 
            catch (Exception ex1) 
            {
                // Secondary Fallback for Auto-Increment Primary Key Table Structure
                PreparedStatement pst = conn.prepareStatement
                (
                    "INSERT INTO appointments VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?)"
                );
                pst.setString(1, txtAppNo.getText().trim());
                pst.setString(2, txtPatientName.getText().trim());
                pst.setString(3, txtAddress.getText().trim());
                pst.setString(4, txtContact.getText().trim());
                pst.setString(5, cbDentist.getSelectedItem() != null ? cbDentist.getSelectedItem().toString() : "Dr. Smith");
                pst.setString(6, cbTreatment.getSelectedItem().toString());
                pst.setString(7, txtDate.getText().trim());
                pst.setString(8, "Pending");
                pst.executeUpdate();
            }

            // 2. Fetch Treatment Cost & Calculate Total (Treatment Cost + Consultation Fee)
            double consultationFee = Double.parseDouble(txtFee.getText().trim());
            double treatmentCost = 0.0;
            
            try 
            {
                PreparedStatement pstCost = conn.prepareStatement("SELECT cost FROM treatments WHERE treatment_name = ?");
                pstCost.setString(1, cbTreatment.getSelectedItem().toString());
                ResultSet rsCost = pstCost.executeQuery();
                if (rsCost.next()) 
                {
                    treatmentCost = rsCost.getDouble("cost");
                }
            } 
            catch (Exception ignored) {}

            double totalBill = treatmentCost + consultationFee;

            // 3. Insert into Billing Table
            try 
            {
                PreparedStatement pstBill = conn.prepareStatement(
                    "INSERT INTO billing (appointment_no, patient_name, amount, status) VALUES (?, ?, ?, 'Paid')"
                );
                pstBill.setString(1, txtAppNo.getText().trim());
                pstBill.setString(2, txtPatientName.getText().trim());
                pstBill.setDouble(3, totalBill);
                pstBill.executeUpdate();
            } 
            catch (Exception ignored) {}

            JOptionPane.showMessageDialog(this, "Appointment Booked & Bill Generated Successfully!\nTotal Bill: LKR " + totalBill, "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();

        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, "Error Saving Appointment: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}