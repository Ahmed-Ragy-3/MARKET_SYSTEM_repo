import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
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
            System.out.println("SQL Exception in execQuery method in DB Class");
            System.out.println(e);
            //e.printStackTrace();
        }
        return null;
    }

    public static boolean emptyQuery(ResultSet resultSet) {
        try {
            return !resultSet.next();
        } catch (SQLException e) {
            System.out.println("In emptyQuery");
            System.out.println(e);
            // e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        // String str = "elknew" + nullfunc();
        // System.out.println(str);
    }
}
