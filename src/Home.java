import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class Home implements Initializable {
   public static final String[] categoryNames = {
      "Arts & Crafts", "Automotive",
      "Baby & Kids", "Beauty & Personal Care",
      "Clothing & Accessories", "Electronics & Gadgets",
      "Food & Beverages", "Health & Wellness",
      "Home & Kitchen", "Household Supplies",
      "Industrial & Tools", "Office & Events",
      "Outdoors & Garden", "Pet Supplies", "Toys & Games"
   };
   @FXML
   private ListView<CategoryFrame> categories;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      System.out.println("Initialize Home");
      for(String category : categoryNames) {
         System.out.println(category);
         categories.getItems().add(new CategoryFrame(category));
      }
   }
   
   // ObservableList<Frame> frames = FXCollections.observableArrayList(
      //    new Frame("https://example.com/image1.png", "Product 1", "$69.99", "Description 1"),
      //    new Frame("https://example.com/image2.png", "Product 2", "$89.99", "Description 2"),
      //    new Frame("https://example.com/image3.png", "Product 3", "$29.99", "Description 3")
      // );
      
      // ListView<Frame> listView = new ListView<>(frames);
      // listView.setCellFactory(param -> new Frame());
   }
   
class CategoryFrame {
   private ListView<Frame> topProducts;
   private Label categoryName;
   
   public CategoryFrame() {}
   
   public CategoryFrame(String name) {

      this.categoryName = new Label(name);
      this.topProducts = new ListView<>();
      
      ResultSet res = DB.execQuery("SELECT * FROM HOME_VIEW WHERE CATEGORY = '" + name + "'");
      try {
         // DB.printQuery(res);
         while (res.next()) {
            // System.out.print(name + " ---- ");
            // System.out.println(res.getInt("PRODUCT_ID"));
            topProducts.getItems().add(new Frame(new Product(res)));
         }
      } catch (Exception e) {
         System.out.println("In CategoryFrame Constructer");
         System.out.println(e);
         // e.printStackTrace();
      }
   }
   
}
