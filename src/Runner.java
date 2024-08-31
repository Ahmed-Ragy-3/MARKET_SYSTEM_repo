import java.awt.event.ActionEvent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;


public class Runner extends Application {
   public boolean isAdmin;
   public boolean registered;

   private Stage stage;
   private Scene scene;
   private Parent root;
   //private String filterby;
   
   // public void login_scene(ActionEvent event) {
   //    root = FXMLLoader.load(getClass().getResource("Login.fxml"));
   //    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
   //    scene = new Scene(root);
   //    stage.setTitle("Login");
   //    stage.setScene(scene);
   //    stage.show();
   // }
   
   @Override
   public void start(Stage primaryStage) throws Exception {
      try {          
         root = FXMLLoader.load(getClass().getResource("Home.fxml"));
         //System.out.println("here");
         scene = new Scene(root);
         stage = primaryStage;
         stage.setTitle("Home");
         stage.setScene(scene);
         stage.show();
      } catch (Exception e) {
         //e.printStackTrace();
      }
   }
   
   public static void main(String[] args) {
      //System.out.println("11");
      launch(args);
   }
}