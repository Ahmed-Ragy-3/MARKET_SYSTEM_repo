//import VIP.javafx-sdk-22.0.2.javafx;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class Runner extends Application {
   public static boolean registered;
   public static final String SHOPNAME = "OnShopping";

   public static Stage stage;
   public static Scene scene;
   public static Parent root;

   public void login_button(ActionEvent event) {
      try {
         stage = (Stage)((Node)event.getSource()).getScene().getWindow();
         display("Login");
      } catch (Exception e) {
         System.out.println("print Stack trace 1");
         e.printStackTrace();
      }
   }

   // Static method to display a specific screen
   public static void display(String screen) {
      try {
         root = FXMLLoader.load(Runner.class.getResource(screen + ".fxml"));
         scene = new Scene(root);
         stage.setTitle(SHOPNAME + " - " + screen);
         stage.setScene(scene);
         stage.setFullScreen(true);
         stage.show();
      } catch (Exception e) {
         System.out.println("print Stack trace 2");
         e.printStackTrace();
      }
   }

   @Override
   public void start(Stage primaryStage) throws Exception {
      try {
         stage = primaryStage;
         display("Home"); // Display the initial "Home" screen
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void main(String[] args) {
      launch(args);
   }
}
