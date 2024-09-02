import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;


public class Login {
   public boolean isAdmin;
   
   public void back_button(ActionEvent event) {
      try {
         Runner.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
         Runner.display("Home");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void main(String[] args) {
       
   }
}
