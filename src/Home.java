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

   private static ListView<CategoryFrame> mycategories = new ListView<>();

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
         categories.getItems().clear();
         categories.getItems().addAll(mycategories.getItems());
         categories.setCellFactory(param -> {
            CategoryFrame categoryCell = new CategoryFrame();
            categoryCell.autosize();
            categoryCell.getStyleClass().add("category-cell");
            return categoryCell;
         });
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
      mycategories.getItems().addAll(categories.getItems());
      loaded = true;
   }

   @FXML
   void scroll_load(ScrollEvent event) {
      if (categoryIndex >= categoryNames.length)   return;
      System.out.println(categoryNames[categoryIndex]);
      
      CategoryFrame cat_frame = new CategoryFrame(categoryNames[categoryIndex++]);
      if(!empty_category) {
         categories.getItems().add(cat_frame);
         mycategories.getItems().add(cat_frame);
      } else {
         empty_category = false;
      }

   }

}
