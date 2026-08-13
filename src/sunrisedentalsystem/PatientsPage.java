/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sunrisedentalsystem;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 *
 * @author Future_Mind
 */
public class PatientsPage extends JPanel 
{

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch;

    public PatientsPage() 
    {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Search Header (Top Right)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Search Patient (ID / Name / Phone): "));
        txtSearch = new JTextField(22);
        searchPanel.add(txtSearch);

        add(searchPanel, BorderLayout.NORTH);

        // Table Model Setup
        String[] cols = {"Patient ID", "Name", "Age", "Gender", "Contact", "Address"};
        model = new DefaultTableModel(cols, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false; // Prevent manual editing in table
            }
        };

        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Live Filter Text Event
        txtSearch.getDocument().addDocumentListener(new DocumentListener() 
        {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() 
            {
                String text = txtSearch.getText();
                if (text.trim().length() == 0) 
                {
                    sorter.setRowFilter(null);
                } 
                else 
                {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        loadPatientRecords();
    }

    public void loadPatientRecords() 
    {
        try 
        {
            model.setRowCount(0); // Clear old rows
            Connection conn = DBConnection.getInstance().getConnection();
            Statement st = conn.createStatement();
            
            // Execute Query to fetch all patients
            ResultSet rs = st.executeQuery("SELECT * FROM patients");
            
            while (rs.next()) 
            {
                // Fetch columns safely by index numbers (1 to 6) to avoid column name mismatch
                String id = rs.getString(1);
                String name = rs.getString(2);
                String age = rs.getString(3);
                String gender = rs.getString(4);
                String contact = rs.getString(5);
                String address = rs.getString(6);

                model.addRow(new Object[]{id, name, age, gender, contact, address});
            }
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patients: " + ex.getMessage());
        }
    }
}
