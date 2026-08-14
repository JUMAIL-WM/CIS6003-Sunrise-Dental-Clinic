/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sunrisedentalsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
/**
 *
 * @author Future_Mind
 */
public class DentistsPage extends JPanel 
{
    private DefaultTableModel model;
    private JTable tbl;

    public DentistsPage() 
    {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel title = new JLabel("Dentists Directory");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        JButton btnAdd = new JButton("+ Add Dentist");
        btnAdd.setBackground(new Color(13, 110, 253));
        btnAdd.setForeground(Color.WHITE);

        btnAdd.addActionListener(e -> addDentistDialog());

        top.add(title, BorderLayout.WEST);
        top.add(btnAdd, BorderLayout.EAST);

        model = new DefaultTableModel(new String[]{"Dentist ID", "Name", "Specialization", "Contact"}, 0);
        tbl = new JTable(model);
        tbl.setRowHeight(32);

        JPanel btm = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btm.setOpaque(false);
        JButton btnDel = new JButton("Delete Dentist");
        btnDel.setBackground(new Color(220, 53, 69));
        btnDel.setForeground(Color.WHITE);

        btnDel.addActionListener(e -> 
        {
            int row = tbl.getSelectedRow();
            if (row != -1) 
            {
                String id = model.getValueAt(row, 0).toString();
                try {
                    Connection conn = DBConnection.getInstance().getConnection();
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM dentists WHERE dentist_id=?");
                    ps.setString(1, id);
                    ps.executeUpdate();
                    loadData();
                } 
                catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        btm.add(btnDel);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        add(btm, BorderLayout.SOUTH);

        loadData();
    }

    private void addDentistDialog() 
    {
        JTextField txtID = new JTextField("D00" + (int)(Math.random()*100));
        JTextField txtName = new JTextField();
        JTextField txtSpec = new JTextField("Orthodontics");
        JTextField txtContact = new JTextField();

        Object[] fields = { "Dentist ID:", txtID, "Name:", txtName, "Specialization:", txtSpec, "Contact:", txtContact };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add New Dentist", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) 
        {
            try 
            {
                Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO dentists VALUES (?,?,?,?)");
                ps.setString(1, txtID.getText());
                ps.setString(2, txtName.getText());
                ps.setString(3, txtSpec.getText());
                ps.setString(4, txtContact.getText());
                ps.executeUpdate();
                loadData();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    public void loadData() 
    {
        model.setRowCount(0);
        try 
        {
            Connection conn = DBConnection.getInstance().getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM dentists");
            while (rs.next()) 
            {
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
            }
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }
}
