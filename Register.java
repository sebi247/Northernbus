import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;

public class Register extends JFrame implements ActionListener {
    private JTextField emailText, phoneText, addressText;
    private JPasswordField passwordText, confirmPasswordText;

    private JButton register, cancel;

    private static final String url = "jdbc:mysql://localhost/northernbus";
    private static final String user = "root";
    private static final String password = "Sebires123";

    Register() {



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

        JLabel label = new JLabel("Register", SwingConstants.CENTER);
        label.setFont(new Font("Times New Roman", Font.BOLD, 30));
        label.setBounds(0, 80, 800, 760);
        label.setVerticalAlignment(JLabel.TOP);
        label.setForeground(Color.red);
        add(label);

        JLabel email = new JLabel("Email:", SwingConstants.CENTER);
        email.setFont(new Font("Times New Roman", Font.BOLD, 28));
        email.setBounds(130, 200, 250, 30);
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
        password.setBounds(105, 270, 250, 30);
        password.setForeground(new Color(0xE30000));
        add(password);

        passwordText = new JPasswordField();
        passwordText.setVisible(true);
        passwordText.setBounds(300, 270, 230, 30);
        passwordText.setFont(new Font("Times New Roman", Font.BOLD, 14));
        passwordText.setForeground(new Color(0xE30000));
        add(passwordText);

        JLabel confirmPassword = new JLabel("Confirm Password:", SwingConstants.CENTER);
        confirmPassword.setFont(new Font("Times New Roman", Font.BOLD, 28));
        confirmPassword.setBounds(5, 340, 350, 30);
        confirmPassword.setForeground(new Color(0xE30000));
        add(confirmPassword);

        confirmPasswordText = new JPasswordField();
        confirmPasswordText.setVisible(true);
        confirmPasswordText.setBounds(300, 340, 230, 30);
        confirmPasswordText.setFont(new Font("Times New Roman", Font.BOLD, 14));
        confirmPasswordText.setForeground(new Color(0xE30000));
        add(confirmPasswordText);

        JLabel phone = new JLabel("Phone Number:", SwingConstants.CENTER);
        phone.setFont(new Font("Times New Roman", Font.BOLD, 28));
        phone.setBounds(25, 410, 350, 30);
        phone.setForeground(new Color(0xE30000));
        add(phone);

        phoneText = new JTextField();
        phoneText.setVisible(true);
        phoneText.setBounds(300, 410, 230, 30);
        phoneText.setFont(new Font("Times New Roman", Font.BOLD, 14));
        phoneText.setForeground(new Color(0xE30000));
        add(phoneText);

        JLabel address = new JLabel("Address:", SwingConstants.CENTER);
        address.setFont(new Font("Times New Roman", Font.BOLD, 28));
        address.setBounds(105, 410, 250, 30);
        address.setForeground(new Color(0xE30000));
        add(address);

        addressText = new JTextField();
        addressText.setVisible(true);
        addressText.setBounds(300, 410, 230, 30);
        addressText.setFont(new Font("Times New Roman", Font.BOLD, 14));
        addressText.setForeground(new Color(0xE30000));
        add(addressText);


        address.setBounds(120, 480, 250, 30);
        address.setForeground(new Color(0xE30000));
        add(address);

        addressText = new JTextField();
        addressText.setVisible(true);
        addressText.setBounds(300, 480, 230, 30);
        addressText.setFont(new Font("Times New Roman", Font.BOLD, 14));
        addressText.setForeground(new Color(0xE30000));
        add(addressText);

        register = new JButton("Register");
        register.setBounds(300, 550, 100, 30);
        register.setBackground(new Color(0xE30000));
        register.setForeground(new Color(0xFFFFFF));
        register.addActionListener(this);
        add(register);

        cancel = new JButton("Cancel");
        cancel.setBounds(420, 550, 100, 30);
        cancel.setBackground(new Color(0xE30000));
        cancel.setForeground(new Color(0xFFFFFF));
        cancel.addActionListener(this);
        add(cancel);

        this.setResizable(false);
        this.setSize(800, 760);
        this.setVisible(true);
        this.getContentPane().setBackground(Color.blue);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == register) {
            String email = emailText.getText();
            String password = passwordText.getText();
            String confirmPassword = confirmPasswordText.getText();
            String phone = phoneText.getText();
            String address = addressText.getText();

            if (password.equals(confirmPassword)) {
                //TODO: Store user information in database
                JOptionPane.showMessageDialog(this, "Registration Successful");
                RegisterHelper dbHelper = new RegisterHelper();
                try {
                    dbHelper.connect("jdbc:mysql://localhost:3306/northernbus", "root", "Sebires123");
                    dbHelper.insertRegistration(email, password, address,phone,1000);
                    dbHelper.disconnect();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Login login1 = new Login();
                login1.setVisible(true);
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Passwords do not match");
            }
        } else if (ae.getSource() == cancel) {
            Login login1 = new Login();
            login1.setVisible(true);
            this.setVisible(false);
        }
    }

}

