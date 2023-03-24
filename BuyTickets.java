import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Properties;
import java.sql.*;


import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;



public class BuyTickets extends JFrame implements ActionListener {

    private JButton searchButton;
    private JButton backButton;
    private JTextField fromField, toField, dateField;
    private JComboBox<String> passengerComboBox;

    private final Color primaryColor = new Color(0x2F2FA2);
    private final Color secondaryColor = new Color(0xFFFFFF);
    private Font customFont;
    private static JDatePickerImpl datePicker;
    private JComboBox<String> hourComboBox;
    private JComboBox<String> minuteComboBox;

    private int loggedInUserId;

    BuyTickets(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
        createBuyPageUI();
    }





    private void createBuyPageUI() {
        this.setTitle("NorthernBus - Buy Tickets");
        setLayout(new BorderLayout());
        customFont = new Font("Arial", Font.BOLD, 18);

        ImageIcon logo = new ImageIcon(ClassLoader.getSystemResource("Images/logo.png"));
        this.setIconImage(logo.getImage());


        Image logo1 = logo.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        ImageIcon icon2 = new ImageIcon(logo1);


        JLabel logoLabel = new JLabel(icon2, SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(JLabel.TOP);
        add(logoLabel, BorderLayout.NORTH);


        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);


        formPanel.setBackground(primaryColor);
        add(formPanel, BorderLayout.CENTER);



        JLabel fromLabel = new JLabel("From: ", SwingConstants.RIGHT);
        fromLabel.setVerticalAlignment(JLabel.CENTER);
        fromLabel.setFont(customFont);
        fromLabel.setForeground(secondaryColor);
        formPanel.setBackground(primaryColor);
        formPanel.add(fromLabel, gbc);



        fromField = new JTextField();
        fromField.setFont(customFont);
        gbc.gridx = 1;
        formPanel.add(fromField, gbc);



        JLabel toLabel = new JLabel("To: ", SwingConstants.RIGHT);
        toLabel.setVerticalAlignment(JLabel.CENTER);
        toLabel.setFont(customFont);
        toLabel.setForeground(secondaryColor);
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(toLabel, gbc);



        toField = new JTextField();
        toField.setFont(customFont);
        gbc.gridx = 1;
        formPanel.add(toField, gbc);



        JLabel dateLabel = new JLabel("Date: ", SwingConstants.RIGHT);
        dateLabel.setFont(customFont);
        dateLabel.setForeground(secondaryColor);
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(dateLabel, gbc);



        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
        this.datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.getJFormattedTextField().setFont(customFont);
        datePicker.getJFormattedTextField().setForeground(secondaryColor);
        gbc.gridx = 1;
        formPanel.add(datePicker, gbc);



        JLabel timeLabel = new JLabel("Time: ", SwingConstants.RIGHT);
        timeLabel.setFont(customFont);
        timeLabel.setForeground(secondaryColor);
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(timeLabel, gbc);

        String[] hourOptions = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        hourComboBox = new JComboBox<>(hourOptions);
        hourComboBox.setFont(customFont);
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        formPanel.add(hourComboBox, gbc);

        JLabel colonLabel = new JLabel(":", SwingConstants.CENTER);
        colonLabel.setFont(customFont);
        colonLabel.setForeground(secondaryColor);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        formPanel.add(colonLabel, gbc);

        String[] minuteOptions = {"00", "15", "30", "45"};
        minuteComboBox = new JComboBox<>(minuteOptions);
        minuteComboBox.setFont(customFont);
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        formPanel.add(minuteComboBox, gbc);



        JLabel passengerLabel = new JLabel("Passengers: ", SwingConstants.RIGHT);
        passengerLabel.setVerticalAlignment(JLabel.CENTER);
        passengerLabel.setFont(customFont);
        passengerLabel.setForeground(secondaryColor);
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(passengerLabel, gbc);


        String[] passengerOptions = {"1", "2", "3", "4", "5"};
        passengerComboBox = new JComboBox<String>(passengerOptions);
        passengerComboBox.setFont(customFont);
        gbc.gridx = 1;
        formPanel.add(passengerComboBox, gbc);

        formPanel.setBackground(primaryColor);
        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.setBackground(primaryColor);

        backButton = new JButton("Back");
        backButton.setBackground(new Color(0xE30000));
        backButton.setForeground(new Color(0xFFFFFF));
        backButton.addActionListener(this); // Add this line
        backButton.setPreferredSize(new Dimension(backButton.getWidth(), 50));
        backButton.setBackground(secondaryColor);
        backButton.setFont(customFont);
        backButton.setForeground(primaryColor);
        buttonPanel.add(backButton);

        searchButton = new JButton("Search");
        searchButton.setBackground(new Color(0xE30000));
        searchButton.setForeground(new Color(0xFFFFFF));
        searchButton.addActionListener(this); // Add this line
        searchButton.setPreferredSize(new Dimension(searchButton.getWidth(), 50));
        searchButton.setBackground(secondaryColor);
        searchButton.setForeground(primaryColor);
        searchButton.setFont(customFont);
        buttonPanel.add(searchButton);

        add(buttonPanel, BorderLayout.SOUTH);

        this.setResizable(false);
        this.setSize(800, 600);
        this.setVisible(true);
        this.getContentPane().setBackground(primaryColor);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    public static void createBookingTable(String from, String to, LocalDate date, LocalTime time, int numPassengers) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        Statement insertStmt = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/northernbus", "root", "Sebires123");

            String tableName = "booking";
            stmt = conn.createStatement();
            String sql = "CREATE TABLE " + tableName + " (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "departure_location VARCHAR(255) NOT NULL," +
                    "arrival_location VARCHAR(255) NOT NULL," +
                    "departure_date DATE NOT NULL," +
                    "departure_time TIME NOT NULL," +
                    "num_passengers INT NOT NULL," +
                    "total_price DECIMAL(10,2) NOT NULL" +
                    ")";
            stmt.executeUpdate(sql);

            java.sql.Date sqlDate = java.sql.Date.valueOf(date);
            java.sql.Time sqlTime = java.sql.Time.valueOf(time);
            Time startTime = Time.valueOf(time.minusHours(1));
            Time endTime = Time.valueOf(time.plusHours(1));
            sql = "SELECT * FROM bus_service " +
                    "WHERE departure_location = '" + from + "' " +
                    "AND arrival_location = '" + to + "' " +
                    "AND departure_date = '" + sqlDate + "' " +
                    "AND departure_time BETWEEN '" + startTime + "' AND '" + endTime + "'";
            ResultSet rs = stmt.executeQuery(sql);

            double totalPrice = 0.0;
            insertStmt = conn.createStatement();
            while (rs.next()) {
                System.out.println("Fetched row: " + rs.getString("departure_location") + ", " + rs.getString("arrival_location") + ", " + rs.getDate("departure_date") + ", " + rs.getTime("departure_time") + ", " + rs.getDouble("price")); // Display fetched rows
                double price = rs.getDouble("price");
                totalPrice += price * numPassengers;
                sql = "INSERT INTO " + tableName + " " +
                        "(departure_location, arrival_location, departure_date, departure_time, num_passengers, total_price) " +
                        "VALUES " +
                        "('" + from + "', '" + to + "', '" + sqlDate + "', '" + rs.getTime("departure_time") + "', " + numPassengers + ", " + price * numPassengers + ")";
                insertStmt.executeUpdate(sql);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) stmt.close();
            if (insertStmt != null) insertStmt.close();
            if (conn != null) conn.close();
        }
    }


    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == backButton) {
            HomePage home = new HomePage(loggedInUserId);
            home.setVisible(true);
            this.setVisible(false);
        } else if (ae.getSource() == searchButton) {
            String from = fromField.getText();
            String to = toField.getText();
            java.util.Date utilDate = (java.util.Date) datePicker.getModel().getValue();
            LocalDate localDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String hour = (String) hourComboBox.getSelectedItem();
            String minute = (String) minuteComboBox.getSelectedItem();
            String timeString = hour + ":" + minute + ":00";
            LocalTime localTime = LocalTime.parse(timeString);
            int numPassengers = Integer.parseInt((String) passengerComboBox.getSelectedItem());

            try {
                createBookingTable(from, to, localDate, localTime, numPassengers);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            Search searchpage = new Search(loggedInUserId);
            searchpage.setVisible(true);
            this.setVisible(false);
        }
    }

}

