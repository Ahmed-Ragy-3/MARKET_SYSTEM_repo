import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


public class UserProfile implements Initializable {

   @FXML
   private Label username;
   @FXML
   private Label balance;
   @FXML
   private Label admin;

   @FXML
   private ListView<Frame> cartList;
   @FXML
   private ListView<Frame> suggestions;

   private User user = Runner.user;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      if(user == null) return;
      username.setText(username.getText() + user.getUsername());
      balance.setText(balance.getText() + user.balance);
      admin.setText(admin.getText() + ((user.isAdmin) ? " ✓" : " ✕"));

      if(user.getCart() != null) {
         for(int prod : user.getCart()) {
            cartList.getItems().add(new Frame(DB.execQuery("SELECT * FROM PRODUCTS WHERE PRODUCT_ID = " + prod)));
         }
      }
   }
   
}

class Suggestion {
   private int id;
   private String sender;

   public Suggestion(int id, String sender) {
      this.id = id;
      this.sender = sender;
   }

   public int getId() {
      return id;
   }
   public void setId(int id) {
      this.id = id;
   }
   public String getSender() {
      return sender;
   }
   public void setSender(String sender) {
      this.sender = sender;
   }
}

class User {
   private int id;
   private String username;
   private String password;
   public float balance;
   private int unSeenSuggestions = 0;
   private List<Integer> cart;
   private List<Suggestion> suggestions;
   public boolean isAdmin;
   
   public User() {}

   public User(String username) {
      this(DB.execQuery("SELECT * FROM USERS WHERE USERNAME = '" + username + "'"));
   }
   
   public User(ResultSet res) {
      if(DB.emptyQuery(res))  return;

      try {
         res.next();
         setId(res.getInt("USER_ID"));
         username = res.getString("USERNAME"); 
         setPassword(res.getString("PASSWORD"));
         balance = res.getFloat("BALANCE");

         java.sql.Array array = res.getArray("CART");
         if(array == null) {
            cart = null;
         }else {
            cart = Arrays.asList((Integer[]) array.getArray());
         }
         
         ResultSet res2 = DB.execQuery("SELECT PRODUCT_ID, SENDER_ID, IS_SEEN FROM SUGGESTIONS WHERE RECIEVER_ID = " + this.id);
         while (res2.next()) {
            suggestions.add(new Suggestion(res2.getInt(1), res2.getString(2)));
            if(res2.getInt(3) == 0) {
               unSeenSuggestions++;
            }
         }
         isAdmin = isAdmin(username);

      } catch (SQLException e) {
         System.out.println("SQL EXCEPTION in User constructer");
         System.out.println(e);
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
      DB.execQuery("INSERT INTO USERS VALUES (USERS_SEQ.NEXTVAL, \'" + username + "\', \'" + password + "\', 0, num_arr())");
      // DB.execQuery("COMMIT;");
   }

   public static boolean isAdmin(String name) {
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

   private void addToCart(int prod_id) {
      DB.execQuery("""
         UPDATE USERS
         SET CART = CART || num_arr(%d)
         WHERE USER_ID = %d
         AND %d NOT MEMBER OF CART;
      """.formatted(prod_id, id, prod_id));
   }
   private void deleteFromCart(int prod_id) {
      DB.execQuery("""
         UPDATE USERS
         SET CART = (
            SELECT CAST(COLLECT(num) AS num_arr)
            FROM (SELECT num FROM TABLE(CART) WHERE num != %d)
         )
         WHERE USER_ID = %d;
      """.formatted(prod_id, id));
   }

   public int getId() {
      return id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public float getBalance() {
      return balance;
   }

   public void setBalance(float balance) {
      this.balance = balance;
   }

   public int getUnSeenSuggestions() {
      return unSeenSuggestions;
   }

   public void setUnSeenSuggestions(int unSeenSuggestions) {
      this.unSeenSuggestions = unSeenSuggestions;
   }

   public List<Integer> getCart() {
      return cart;
   }

   public void setCart(List<Integer> cart) {
      this.cart = cart;
   }

   public List<Suggestion> getSuggestions() {
      return suggestions;
   }

   public void setSuggestions(List<Suggestion> suggestions) {
      this.suggestions = suggestions;
   }

   public boolean isAdmin() {
      return isAdmin;
   }

   public void setAdmin(boolean isAdmin) {
      this.isAdmin = isAdmin;
   }

}

