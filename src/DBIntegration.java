import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBIntegration {
    public final static String JDBCURL = "jdbc:oracle:thin:@localhost:1521/ORCLPDB";
    public final static String USERNAME = "MARKET";
    public final static String PASSWORD = "marketdb";

    public static ResultSet execQuery(String query) {
        ResultSet resultSet;
        try {
            Connection connection = DriverManager.getConnection(JDBCURL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            return resultSet;
            
        } catch (SQLException e) {
            System.out.println("SQL Exception1");
            //e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) {
        ResultSet resultSet = null;
        try {
            resultSet = execQuery("SELECT * FROM PRODUCTS");

            if (resultSet != null) {
                while (resultSet.next()) {
                    // Assuming your table has columns 'id' and 'name'
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    System.out.println("ID: " + id + ", Name: " + name);
                }
            }else {
                System.out.println("NULL");
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception2");
            //e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.getStatement().getConnection().close();
                    resultSet.getStatement().close();
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println("SQL Exception3");
                    //e.printStackTrace();
                }
            }
        }
    }
}
