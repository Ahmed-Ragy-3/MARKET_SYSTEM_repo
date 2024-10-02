import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ProductDetails implements Initializable{
   public static String prev_page;
   public static int id;

   @FXML
   private ImageView image;
   @FXML
   private Label name;
   @FXML
   private Label price;
   @FXML
   private Label brand;
   @FXML
   private Label category;
   @FXML
   private Label rate;
   @FXML
   private ProgressBar rateBar;
   @FXML
   private Label description;
   @FXML
   private Label features;
   @FXML
   private Label balance;

   @FXML
   private VBox featuresVbox;

   private int current_image = 0;
   List<Image> images = new ArrayList<>();

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      ResultSet res = DB.execQuery("SELECT * FROM PRODUCTS WHERE PRODUCT_ID = " + id);
      try {
         res.next();
         
         images.add(Runner.getImageView(id, res.getString(8)).getImage());
         for(String im : res.getString(11).split(",")) {
            images.add(new Image(im));
         }
         image.setImage(images.get(current_image));

         name.setText(name.getText() + res.getString(2));
         name.autosize();
         price.setText(price.getText() + res.getString(3));
         brand.setText(brand.getText() + res.getString(6));
         category.setText(category.getText() + res.getString(9));

         float rate_num = res.getFloat(4);
         rate.setText(rate.getText() + rate_num);
         rateBar.setProgress(rate_num / 5);
         description.setText(res.getString(13));
         description.autosize();

         String features_str = res.getString(12);
         if(features_str == null) {
            featuresVbox.setVisible(false);
         }else {
            features.setText(features_str);
            features.autosize();
         }
      } catch (SQLException e) {
         System.out.println("In fill method in Product Details");
         System.out.println(e);
         // e.printStackTrace();
      }

      if(Runner.user == null) {
         balance.setVisible(false);
      }else {
         balance.setText(balance.getText() + Runner.user.getBalance());
      }
   }

   public static void fill(int id) {

   }

   @FXML
   private void nextImage(ActionEvent event) {
      current_image++;
      image.setImage(images.get(current_image % images.size()));
   }
   @FXML
   void nextImageLabel(MouseEvent event) {
      nextImage(null);
   }

   @FXML
   void back(ActionEvent event) {
      Runner.display(prev_page);
   }

   
}


class Product {

   private int id, remaining;
   public String name, category, brand, description;
   public float price, rate;
   public byte discount;
   public List<Image> images;    // first image is IMAGE_URL
   public List<String> features;    // first image is image

   public Product() {}
   
   public Product(int id) {
      this(DB.execQuery("SELECT * FROM PRODUCTS WHERE PRODUCT_ID = " + id));
   }
   public Product(String prodName) {
      this(DB.execQuery("SELECT * FROM PRODUCTS WHERE PRODUCT_NAME = '" + prodName + "'"));
   }

   public Product(ResultSet res) {
      if(DB.emptyQuery(res)) {
         System.out.println("No Product");
         return;
      }
      try {
         res.next();
         
         this.id = res.getInt("PRODUCT_ID");
         this.name = res.getString("PRODUCT_NAME");
         this.price = res.getFloat("PRICE");
         this.rate = res.getFloat("RATE");
         this.discount = res.getByte("DISCOUNT");
         this.remaining = res.getInt("Quantity");
         this.category = res.getString("CATEGORY");
         this.brand = res.getString("BRAND");
         this.description = res.getString("DESCRIPTION");
         
         // this.features = modifyFeatures(res.getString("FEATURES"));
         // this.images = modifyImages(res.getString("IMAGE_URL"), res.getString("IMAGES"));
         
      } catch (Exception e) {
         System.out.println("In Product Constructer - id = " + this.id);
         System.out.println(e);
         // System.out.println(this.images.get(0));
         // e.printStackTrace();
      }
   }

   public static List<Image> modifyImages(String image_str, String images_str) {
      List<Image> images = new ArrayList<>();
      
      String[] images_arr = images_str.split(",");
      
      if(image_str != null) {   // there is IMAGE_URL
         images.add(new Image(image_str));
      }
      for(String str : images_arr) {
         images.add(new Image(str));
      }

      return images;
   }

   public static List<String> modifyFeatures(String features_str) {
      if(features_str == null) return null;
   
      List<String> features = new ArrayList<>();

      String[] features_arr = features_str.split(".,");
      for (String feature : features_arr) {
         features.add(feature);
      }
      return features;
   }

   // Getters & Setters
   public int getId() {
      return id;
   }
   public void setId(int id) {
      this.id = id;
   }

   public int getRemaining() {
      return remaining;
   }
   public void setRemaining(int remaining) {
      this.remaining = remaining;
   }

   public ResultSet get_products_in_price_range(float min, float max) {
      String query = "select NAME, PRICE, DESCRIPTION from PRODUCTS where PRICE is between ";
      query += Float.toString(min) + " and " + Float.toString(max);
      return DB.execQuery(query);
   }
}
