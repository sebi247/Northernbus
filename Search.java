import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class Search extends JFrame implements ActionListener{

    private int loggedInUserId;
    private JTable busInfoTable;
    private JScrollPane scrollPane;

    private JButton backButton;

    Search(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
        createSearchPageUI();
    }



    private void createSearchPageUI() {
        setTitle("Bus Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLayout(new BorderLayout());

        String[] columnNames = {"Departure Location", "Arrival Location", "Departure Date", "Departure Time", "Num Passengers", "Total Price", "Buy"};

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        busInfoTable = new JTable(tableModel);
        scrollPane = new JScrollPane(busInfoTable);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 1));

        System.out.println(loggedInUserId);


        backButton = new JButton("Back");
        backButton.setBackground(new Color(0xE30000));
        backButton.setForeground(new Color(0xFFFFFF));
        backButton.addActionListener(this);
        backButton.setPreferredSize(new Dimension(backButton.getWidth(), 50));
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Set custom renderer and editor for the last column
        busInfoTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        busInfoTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));

        add(scrollPane, BorderLayout.CENTER);

        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving bus data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/northernbus";
        String user = "root";
        String password = "Sebires123";
        String query = "SELECT departure_location, arrival_location, departure_date, departure_time, num_passengers, total_price FROM booking";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            DefaultTableModel tableModel = (DefaultTableModel) busInfoTable.getModel();

            while (resultSet.next()) {
                String departureLocation = resultSet.getString("departure_location");
                String arrivalLocation = resultSet.getString("arrival_location");
                Date departureDate = resultSet.getDate("departure_date");
                Time departureTime = resultSet.getTime("departure_time");
                int numPassengers = resultSet.getInt("num_passengers");
                double totalPrice = resultSet.getDouble("total_price");

                Object[] rowData = {departureLocation, arrivalLocation, departureDate, departureTime, numPassengers, totalPrice, "Buy"};
                tableModel.addRow(rowData);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()== backButton){
            BuyTickets buy = new BuyTickets(loggedInUserId);
            buy.setVisible(true);
            this.setVisible(false);
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());

        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = busInfoTable.getSelectedRow();
                handleBuyButtonClick(row);
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    private void handleBuyButtonClick(int row) {
        // Handle the Buy button click for the selected row
        System.out.println("Buy button clicked for row: " + row);

        // Fetch data from the selected row
        String departureLocation = (String) busInfoTable.getValueAt(row, 0);
        String arrivalLocation = (String) busInfoTable.getValueAt(row, 1);
        Date departureDate = (Date) busInfoTable.getValueAt(row, 2);
        Time departureTime = (Time) busInfoTable.getValueAt(row, 3);
        Integer numPassengers = (Integer) busInfoTable.getValueAt(row, 4);
        Double totalPrice = (Double) busInfoTable.getValueAt(row, 5);

        // Insert the row into the new table
        try {
            saveRowToNewTable(departureLocation, arrivalLocation, departureDate, departureTime, numPassengers, totalPrice);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving row to new table", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Deduct the price from the user's funds and get the updated funds
        int updatedFunds;
        try {
            updatedFunds = deductPriceFromUserFunds(loggedInUserId, totalPrice);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deducting price from user's funds", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Drop the booking table
        try {
            dropBookingTable();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error dropping the booking table", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show the success message and navigate to the homepage
        JOptionPane.showMessageDialog(this, "Ticket purchase successful. Remaining funds: " + updatedFunds, "Success", JOptionPane.INFORMATION_MESSAGE);
        BuyTickets buy = new BuyTickets(loggedInUserId);
        buy.setVisible(true);
        this.setVisible(false);
    }

    private void dropBookingTable() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/northernbus";
        String user = "root";
        String password = "Sebires123";

        String query = "DROP TABLE booking";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.executeUpdate();
        }
    }





    private int deductPriceFromUserFunds(int userId, double price) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/northernbus";
        String user = "root";
        String password = "Sebires123";

        // Check if the price is negative or zero
        if (price <= 0) {
            JOptionPane.showMessageDialog(this, "Invalid price. Please select a valid ticket.", "Error", JOptionPane.ERROR_MESSAGE);
            return userId;
        }

        // Retrieve the user's current funds
        String selectQuery = "SELECT FUNDS FROM users WHERE ID = ?";
        int currentFunds;

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {

            selectStatement.setInt(1, userId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) { // Check if there's a row in the ResultSet
                currentFunds = resultSet.getInt("FUNDS");
            } else {
                // Handle the case when no user is found for the given userId
                JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return userId;
            }
        }

        // Check if the user has enough funds
        if (currentFunds < price) {
            JOptionPane.showMessageDialog(this, "Insufficient funds. Please add more funds to your account.", "Error", JOptionPane.ERROR_MESSAGE);
            return userId;
        }

        // Subtract the price from the user's funds
        int updatedFunds = currentFunds - (int) Math.round(price);

        // Update the user's funds in the `users` table
        String updateQuery = "UPDATE users SET FUNDS = ? WHERE ID = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            updateStatement.setInt(1, updatedFunds);
            updateStatement.setInt(2, loggedInUserId);
            updateStatement.executeUpdate();
        }

        JOptionPane.showMessageDialog(this, "Ticket purchase successful. Remaining funds: " + updatedFunds, "Success", JOptionPane.INFORMATION_MESSAGE);

        return updatedFunds;
    }



    private void saveRowToNewTable(String departureLocation, String arrivalLocation, Date departureDate, Time departureTime, Integer numPassengers, Double totalPrice) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/northernbus";
        String user = "root";
        String password = "Sebires123";

        String query = "INSERT INTO user_tickets_" + (loggedInUserId) + "(departure_location, arrival_location, departure_date, departure_time, num_passengers, total_price) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, departureLocation);
            statement.setString(2, arrivalLocation);
            statement.setDate(3, departureDate);
            statement.setTime(4, departureTime);
            statement.setInt(5, numPassengers);
            statement.setDouble(6, totalPrice);

            statement.executeUpdate();
        }
    }

}
