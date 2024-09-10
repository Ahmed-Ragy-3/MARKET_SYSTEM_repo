import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class Runner extends Application {
   public static final String SHOPNAME = "OnShopping";
   public static boolean isAdmin;
   public static User user;

   public static Stage stage;
   
   @FXML
   private AnchorPane fxmlContent;  // For Scene Builder
   private static AnchorPane content;  // Main content area
   
   @FXML
   private Button login, usernameButton;
   
   @FXML
   public void initialize() {
      content = fxmlContent;
   }

   // @FXML
   // public void showName(String name) {
   //    usernameButton.setText(name);
   // }

   public static void display(String fxmlScreen) {
      try {
         Parent inroot = FXMLLoader.load(Runner.class.getResource(fxmlScreen + ".fxml"));
         stage.setTitle(SHOPNAME + " - " + fxmlScreen);
         content.getChildren().setAll(inroot);

      } catch (Exception e) {
         System.out.println("Error when displaying " + fxmlScreen);
         System.out.println(e);
         // e.printStackTrace();
      }
   }
   
   public void OnShopping(ActionEvent event) {
      display("Home");
   }
   
   public void login_button(ActionEvent event) {
      login.setVisible(false);
      display("Login");
   }
   
   public void search_button(ActionEvent event) {
      // Implement the functionality to display the appropriate screen
      // display("SearchScreen");
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
      Parent root = FXMLLoader.load(getClass().getResource("Home_bar.fxml"));
      stage.setScene(new Scene(root));
      stage.setMaximized(true);
      stage.show();

      display("Home");
   }

   public static void main(String[] args) {
      user = null;
      launch(args);
   }
}
