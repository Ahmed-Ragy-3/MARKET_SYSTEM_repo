import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.image.Image;

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

    public static void printQuery(ResultSet res) throws SQLException {
        ResultSetMetaData metaData = res.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            System.out.print(metaData.getColumnName(i) + "\t");
        }
        System.out.println("\n-----------------------------------------------");

        while (res.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(res.getString(i) + "\t");
            }
            System.out.println();
        }
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
        Image image = new Image("https://i5.walmartimages.com/seo/Clinique-City-Block-Sheer-SPF-25-Oil-Free-Daily-Face-Protector-for-Dry-and-Oily-Skin-1-4-oz_727fe7d8-9cc4-49b9-92ce-af2ddade48eb.94221f26fad01b19c31b6ef3d96c97d1.jpeg");
        // System.out.println(image);
    }
}
