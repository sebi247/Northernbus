import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Login extends JFrame implements ActionListener {

    JButton login, register;
    JTextField emailText;
    JPasswordField passwordText;

    JFrame frame = new JFrame();

    private int loggedInUserId = -1;

    Login() {
        this.setTitle("NorthernBus");
        setLayout(null);

        ImageIcon logo = new ImageIcon(ClassLoader.getSystemResource("Images/logo.png"));
        this.setIconImage(logo.getImage());

        Image logo1 = logo.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        ImageIcon icon2 = new ImageIcon(logo1);

        JLabel Logo = new JLabel(icon2, SwingConstants.CENTER);
        Logo.setBounds(-150, 30, 800, 760);
        Logo.setVerticalAlignment(JLabel.TOP);
        add(Logo);

        JLabel label = new JLabel("NorthernBus", SwingConstants.CENTER);
        label.setFont(new Font("Times New Roman", Font.BOLD, 30));
        label.setBounds(0, 80, 800, 760);
        label.setVerticalAlignment(JLabel.TOP);
        label.setForeground(Color.red);
        add(label);

        JLabel email = new JLabel("Email:", SwingConstants.CENTER);
        email.setFont(new Font("Times New Roman", Font.BOLD, 28));
        email.setBounds(120, 200, 250, 30);
        email.setForeground(new Color(0xE30000));
        add(email);

        emailText = new JTextField();
        emailText.setVisible(true);
        emailText.setBounds(300, 200, 230, 30);
        emailText.setFont(new Font("Times New Roman", Font.BOLD, 14));
        emailText.setForeground(new Color(0xE30000));
        add(emailText);

        JLabel password = new JLabel("Password:", SwingConstants.CENTER);
        password.setFont(new Font("Times New Roman", Font.BOLD, 28));
        password.setBounds(95, 270, 250, 30);
        password.setForeground(new Color(0xE30000));
        add(password);

        passwordText = new JPasswordField();
        passwordText.setVisible(true);
        passwordText.setBounds(300, 270, 230, 30);
        passwordText.setFont(new Font("Times New Roman", Font.BOLD, 14));
        passwordText.setForeground(new Color(0xE30000));
        add(passwordText);

        login = new JButton("Login");
        login.setBounds(300, 350, 100, 30);
        login.setBackground(new Color(0xE30000));
        login.setForeground(new Color(0xFFFFFF));
        login.addActionListener(this);
        add(login);

        register = new JButton("Register");
        register.setBounds(420, 350, 100, 30);
        register.setBackground(new Color(0xE30000));
        register.setForeground(new Color(0xFFFFFF));
        register.addActionListener(this);
        add(register);

        //this.setIconImage(logo.getImage());
        this.setResizable(false);
        this.setSize(800, 760);
        this.setVisible(true);
        this.getContentPane().setBackground(Color.blue);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login) {
            String email = emailText.getText();
            String password = passwordText.getText();

            try {
                LoginHelper loginHelper = new LoginHelper("jdbc:mysql://localhost/northernbus", "root", "Sebires123");
                boolean loginSuccessful = loginHelper.checkLogin(email, password);
                if (loginSuccessful) {
                    loggedInUserId = loginHelper.getUserId(email, password);
                    System.out.println("Logged in user ID: " + loggedInUserId);
                    HomePage home = new HomePage(loggedInUserId);
                    home.setVisible(true);
                    this.setVisible(false);
                    loginHelper.close();
                } else {
                    JOptionPane.showMessageDialog(frame, "Login failed.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else if (ae.getSource() == register) {
            Register register = new Register();
            register.setVisible(true);
            this.setVisible(false);
        }
    }



}

