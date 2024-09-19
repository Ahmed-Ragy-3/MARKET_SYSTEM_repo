import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.ScrollEvent;

public class Home implements Initializable {
   public static byte categoryIndex;
   private static boolean loaded = false;
   
   @FXML
   private ListView<CategoryFrame> categories;

   public static final String[] categoryNames = {
      "Arts & Crafts", "Automotive",
      "Baby & Kids", "Beauty & Personal Care",
      "Clothing & Accessories", "Electronics & Gadgets",
      "Food & Beverages", "Health & Wellness",
      "Home & Kitchen", "Household Supplies",
      "Industrial & Tools", "Office & Events",
      "Outdoors & Garden", "Pet Supplies", "Toys & Games", "Others"
   };
   public static boolean empty_category = false;
   
   @Override
   public void initialize(URL location, ResourceBundle resources) {
      if(loaded) {
         return;
      }
      System.out.println("Initialize Home");
      categories.setCellFactory(param -> {
         CategoryFrame categoryCell = new CategoryFrame();
         categoryCell.autosize();
         categoryCell.getStyleClass().add("category-cell");
         return categoryCell;
      });

      for (categoryIndex = 0; categoryIndex < 3; categoryIndex++) {
         System.out.println(categoryNames[categoryIndex]);
         CategoryFrame cat_frame = new CategoryFrame(categoryNames[categoryIndex]);
         
         if(!empty_category) {
            categories.getItems().add(cat_frame);
         } else {
            empty_category = false;
         }
      }
      loaded = true;
   }

   @FXML
   void scroll_load(ScrollEvent event) {
      if (categoryIndex >= categoryNames.length)   return;
      System.out.println(categoryNames[categoryIndex]);
      categories.getItems().add(new CategoryFrame(categoryNames[categoryIndex++]));
   }

   // ObservableList<Frame> frames = FXCollections.observableArrayList(
   // new Frame("https://example.com/image1.png", "Product 1", "$69.99",
   // "Description 1"),
   // new Frame("https://example.com/image2.png", "Product 2", "$89.99",
   // "Description 2"),
   // new Frame("https://example.com/image3.png", "Product 3", "$29.99",
   // "Description 3")
   // );

   // ListView<Frame> listView = new ListView<>(frames);
   // listView.setCellFactory(param -> new Frame());
}
