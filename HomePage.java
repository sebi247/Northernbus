import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class HomePage extends JFrame implements ActionListener {

    private JButton searchButton, MyTickets, buyButton, accountButton;

    private Search searchInstance;
    private MyTickets ticketsInstance;
    private int loggedInUserId;



    HomePage(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
        createHomePageUI();
    }

    private void createHomePageUI() {
        this.setTitle("NorthernBus");
        setLayout(new BorderLayout());

        ImageIcon logo = new ImageIcon(ClassLoader.getSystemResource("Images/logo.png"));
        this.setIconImage(logo.getImage());

        Image logo1 = logo.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        ImageIcon icon2 = new ImageIcon(logo1);

        JLabel logoLabel = new JLabel(icon2, SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(JLabel.TOP);
        add(logoLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));

        MyTickets = new JButton("My Tickets");
        MyTickets.setBackground(new Color(0xE30000));
        MyTickets.setForeground(new Color(0xFFFFFF));
        MyTickets.addActionListener(this);
        MyTickets.setPreferredSize(new Dimension(MyTickets.getWidth(), 50));
        buttonPanel.add(MyTickets);

        buyButton = new JButton("Buy");
        buyButton.setBackground(new Color(0xE30000));
        buyButton.setForeground(new Color(0xFFFFFF));
        buyButton.addActionListener(this);
        buyButton.setPreferredSize(new Dimension(buyButton.getWidth(), 50));
        buttonPanel.add(buyButton);

        accountButton = new JButton("Account");
        accountButton.setBackground(new Color(0xE30000));
        accountButton.setForeground(new Color(0xFFFFFF));
        accountButton.addActionListener(this);
        accountButton.setPreferredSize(new Dimension(accountButton.getWidth(), 50));
        buttonPanel.add(accountButton);

        add(buttonPanel, BorderLayout.SOUTH);

        this.setResizable(false);
        this.setSize(800, 600);
        this.setVisible(true);
        this.getContentPane().setBackground(Color.blue);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == MyTickets) {
            MyTickets tickets = new MyTickets(loggedInUserId);
            tickets.setVisible(true);
            this.setVisible(false);
        } else if (ae.getSource() == buyButton) {
            BuyTickets buy = new BuyTickets(loggedInUserId);
            buy.setVisible(true);
            this.setVisible(false);
        } else if (ae.getSource() == accountButton) {
            Account account = new Account(loggedInUserId);
            account.setVisible(true);
            this.setVisible(false);
        }
    }


}
