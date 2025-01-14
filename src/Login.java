import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class Login {
   public static boolean haveAccount = true;
   public static boolean fromAdmin = false;

   @FXML
   private AnchorPane rootPane;
   @FXML
   private Button submit, cna;
   @FXML
   private Button cna1; // Sign in ?
   @FXML
   private TextField username, password1, password2;
   @FXML
   private Text confirm;

   @FXML
   public void initialize() {
      if (fromAdmin) {
         submit.setText("Create");
         submit.setLayoutY(submit.getLayoutY() + 90);

         confirm.setVisible(true);
         cna.setVisible(false);
         cna1.setVisible(false);
         password2.setVisible(true);
      }
   }

   @FXML
   void create_new_account(ActionEvent event) {
      if (haveAccount) {
         haveAccount = false;
         submit.setText("Create");
         submit.setLayoutY(submit.getLayoutY() + 90);

         confirm.setVisible(true);
         cna.setVisible(false);
         cna1.setVisible(true);
         password2.setVisible(true);
      } else {
         haveAccount = true;
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
         if (exist) {
            savedPassword = res.getString("PASSWORD");
         }
      } catch (SQLException e) {
         System.out.println("SQL EXCEPTION in submit_button method");
      }

      if (haveAccount) {
         if (!exist) {
            Runner.showAlert("Invalid Username", "Username is not found\nEnter Valid Username");
            return;
         }

         if (!savedPassword.equals(password1.getText())) {
            Runner.showAlert("Invalid Password", "Password provided didn't match your username");
            return;
         }

      } else {

         if (exist) {
            System.out.println("Username is used before");
            Runner.showAlert("Used username", "This username is used before\nEnter another username");
            return;
         }
         if (!password1.getText().equals(password2.getText())) {
            System.out.println("password1 != password2");
            Runner.showAlert("Passwords don't match",
                  "Passwords don't match\nPlease confirm password correctly");
            return;
         }

         User.insertUser(name, password1.getText());
      }

      if (fromAdmin) {
         if (User.isAdmin(name)) {
            Runner.showAlert("Used Name", "Name is used before\nEnter another username");
            return;
         }
         try {
            FileWriter writer = new FileWriter("Admins.txt", true);

            writer.write(name + "\n");
            writer.close();

            System.out.println(name + " added successfully");
         } catch (IOException e) {
            System.out.println("An error occurred in adding admin");
            // e.printStackTrace();
         }
      } else {
         Runner.setUser(name);
      }
      Runner.display("Home");
   }

   public static void main(String[] args) {

   }
}
