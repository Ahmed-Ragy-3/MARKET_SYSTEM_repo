import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class User {
   private int id;
   public String username;
   private String password;
   public float balance;
   public int new_suggestions;
   public List<Product> suggestions, cart;
   
   public User() {}

   public User(String username) {
      this(DB.execQuery("SELECT * FROM USERS WHERE USERNAME = '" + username + "'"));
   }
   
   public User(ResultSet res) {
      if(DB.emptyQuery(res))  return;

      try {
         this.setId(res.getInt("USER_ID")); 
         this.username = res.getString("USERNAME"); 
         this.setPassword(res.getString("PASSWORD"));
         this.new_suggestions = res.getInt("NEW_SUGGESTIONS");
         this.balance = res.getFloat("BALANCE");
         
         ResultSet res2 = DB.execQuery("SELECT * FROM SUGGESTIONS WHERE RECIEVER_ID = " + this.id);
         while (res2.next()) {
            Product product = new Product(res2.getInt("PRODUCT_ID"));
            this.suggestions.add(product);
         }

      } catch (SQLException e) {
         System.out.println("SQL EXCEPTION in getUser method");
         //e.printStackTrace();
      }
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


   public static void insertUser(String username, String password) {
      System.out.println("INSERT INTO USERS VALUES (NULL, \'" + username + "\', \'" + password + "\', 0, 0);");
      DB.execQuery("INSERT INTO USERS VALUES (NULL, \'" + username + "\', \'" + password + "\', 0, 0);");
      DB.execQuery("COMMIT;");
   }
}

