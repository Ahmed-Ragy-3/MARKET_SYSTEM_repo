import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MultipleProducts implements Initializable {
   /* Filter by:
      Price - Rate - Brand
      discount (yes or no)
   */
   public static ResultSet current;
   
   @FXML
   private VBox brandVBox;

   @FXML
   private TextField minPrice, maxPrice;

   @FXML
   private TextField minRate, maxRate;

   @FXML
   private ListView<ProductFrame> products;

   public static Set<String> allBrands = new HashSet<>();
   public static List<String> brandsFiltered = new ArrayList<>();
   public static float max_price = 0;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      products.setCellFactory(lv -> {
         ProductFrame cell = new ProductFrame();
         cell.autosize();
         cell.getStyleClass().add("product-frame");
         // cell.setMaxHeight(Frame.IMAGE_HEIGHT);
         // cell.setMaxWidth(1300);
         return cell;
      });

      try {
         while (current.next()) {
            products.getItems().add(new ProductFrame(current, false));
         }
         
         for(String brand : allBrands) {
            CheckBox check = new CheckBox(brand);
            check.setStyle("""
               -fx-font-size: 18px;
               -fx-font-weight: bold;
            """);

            check.setOnAction(event -> {
               if (check.isSelected()) {
                  brandsFiltered.add(check.getText());
               } else {
                  brandsFiltered.remove(check.getText());
               }
            });

            check.setSelected(true);
            brandsFiltered.add(check.getText());
            brandVBox.getChildren().add(check);
         }
      } catch (SQLException e) {
         System.out.println("In initialize Multiple Products");
         System.out.println(e);
         // e.printStackTrace();
      }
   }

   @FXML
   void filter(ActionEvent event) {
      minRate.setText((minRate.getText().equals("")) ? "0" : minRate.getText());
      maxRate.setText((maxRate.getText().equals("")) ? "5" : maxRate.getText());
      minPrice.setText((minPrice.getText().equals("")) ? "0" : minPrice.getText());
      maxPrice.setText((maxPrice.getText().equals("")) ? Float.toString(max_price) : maxPrice.getText());

      current = DB.execQuery(" SELECT * FROM TEMP_VIEW WHERE PRICE BETWEEN " +
      minPrice.getText() + " AND "  + maxPrice.getText() + " AND RATE BETWEEN " + minRate.getText() + 
      " AND " + maxRate.getText() + " AND BRAND IN " + 
      brandsFiltered.toString().replaceAll("'", "''").replace("[", "('").
      replace("]", "')").replaceAll(", ", "','"));
      
      products.getItems().clear();

      try {
         while (current.next()) {
            products.getItems().add(new ProductFrame(current, false));
         }
      } catch (SQLException e) {
         System.out.println("In filter method");
         System.out.println(e);
      }
   }
}

class ProductFrame extends ListCell<ProductFrame> {
   private int id;
   VBox texts = new VBox();
   HBox data = new HBox();

   public ProductFrame() {}

   public ProductFrame(int id) {
      this(DB.execQuery("""
         SELECT PRODUCT_ID, PRODUCT_NAME, PRICE, RATE, DISCOUNT, BRAND, IMAGE_URL FROM PRODUCTS WHERE PRODUCT_ID =  
      """ + id), true);
   }

   public ProductFrame(ResultSet res, boolean next) {
      try {
         if(next) res.next();

         System.out.println(res.getInt(1));
         
         id = res.getInt(1);

         Label text = new Label(res.getString(2));
         text.setWrapText(true);
         text.getStyleClass().add("product-frame-texts");
         texts.getChildren().add(text);

         float p = res.getFloat(3);
         MultipleProducts.max_price = Math.max(MultipleProducts.max_price, p);
         text = new Label("Price: " + p + " $");
         text.getStyleClass().add("product-frame-texts");
         texts.getChildren().add(text);

         text = new Label("Rate: " + res.getString(4));
         text.getStyleClass().add("product-frame-texts");
         texts.getChildren().add(text);

         String dis = res.getString(5);
         text = new Label("Discount: " + ((dis == null) ? "0" : dis) + "%");
         text.getStyleClass().add("product-frame-texts");
         texts.getChildren().add(text);

         String br = res.getString(6);
         MultipleProducts.allBrands.add(br);
         text = new Label("Brand: " + br);
         text.getStyleClass().add("product-frame-texts");
         texts.getChildren().add(text);

         texts.setSpacing(10);

         ImageView image = Runner.getImageView(id, res.getString(7));
         image.setFitHeight(Frame.IMAGE_HEIGHT);
         image.setFitWidth(Frame.WIDTH);
         data.getChildren().addAll(image, texts);
         
         data.setSpacing(50);
         data.setMaxWidth(1300);
         // data.getStyleClass().add("product-frame");
         data.setOnMouseClicked(event -> {
            System.out.println("display product");
            // display("");
         });

      } catch (SQLException e) {
         System.out.println("In product frame constructer.");
         System.out.println(e);
      }
      
   }

   @Override
   protected void updateItem(ProductFrame frame, boolean empty) {
      // System.out.println("In update item in Frame");
      super.updateItem(frame, empty);
      if (empty || frame == null) {
         setText(null);
         setGraphic(null);
      } else {
         setGraphic(frame.data);
      }
   }
}