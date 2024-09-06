import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class Runner extends Application {
   public static boolean registered;
   public static boolean isAdmin;
   public static final String SHOPNAME = "OnShopping";

   public static Stage stage;
   public static Parent root;

   public void login_button(ActionEvent event) {
      display("Login");
   }

   public static void display(String screen) {
      try {
         root = FXMLLoader.load(Runner.class.getResource(screen + ".fxml"));
         stage.setScene(new Scene(root));
         stage.setTitle(SHOPNAME + " - " + screen);
         stage.setFullScreen(true);
         stage.show();
      } catch (Exception e) {
         System.out.println("Error when displaying " + screen);
         e.printStackTrace();
      }
   }

   @Override
   public void start(Stage primaryStage) throws Exception {
      stage = primaryStage;
      display("Home");  // Starts with the Login screen
   }

   public static void main(String[] args) {
      launch(args);
   }
}
