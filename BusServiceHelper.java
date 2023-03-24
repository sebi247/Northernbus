import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BusServiceHelper {
    private static Connection conn;

    public BusServiceHelper(String url, String username, String password) throws SQLException {
        conn = DriverManager.getConnection(url, username, password);
    }

    public static List<BusService> getServices(String startLocation, String endLocation, Timestamp start, Timestamp end) throws SQLException {
        String query = "SELECT * FROM bus_service WHERE departure_location = ? AND arrival_location = ? AND departure_time BETWEEN ? AND ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, startLocation);
        statement.setString(2, endLocation);
        statement.setTimestamp(3, start);
        statement.setTimestamp(4, end);
        ResultSet rs = statement.executeQuery();

        List<BusService> services = new ArrayList<>();
        while (rs.next()) {
            BusService service = new BusService(rs.getString("departure_location"), rs.getString("arrival_location"), rs.getTimestamp("departure_time"), rs.getTimestamp("arrival_time"), rs.getBigDecimal("price"));
            services.add(service);
        }

        return services;
    }
}
