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
public class DashboardPage extends JPanel 
{

    private JLabel lblTotalPatients, lblTotalDentists, lblTodayApp, lblPendingApp;
    private JLabel lblCompleted, lblRevenue, lblNewPatients, lblAvailDentists;
    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch;

    public DashboardPage() 
    {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(248, 250, 252));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // TOP METRICS GRID (8 CARDS)
        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 12, 12));
        gridPanel.setOpaque(false);

        lblTotalPatients = createCard(gridPanel, "Total Patients", new Color(34, 197, 94));
        lblTotalDentists = createCard(gridPanel, "Total Dentists", new Color(13, 110, 253));
        lblTodayApp = createCard(gridPanel, "Today's Appointments", new Color(139, 92, 246));
        lblPendingApp = createCard(gridPanel, "Pending Appointments", new Color(245, 158, 11));

        lblCompleted = createCard(gridPanel, "Completed Treatments", new Color(239, 68, 68));
        lblRevenue = createCard(gridPanel, "Total Revenue (LKR)", new Color(20, 184, 166));
        lblNewPatients = createCard(gridPanel, "New Patients", new Color(234, 179, 8));
        lblAvailDentists = createCard(gridPanel, "Available Dentists", new Color(6, 182, 212));

        add(gridPanel, BorderLayout.NORTH);

        // TABLE PANEL WITH SEARCH ON RIGHT SIDE
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Recent Appointments Overview"));

        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchBarPanel.setOpaque(false);
        searchBarPanel.add(new JLabel("Search Appointments: "));
        txtSearch = new JTextField(20);
        searchBarPanel.add(txtSearch);
        tablePanel.add(searchBarPanel, BorderLayout.NORTH);

        String[] cols = {"App No", "Patient Name", "Address", "Contact", "Dentist Name", "Treatment", "Status"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() 
        {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String text = txtSearch.getText();
                if (text.trim().length() == 0) sorter.setRowFilter(null);
                else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        loadDashboardData();
    }

    private JLabel createCard(JPanel parent, String title, Color color) 
    {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JLabel lblVal = new JLabel("0", SwingConstants.RIGHT);
        lblVal.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblVal.setForeground(Color.WHITE);

        JLabel lblT = new JLabel(title, SwingConstants.LEFT);
        lblT.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblT.setForeground(Color.WHITE);

        card.add(lblVal, BorderLayout.NORTH);
        card.add(lblT, BorderLayout.SOUTH);
        parent.add(card);
        return lblVal;
    }

    public void loadDashboardData() 
    {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            Statement st = conn.createStatement();

            // Dynamic Live Counts
            ResultSet rs1 = st.executeQuery("SELECT COUNT(*) FROM patients");
            if (rs1.next()) lblTotalPatients.setText(rs1.getString(1));

            ResultSet rs2 = st.executeQuery("SELECT COUNT(*) FROM dentists");
            if (rs2.next()) {
                lblTotalDentists.setText(rs2.getString(1));
                lblAvailDentists.setText(rs2.getString(1));
            }

            ResultSet rs3 = st.executeQuery("SELECT COUNT(*) FROM appointments");
            if (rs3.next()) lblTodayApp.setText(rs3.getString(1));

            ResultSet rs4 = st.executeQuery("SELECT COUNT(*) FROM appointments WHERE status='Pending'");
            if (rs4.next()) lblPendingApp.setText(rs4.getString(1));

            ResultSet rs5 = st.executeQuery("SELECT COUNT(*) FROM appointments WHERE status='Confirmed' OR status='Completed'");
            if (rs5.next()) lblCompleted.setText(rs5.getString(1));

            ResultSet rs6 = st.executeQuery("SELECT SUM(amount) FROM billing");
            if (rs6.next()) lblRevenue.setText(rs6.getString(1) != null ? rs6.getString(1) : "0.0");

            // Load Table Data Safely using Column Index Mapping
            model.setRowCount(0);
            ResultSet rsT = st.executeQuery("SELECT * FROM appointments ORDER BY appointment_no ASC");

            while (rsT.next()) 
            {
                String appNo = rsT.getString(2);
                String pName = rsT.getString(3);
                String address = rsT.getString(4);
                String contact = rsT.getString(5);
                String dName = rsT.getString(6);
                String treatment = rsT.getString(7);
                String status = rsT.getString(9);

                model.addRow(new Object[]{appNo, pName, address, contact, dName, treatment, status});
            }
        } 
        catch (Exception ex) 
        {
            // Fallback for Index Mapping variations
            try {
                model.setRowCount(0);
                Connection conn = DBConnection.getInstance().getConnection();
                Statement st = conn.createStatement();
                ResultSet rsT = st.executeQuery("SELECT * FROM appointments");
                while (rsT.next()) {
                    model.addRow(new Object[]{
                        rsT.getString(1), rsT.getString(2), rsT.getString(3),
                        rsT.getString(4), rsT.getString(5), rsT.getString(6), rsT.getString(7)
                    });
                }
            } catch (Exception ignored) {}
        }
    }
}