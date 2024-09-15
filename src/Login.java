import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class Login {
   private boolean haveAccount = true;

   @FXML
   private AnchorPane rootPane;
   @FXML
   private Button submit, cna;
   @FXML
   private Button cna1;  // Sign in ?
   @FXML
   private TextField username, password1, password2;
   @FXML
   private Text confirm;

   @FXML
   void create_new_account(ActionEvent event) {
      if(this.haveAccount) {
         this.haveAccount = false;
         submit.setText("Create");
         submit.setLayoutY(submit.getLayoutY() + 90);
   
         confirm.setVisible(true);
         cna.setVisible(false);
         cna1.setVisible(true);
         password2.setVisible(true);
      }else {
         this.haveAccount = true;
         submit.setText("Sign in");
         submit.setLayoutY(submit.getLayoutY() - 90);
   
         confirm.setVisible(false);
         cna1.setVisible(true);
         cna.setVisible(false);
         password2.setVisible(false);
      }
   }

   @FXML
   void submit_button(ActionEvent event) {
      String name = this.username.getText();
      ResultSet res = DB.execQuery("SELECT PASSWORD FROM USERS where USERNAME = \'" + name + "\'");
      boolean exist = true;
      String savedPassword = "";
      try {
         exist = res.next();
         if(exist) {
            savedPassword = res.getString("PASSWORD");
         }
      } catch (SQLException e) {
         System.out.println("SQL EXCEPTION in submit_button method");
      }

      if(this.haveAccount) {
         if(!exist) {
            Runner.showAlert("Invalid Username", "Username is not found\nEnter Valid Username");
            return;
         }

         if(!savedPassword.equals(password1.getText())) {
            Runner.showAlert("Invalid Password", "Password provided didn't match your username");
            return;
         }

      }else {

         if(exist) {
            System.out.println("Username is used before");
            Runner.showAlert("Used username", "This username is used before\nEnter another username");
            return;
         }
         if(!password1.getText().equals(password2.getText())) {
            System.out.println("password1 != password2");
            Runner.showAlert("Passwords don't match", 
            "Passwords don't match\nPlease confirm password correctly");
            return;
         }

         User.insertUser(name, password1.getText());
      }

      Runner.user = new User(name);
      Runner.display("Home");
   }

   public static void main(String[] args) {
      
   }
}
