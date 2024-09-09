import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class User {
   private int id;
   public String username;
   private String password;
   public float balance;
   public List<Product> suggestions, cart;
   
   public User() {}
   
   public User(String name, String password) {
      this.username = name;
      this.password = password;
   }

   public void setId(int id) {
      this.id = id;
   }
   
   public String getPassword() {
      return password;
   }
   public void setPassword(String password) {
      this.password = password;
   }

   public static User getUser(String username) {
      User user = new User();
      ResultSet res = DB.execQuery("SELECT * FROM USERS WHERE USERNAME = " + username);
      if(res == null)  return null;
      
      try {
         user.setId(res.getInt("USER_ID")); 
         user.username = username; 
         user.setPassword(res.getString("PASSWORD"));
         user.balance = res.getFloat("BALANCE");
         
         ResultSet res2 = DB.execQuery("SELECT * FROM SUGGESTIONS WHERE RECIEVER_ID = " + user.id);
         while (res2.next()) {
            //user.suggestions.add(getProduct(res2.getInt("PRODUCT_ID")));
         }

      } catch (SQLException e) {
         System.out.println("SQL EXCEPTION in getUser method");
         //e.printStackTrace();
      }

      return user;
   }

   public static void insertUser(String username, String password) {
      System.out.println("INSERT INTO USERS VALUES (NULL, \'" + username + "\', \'" + password + "\', 0, 0);");
      DB.execQuery("INSERT INTO USERS VALUES (NULL, \'" + username + "\', \'" + password + "\', 0, 0);");
      DB.execQuery("COMMIT;");
   }
}

