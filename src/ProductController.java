import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ProductController implements Initializable {
   public static String status;
      
   @FXML
   private Button statusButton;

   private static String nullIfEmpty(String arg) {
      return (arg.compareTo("") == 0) ? "NULL" : arg;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      statusButton.setText(status);
   }
}
