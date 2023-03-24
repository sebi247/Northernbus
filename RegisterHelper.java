import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterHelper {
    // other helper methods and fields

    private Connection conn;

    // connect to the database
    public void connect(String url, String username, String password) throws SQLException {
        conn = DriverManager.getConnection(url, username, password);
    }

    // disconnect from the database
    public void disconnect() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    // insert a new registration into the database
    public void insertRegistration(String email, String password, String address, String phone, int funds) throws SQLException {
        String insertUserQuery = "INSERT INTO users (email, password, address, phone_number, funds) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement insertUserStatement = conn.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertUserStatement.setString(1, email);
            insertUserStatement.setString(2, password);
            insertUserStatement.setString(3, address);
            insertUserStatement.setString(4, phone);
            insertUserStatement.setInt(5, funds);

            insertUserStatement.executeUpdate();

            // Get the newly created user's ID
            try (ResultSet generatedKeys = insertUserStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    // Create a new table for the user
                    String createUserTicketsTableQuery = "CREATE TABLE user_tickets_" + userId + " ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "departure_location VARCHAR(255), "
                            + "arrival_location VARCHAR(255), "
                            + "departure_date DATE, "
                            + "departure_time TIME, "
                            + "num_passengers INT, "
                            + "total_price DOUBLE)";

                    try (Statement createUserTicketsTableStatement = conn.createStatement()) {
                        createUserTicketsTableStatement.executeUpdate(createUserTicketsTableQuery);
                    }
                } else {
                    throw new SQLException("Failed to get the user ID. User not created.");
                }
            }
        }
    }
}
