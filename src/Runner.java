import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class Runner extends Application {
   public static final String SHOPNAME = "OnShopping";
   public static boolean isAdmin;
   public static User user;

   public static Stage stage;
   public static Parent root;

   @FXML
   private Button login, usernameButton;

   private static Runner instance;

   public Runner() {
      instance = this;
   }

   // Singleton pattern: method to get the instance of Runner
   public static Runner getInstance() {
      return instance;
   }

   //@FXML
   public void showName(String name) {
      System.out.println("In show name");
      usernameButton.setText(name);
      usernameButton.setVisible(true);
   }

   public static void display(String screen) {
      try {
         root = FXMLLoader.load(Runner.class.getResource(screen + ".fxml"));
         stage.setScene(new Scene(root));
         stage.setTitle(SHOPNAME + " - " + screen);
         stage.setMaximized(true);
         stage.show();
      } catch (Exception e) {
         System.out.println("Error when displaying " + screen);
         e.printStackTrace();
      }
   }
   
   public void OnShopping(ActionEvent event) {
      display("Home");
   }
   
   public void login_button(ActionEvent event) {
      //login.setVisible(false);
      display("Login");
   }
   
   public void search_button(ActionEvent event) {
      // display the window responsible for multiple items 
      //display("");
   }
   
   public static void showAlert(String title, String content) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle(title);
      alert.setContentText(content);
      alert.showAndWait();
   }
   
   @Override
   public void start(Stage primaryStage) throws Exception {
      stage = primaryStage;
      display("Home");  // Starts with the Login screen
   }
   public static void main(String[] args) {
      user = null;
      launch(args);
   }
}
