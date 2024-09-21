import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserProfile {
   
}


class User {
   private int id;
   public String username;
   private String password;
   public float balance;
   public int new_suggestions;
   public List<Product> suggestions, cart;
   public boolean isAdmin;
   
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
         isAdmin = isAdmin(username);

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
      DB.execQuery("INSERT INTO USERS VALUES (NULL, \'" + username + "\', \'" + password + "\', 0, 0)");
      // DB.execQuery("COMMIT;");
   }

   private static boolean isAdmin(String name) {
      List<String> lines = null;
      try {
         lines = Files.readAllLines(Paths.get("Admins.txt"));
      } catch (IOException e) {
         System.out.println("IOException in isAdmin method");
      }

      for (String line : lines) {
         if(name.compareTo(line) == 0)
            return true;
      }
      return false;
   }
}

