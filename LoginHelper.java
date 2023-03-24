import java.sql.*;

public class LoginHelper {

    private Connection conn;

    public LoginHelper(String url, String username, String password) throws SQLException {
        conn = DriverManager.getConnection(url, username, password);
    }

    public boolean checkLogin(String email, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, email);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        boolean loginSuccessful = rs.next();
        pstmt.close();
        return loginSuccessful;
    }

    public int getUserId(String email, String password) throws SQLException {
        String query = "SELECT ID FROM users WHERE EMAIL = ? AND PASSWORD = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, email);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("ID");
        } else {
            return -1;
        }
    }

    public void close() throws SQLException {
        conn.close();
    }
}
