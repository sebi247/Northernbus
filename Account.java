import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Account extends JFrame implements ActionListener {

    private JButton backButton;
    private JLabel nameLabel, emailLabel, passwordLabel;
    private JTextField nameField, emailField, passwordField;

    private int loggedInUserId;



    Account(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
        createAccountPageUI();
    }

    private void createAccountPageUI() {
        this.setTitle("NorthernBus - Account");
        setLayout(new BorderLayout());

        ImageIcon logo = new ImageIcon(ClassLoader.getSystemResource("Images/logo.png"));
        this.setIconImage(logo.getImage());

        Image logo1 = logo.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        ImageIcon icon2 = new ImageIcon(logo1);

        JLabel logoLabel = new JLabel(icon2, SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(JLabel.TOP);
        add(logoLabel, BorderLayout.NORTH);

        JPanel accountPanel = new JPanel(new GridLayout(4, 2));
        nameLabel = new JLabel("Name:");
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        accountPanel.add(nameLabel);

        nameField = new JTextField();
        accountPanel.add(nameField);

        emailLabel = new JLabel("Email:");
        emailLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        accountPanel.add(emailLabel);

        emailField = new JTextField();
        accountPanel.add(emailField);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        accountPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        accountPanel.add(passwordField);

        backButton = new JButton("Back");
        backButton.setBackground(new Color(0xE30000));
        backButton.setForeground(new Color(0xFFFFFF));
        backButton.addActionListener(this);
        backButton.setPreferredSize(new Dimension(backButton.getWidth(), 50));

        accountPanel.add(backButton);

        add(accountPanel, BorderLayout.CENTER);

        this.setResizable(false);
        this.setSize(800, 600);
        this.setVisible(true);
        this.getContentPane().setBackground(Color.white);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == backButton){
            HomePage home = new HomePage(loggedInUserId);
            home.setVisible(true);
            this.setVisible(false);
        }
    }
}
