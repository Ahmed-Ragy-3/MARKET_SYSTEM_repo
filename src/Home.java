import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;

public class Home /*extends Thread*/ implements Initializable {
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
      
      for (categoryIndex = 0; categoryIndex < 8; categoryIndex++) {
         System.out.println(categoryNames[categoryIndex]);
         CategoryFrame cat_frame = new CategoryFrame(categoryNames[categoryIndex]);
         // this.start();
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

///////////////////////////////////////////////////////////////////////////////

class Frame extends ListCell<Frame> {
   public final static int IMAGE_HEIGHT = 250;
   public final static int HEIGHT = 350;
   public final static int WIDTH = 200;
   
   public int id;
   public ImageView image;
   public Label name, price;
   
   private VBox vbox = new VBox();
   
   public Frame() {}
   
   public Frame(ResultSet res) {
      // System.out.println("start");
      try {
         this.id = res.getInt(1);
         this.name = new Label(res.getString(2));
         
         this.image = Runner.getImageView(id, res.getString(3));
         
         float initial_price = res.getFloat(4);
         float discount = res.getInt(5) / 100;
         initial_price -= (initial_price * discount);
         this.price = new Label(Float.toString(initial_price));
         
      } catch (Exception e) {
         System.out.println("In Frame Constructer in product id: " + this.id);
         System.out.println(e);
      }
      
      image.setFitHeight(IMAGE_HEIGHT);
      image.setFitWidth(WIDTH);
      // image.setPreserveRatio(true);
      image.getStyleClass().add("image-view");
      
      name.autosize();
      name.setMaxWidth(WIDTH);
      name.setMaxHeight(HEIGHT - IMAGE_HEIGHT);
      name.setWrapText(true);
      name.getStyleClass().add("product-name");
      
      price.setText("\nPrice: " + price.getText() + " $");
      price.autosize();
      price.getStyleClass().add("product-name");
      
      vbox = new VBox(image, name, price);  
   }
   
   @Override
   protected void updateItem(Frame frame, boolean empty) {
      // System.out.println("In update item in Frame");
      super.updateItem(frame, empty);
      if (empty || frame == null) {
         setText(null);
         setGraphic(null);
      } else {
         setGraphic(frame.vbox);
      }
   }

}

///////////////////////////////////////////////////////////////////////////////

class CategoryFrame extends ListCell<CategoryFrame> {
   public ListView<Frame> topProducts = new ListView<>();
   public Label categoryName = new Label();
   private VBox vbox = new VBox();
   
   public CategoryFrame() {}

   public CategoryFrame(String name) {
      
      ResultSet res = DB.execQuery("SELECT * FROM HOME_VIEW WHERE CATEGORY = '" + name + "'");
      if(DB.emptyQuery(res)) {
         Home.empty_category = true;
         System.out.println("empty");
         return;
      }

      try {
         byte count = 0;
         while (res.next()) {
            topProducts.getItems().add(new Frame(res));
            if (++count == 6) break;
         }
      } catch (Exception e) {
         System.out.println("In CategoryFrame Constructer");
         System.out.println(e);
         // e.printStackTrace();
      }

      topProducts.setOrientation(Orientation.HORIZONTAL);
      topProducts.setMaxWidth(topProducts.getItems().size() * (Frame.WIDTH + 49));
      // topProducts.setMaxWidth(1500);
      // topProducts.setStyle("-fx-padding: 0 0 0 70");
      topProducts.setStyle("-fx-background-color: black");
      topProducts.setMinHeight(Frame.HEIGHT + 90);

      topProducts.setCellFactory(lv -> {
         Frame cell = new Frame();
         cell.autosize();
         cell.getStyleClass().add("frame-cell");
         return cell;
      });

      // Action event
      topProducts.setOnMouseClicked(event -> {
         Frame frame = topProducts.getSelectionModel().getSelectedItem();
         System.out.println(frame.id);
         // display("");   //  Omar page
      });
      
      categoryName.setText(name);
      categoryName.getStyleClass().add("category-label");
      
      vbox = new VBox(categoryName, topProducts);
   }

   @Override
   protected void updateItem(CategoryFrame categoryFrame, boolean empty) {
      // System.out.println("In update Item in CategoryFrame Class");
      super.updateItem(categoryFrame, empty);
      if (empty || categoryFrame == null) {
         setText(null);
         setGraphic(null);
      } else {
         setGraphic(categoryFrame.vbox);
      }
   }

}