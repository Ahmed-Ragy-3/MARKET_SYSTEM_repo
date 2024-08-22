import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBIntegration {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:XE";
        // String username = "MARKET";
        // String password = "marketdb";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, "MARKET", "marketdb");
            Statement statement = connection.createStatement();

            // Example query
            //String query = "SELECT * FROM your_table_name";
            ResultSet resultSet = statement.executeQuery("SELECT * FROM your_table_name");

            while (resultSet.next()) {
                // Assuming your table has columns 'id' and 'name'
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.println("ID: " + id + ", Name: " + name);
            }

            // Close the connections
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
