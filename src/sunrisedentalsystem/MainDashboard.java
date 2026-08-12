/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sunrisedentalsystem;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Future_Mind
 */
public class MainDashboard extends JFrame 
{

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private DashboardPage dashboardPage;
    private PatientsPage patientsPage;
    private AddPatientPage addPatientPage;
    private AppointmentsPage appointmentsPage;
    private BookAppointmentPage bookAppointmentPage;
    private BillingPage billingPage;
    private HelpPage helpPage;
    private ReportsPage reportsPage;
    private DentistsPage dentistsPage;
    private PrescriptionsPage prescriptionsPage;
    private UsersPage usersPage;

    private JPanel pnlPatientsSub;
    private JPanel pnlAppSub;
    private JPanel pnlUsersSub;

    public MainDashboard() 
    {
        setTitle("Sunrise Dental Clinic - Reservation System");
        setSize(1280, 800);
        setMinimumSize(new Dimension(1024, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // SIDEBAR
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(15, 23, 42));
        sidebar.setPreferredSize(new Dimension(230, 800));

        JLabel lblLogo = new JLabel("SUNRISE DENTAL");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblLogo.setForeground(new Color(56, 189, 248));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));
        sidebar.add(lblLogo);

        JButton btnDash = createNavBtn("⌂  Dashboard");

        JButton btnPatientsMain = createNavBtn("👥  Patients ▼");
        pnlPatientsSub = createSubMenuPanel();
        JButton btnAddPatient = createSubNavBtn("  └ Add Patient");
        JButton btnViewPatients = createSubNavBtn("  └ View Directory");
        pnlPatientsSub.add(btnAddPatient);
        pnlPatientsSub.add(btnViewPatients);

        JButton btnDentists = createNavBtn("🩺  Dentists");

        JButton btnAppMain = createNavBtn("📅  Appointments ▼");
        pnlAppSub = createSubMenuPanel();
        JButton btnBookApp = createSubNavBtn("  └ Book Appointment");
        JButton btnViewApp = createSubNavBtn("  └ View Appointments");
        pnlAppSub.add(btnBookApp);
        pnlAppSub.add(btnViewApp);

        JButton btnBill = createNavBtn("💳  Billing & Receipts");
        JButton btnPresc = createNavBtn("✎  Prescriptions");

        JButton btnUsersMain = createNavBtn("👤  User Management ▼");
        pnlUsersSub = createSubMenuPanel();
        JButton btnManageUser = createSubNavBtn("  └ Manage Users");
        pnlUsersSub.add(btnManageUser);

        JButton btnReports = createNavBtn("📊  Reports & CSV");
        JButton btnHelp = createNavBtn("?  Help Manual");
        
        JButton btnExit = createNavBtn("▮  Exit System");
        btnExit.setForeground(new Color(248, 113, 113));

        sidebar.add(btnDash);
        sidebar.add(btnPatientsMain);
        sidebar.add(pnlPatientsSub);
        sidebar.add(btnDentists);
        sidebar.add(btnAppMain);
        sidebar.add(pnlAppSub);
        sidebar.add(btnBill);
        sidebar.add(btnPresc);
        sidebar.add(btnUsersMain);
        sidebar.add(pnlUsersSub);
        sidebar.add(btnReports);
        sidebar.add(btnHelp);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnExit);

        // CONTENT PANEL
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        dashboardPage = new DashboardPage();
        patientsPage = new PatientsPage();
        addPatientPage = new AddPatientPage();
        appointmentsPage = new AppointmentsPage();
        bookAppointmentPage = new BookAppointmentPage();
        billingPage = new BillingPage();
        helpPage = new HelpPage();
        reportsPage = new ReportsPage();
        dentistsPage = new DentistsPage();
        prescriptionsPage = new PrescriptionsPage();
        usersPage = new UsersPage();

        contentPanel.add(dashboardPage, "DASHBOARD");
        contentPanel.add(patientsPage, "PATIENTS");
        contentPanel.add(addPatientPage, "ADD_PATIENT");
        contentPanel.add(dentistsPage, "DENTISTS");
        contentPanel.add(appointmentsPage, "APPOINTMENTS");
        contentPanel.add(bookAppointmentPage, "BOOK_APP");
        contentPanel.add(billingPage, "BILLING");
        contentPanel.add(prescriptionsPage, "PRESCRIPTIONS");
        contentPanel.add(usersPage, "USERS");
        contentPanel.add(reportsPage, "REPORTS");
        contentPanel.add(helpPage, "HELP");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Navigation Actions
        btnDash.addActionListener(e -> { closeSubMenus(); dashboardPage.loadDashboardData(); showPage("DASHBOARD"); });
        btnPatientsMain.addActionListener(e -> pnlPatientsSub.setVisible(!pnlPatientsSub.isVisible()));
        btnAddPatient.addActionListener(e -> showPage("ADD_PATIENT"));
        btnViewPatients.addActionListener(e -> { patientsPage.loadPatientRecords(); showPage("PATIENTS"); });
        btnDentists.addActionListener(e -> { closeSubMenus(); showPage("DENTISTS"); });

        btnAppMain.addActionListener(e -> pnlAppSub.setVisible(!pnlAppSub.isVisible()));
        btnBookApp.addActionListener(e -> 
        {
            bookAppointmentPage.loadDentistsDropdown();
            showPage("BOOK_APP");
        });
        btnViewApp.addActionListener(e -> { appointmentsPage.loadAppointments(); showPage("APPOINTMENTS"); });

        btnBill.addActionListener(e -> 
        { 
            closeSubMenus(); 
            billingPage.loadBillingData(); 
            showPage("BILLING"); 
        });

        btnPresc.addActionListener(e -> { closeSubMenus(); showPage("PRESCRIPTIONS"); });
        btnUsersMain.addActionListener(e -> pnlUsersSub.setVisible(!pnlUsersSub.isVisible()));
        btnManageUser.addActionListener(e -> showPage("USERS"));

        btnReports.addActionListener(e -> { closeSubMenus(); showPage("REPORTS"); });
        btnHelp.addActionListener(e -> { closeSubMenus(); showPage("HELP"); });

        // Safe Exit System Action Event
        btnExit.addActionListener(e -> 
        {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to safely exit the application?",
                "Confirm System Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0); // Safely closes the application
            }
        });
    }

    private void showPage(String name) 
    { 
        cardLayout.show(contentPanel, name); 
    }

    private void closeSubMenus() 
    {
        pnlPatientsSub.setVisible(false);
        pnlAppSub.setVisible(false);
        pnlUsersSub.setVisible(false);
    }

    private JPanel createSubMenuPanel() 
    {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.Y_AXIS));
        pnl.setOpaque(false);
        pnl.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnl.setVisible(false);
        return pnl;
    }

    private JButton createNavBtn(String text) 
    {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setForeground(new Color(226, 232, 240));
        btn.setBackground(new Color(15, 23, 42));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(230, 36));
        btn.setPreferredSize(new Dimension(230, 36));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 10));
        return btn;
    }

    private JButton createSubNavBtn(String text) 
    {
        JButton btn = createNavBtn(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setForeground(new Color(148, 163, 184));
        return btn;
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> new MainDashboard().setVisible(true));
    }
}