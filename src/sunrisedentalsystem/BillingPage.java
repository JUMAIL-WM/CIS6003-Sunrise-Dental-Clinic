/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sunrisedentalsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 *
 * @author Future_Mind
 */
public class BillingPage extends JPanel 
{

    private JTable table;
    private DefaultTableModel model;
    private JTextArea txtReceipt;
    private JTextField txtSearchAppNo;

    public BillingPage() 
    {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // TOP HEADER SEARCH BAR
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(Color.WHITE);

        topPanel.add(new JLabel("Appointment No:"));
        txtSearchAppNo = new JTextField(15);
        topPanel.add(txtSearchAppNo);

        JButton btnSearch = new JButton("Display & Search");
        btnSearch.setBackground(new Color(13, 110, 253));
        btnSearch.setForeground(Color.WHITE);
        topPanel.add(btnSearch);

        JButton btnPrint = new JButton("Print Receipt");
        btnPrint.setBackground(new Color(34, 197, 94));
        btnPrint.setForeground(Color.WHITE);
        topPanel.add(btnPrint);

        add(topPanel, BorderLayout.NORTH);

        // RESPONSIVE SPLIT PANE
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.55);

        String[] cols = {"Bill ID", "App No", "Patient Name", "Total Bill (LKR)", "Status"};
        model = new DefaultTableModel(cols, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        splitPane.setLeftComponent(new JScrollPane(table));

        txtReceipt = new JTextArea();
        txtReceipt.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtReceipt.setEditable(false);
        txtReceipt.setBackground(new Color(250, 250, 250));
        splitPane.setRightComponent(new JScrollPane(txtReceipt));

        add(splitPane, BorderLayout.CENTER);

        // Search Action
        btnSearch.addActionListener(e -> 
        {
            String searchNo = txtSearchAppNo.getText().trim();
            if (searchNo.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, "Please enter an Appointment Number!");
                return;
            }

            boolean found = false;
            for (int i = 0; i < table.getRowCount(); i++) 
            {
                if (table.getValueAt(i, 1).toString().equalsIgnoreCase(searchNo)) 
                {
                    table.setRowSelectionInterval(i, i);
                    generateReceiptFromRow(i);
                    found = true;
                    break;
                }
            }

            if (!found) 
            {
                txtReceipt.setText("\n   No record found for Appointment No: " + searchNo);
            }
        });

        // Print Action
        btnPrint.addActionListener(e -> 
        {
            if (txtReceipt.getText().trim().isEmpty() || txtReceipt.getText().contains("No record found")) 
            {
                JOptionPane.showMessageDialog(this, "Please select an appointment from table first!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try 
            {
                txtReceipt.print();
            } 
            catch (Exception ex) 
            {
                ex.printStackTrace();
            }
        });

        // Row Selection Event
        table.getSelectionModel().addListSelectionListener(e -> 
        {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int selectedRow = table.getSelectedRow();
                String appNo = table.getValueAt(selectedRow, 1).toString();
                txtSearchAppNo.setText(appNo);
                generateReceiptFromRow(selectedRow);
            }
        });

        loadBillingData();
    }

    private void generateReceiptFromRow(int row) 
    {
        String billId = table.getValueAt(row, 0).toString();
        String appNo = table.getValueAt(row, 1).toString();
        String patientName = table.getValueAt(row, 2).toString();
        String amount = table.getValueAt(row, 3).toString();
        String status = table.getValueAt(row, 4).toString();

        txtReceipt.setText
        (
            "==========================================\n" +
            "       SUNRISE DENTAL CLINIC RECEIPT      \n" +
            "==========================================\n\n" +
            " Receipt ID     : REC-" + String.format("%04d", Integer.parseInt(billId)) + "\n" +
            " Appointment No : " + appNo + "\n" +
            " Patient Name   : " + patientName + "\n" +
            " Date           : 2026-08-07\n\n" +
            "------------------------------------------\n" +
            " Consultation Fee : LKR " + amount + "\n" +
            " TOTAL AMOUNT     : LKR " + amount + "\n" +
            " PAYMENT STATUS   : " + status.toUpperCase() + "\n" +
            "==========================================\n\n" +
            "    Thank you for choosing Sunrise Dental! \n"
        );
    }

    public void loadBillingData() 
    {
        try 
        {
            model.setRowCount(0);
            Connection conn = DBConnection.getInstance().getConnection();
            Statement st = conn.createStatement();
            
            // Reads exact amount from billing table
            ResultSet rs = st.executeQuery("SELECT a.appointment_no, a.patient_name, b.amount FROM appointments a LEFT JOIN billing b ON a.appointment_no = b.appointment_no ORDER BY a.appointment_no ASC");

            int billIdCounter = 1;
            while (rs.next()) 
            {
                String appNo = rs.getString(1);
                String name = rs.getString(2);
                String amount = rs.getString(3);

                model.addRow(new Object[]{
                    billIdCounter++,
                    appNo,
                    name,
                    amount != null ? amount : "0.0",
                    "Paid"
                });
            }
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }
}