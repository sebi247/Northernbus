import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MyTickets extends JFrame implements ActionListener {

    private JButton backButton;
    private int loggedInUserId;
    private JTable ticketsTable;
    private JScrollPane scrollPane;

    public MyTickets(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
        createMyTicketsPageUI();
    }

    private void createMyTicketsPageUI() {
        setTitle("My Tickets");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLayout(new BorderLayout());

        String[] columnNames = {"Departure Location", "Arrival Location", "Departure Date", "Departure Time", "Num Passengers", "Total Price"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        ticketsTable = new JTable(tableModel);
        scrollPane = new JScrollPane(ticketsTable);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 1));

        backButton = new JButton("Back");
        backButton.setBackground(new Color(0xE30000));
        backButton.setForeground(new Color(0xFFFFFF));
        backButton.addActionListener(this);
        backButton.setPreferredSize(new Dimension(backButton.getWidth(), 50));
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);

        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving ticket data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/northernbus";
        String user = "root";
        String password = "Sebires123";
        String query = "SELECT departure_location, arrival_location, departure_date, departure_time, num_passengers, total_price FROM user_tickets_" + loggedInUserId;

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            DefaultTableModel tableModel = (DefaultTableModel) ticketsTable.getModel();

            while (resultSet.next()) {
                String departureLocation = resultSet.getString("departure_location");
                String arrivalLocation = resultSet.getString("arrival_location");
                Date departureDate = resultSet.getDate("departure_date");
                Time departureTime = resultSet.getTime("departure_time");
                int numPassengers = resultSet.getInt("num_passengers");
                double totalPrice = resultSet.getDouble("total_price");

                Object[] rowData = {departureLocation, arrivalLocation, departureDate, departureTime, numPassengers, totalPrice};
                tableModel.addRow(rowData);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == backButton) {
            HomePage homepage = new HomePage(loggedInUserId);
            homepage.setVisible(true);
            this.setVisible(false);
        }
    }
}
