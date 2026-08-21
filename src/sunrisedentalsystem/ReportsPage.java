/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sunrisedentalsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
/**
 *
 * @author Future_Mind
 */
public class ReportsPage extends JPanel 
{
    private JTable tblPatients, tblAppointments;
    private DefaultTableModel modelPatients, modelAppointments;

    public ReportsPage() 
    {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(248, 250, 252));
        setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Header Title
        JLabel lblHeader = new JLabel("Reports & CSV Analytics");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblHeader.setForeground(new Color(15, 23, 42));
        add(lblHeader, BorderLayout.NORTH);

        // Tabbed Pane to Switch between Patients and Appointments Report Tables
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));

        // 1. Patients Report Tab
        JPanel pnlPatientsTab = createReportTabPanel(true);
        tabbedPane.addTab("👥 Patients Report", pnlPatientsTab);

        // 2. Appointments Report Tab
        JPanel pnlAppointmentsTab = createReportTabPanel(false);
        tabbedPane.addTab("📅 Appointments Report", pnlAppointmentsTab);

        add(tabbedPane, BorderLayout.CENTER);

        // Load Initial Table Data
        loadPatientsData();
        loadAppointmentsData();
    }

    private JPanel createReportTabPanel(boolean isPatient) 
    {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Table Model Setup
        if (isPatient) 
        {
            modelPatients = new DefaultTableModel(new String[]{"ID", "Patient Name", "Address", "Contact No", "Registered Date"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
            tblPatients = new JTable(modelPatients);
            formatTable(tblPatients);
            panel.add(new JScrollPane(tblPatients), BorderLayout.CENTER);
        } 
        else 
        {
            modelAppointments = new DefaultTableModel(new String[]{"App No", "Patient Name", "Dentist Name", "Treatment Type", "Date & Time", "Status", "Total Amount (LKR)"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
            tblAppointments = new JTable(modelAppointments);
            formatTable(tblAppointments);
            panel.add(new JScrollPane(tblAppointments), BorderLayout.CENTER);
        }

        // Bottom Controls (Refresh & Download CSV)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        bottomPanel.setOpaque(false);

        JButton btnRefresh = new JButton("↻ Refresh Data");
        btnRefresh.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnRefresh.setPreferredSize(new Dimension(130, 36));

        JButton btnDownload = new JButton(isPatient ? "⬇ Download Patients Report (CSV)" : "⬇ Download Appointments Report (CSV)");
        btnDownload.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnDownload.setBackground(new Color(13, 110, 253));
        btnDownload.setForeground(Color.WHITE);
        btnDownload.setPreferredSize(new Dimension(280, 36));
        btnDownload.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnDownload);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        if (isPatient) 
        {
            btnRefresh.addActionListener(e -> loadPatientsData());
            btnDownload.addActionListener(e -> exportToCSV(tblPatients, "Patients_Report.csv"));
        } 
        else 
        {
            btnRefresh.addActionListener(e -> loadAppointmentsData());
            btnDownload.addActionListener(e -> exportToCSV(tblAppointments, "Appointments_Report.csv"));
        }

        return panel;
    }

    private void formatTable(JTable table) 
    {
        table.setRowHeight(32);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(241, 245, 249));
        table.getTableHeader().setForeground(new Color(30, 41, 59));
        table.setShowGrid(true);
        table.setGridColor(new Color(226, 232, 240));
    }

    public void loadPatientsData() 
    {
        modelPatients.setRowCount(0);
        try 
        {
            Connection conn = DBConnection.getInstance().getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM patients");
            while (rs.next()) 
            {
                modelPatients.addRow(new Object[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getMetaData().getColumnCount() >= 5 ? rs.getString(5) : "N/A"
                });
            }
        } 
        catch (Exception ex) 
        {
            // Fallback if records are populated through appointments
            loadPatientsFallback();
        }
    }

    private void loadPatientsFallback() 
    {
        try 
        {
            Connection conn = DBConnection.getInstance().getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT DISTINCT patient_name, address, contact_no FROM appointments");
            int id = 1;
            while (rs.next()) 
            {
                modelPatients.addRow(new Object[]{
                    id++,
                    rs.getString("patient_name"),
                    rs.getString("address"),
                    rs.getString("contact_no"),
                    "Active"
                });
            }
        } 
        catch (Exception ignored) {}
    }

    public void loadAppointmentsData() 
    {
        modelAppointments.setRowCount(0);
        try 
        {
            Connection conn = DBConnection.getInstance().getConnection();
            Statement st = conn.createStatement();
            String sql = "SELECT a.appointment_no, a.patient_name, a.dentist_name, a.treatment_type, a.appointment_date, a.status, " +
                         "COALESCE(b.amount, 0.0) as amount " +
                         "FROM appointments a LEFT JOIN billing b ON a.appointment_no = b.appointment_no";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) 
            {
                modelAppointments.addRow(new Object[]{
                    rs.getString("appointment_no"),
                    rs.getString("patient_name"),
                    rs.getString("dentist_name"),
                    rs.getString("treatment_type"),
                    rs.getString("appointment_date"),
                    rs.getString("status"),
                    String.format("%.2f", rs.getDouble("amount"))
                });
            }
        } 
        catch (Exception ex) 
        {
            JOptionPane.showMessageDialog(this, "Error loading appointments: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToCSV(JTable table, String defaultFileName) 
    {
        if (table.getRowCount() == 0) 
        {
            JOptionPane.showMessageDialog(this, "No data available in table to export!", "Empty Table", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify File Destination to Save Report");
        fileChooser.setSelectedFile(new File(defaultFileName));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) 
        {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".csv")) 
            {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }

            try (FileWriter fw = new FileWriter(fileToSave)) 
            {
                // Write Column Headers
                for (int i = 0; i < table.getColumnCount(); i++) 
                {
                    fw.write(table.getColumnName(i) + (i == table.getColumnCount() - 1 ? "" : ","));
                }
                fw.write("\n");

                // Write Rows Data
                for (int i = 0; i < table.getRowCount(); i++) {
                    for (int j = 0; j < table.getColumnCount(); j++) 
                    {
                        Object val = table.getValueAt(i, j);
                        String strVal = val != null ? val.toString().replace(",", ";") : "";
                        fw.write(strVal + (j == table.getColumnCount() - 1 ? "" : ","));
                    }
                    fw.write("\n");
                }

                JOptionPane.showMessageDialog(this, "Report exported successfully!\nFile Saved: " + fileToSave.getAbsolutePath(), "Export Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) 
            {
                JOptionPane.showMessageDialog(this, "Failed to write CSV file: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}