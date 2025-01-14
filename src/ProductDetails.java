import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ProductDetails implements Initializable{
   public static String prev_page;
   public static int id;

   @FXML
   private ImageView image;
   @FXML
   private Label name;
   @FXML
   private TextFlow price;
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
   private Button add_to_cart;
   @FXML
   private Button suggestButton;
   @FXML
   private Button buy;

   @FXML
   private VBox featuresVbox;
   @FXML
   private Pane suggestPane;
   @FXML
   private TextField recieverUserName;

   private float p;

   private int current_image = 0;
   List<Image> images = new ArrayList<>();

   @FXML
   public void initialize(URL location, ResourceBundle resources) {
      ResultSet res = DB.execQuery("SELECT * FROM PRODUCTS WHERE PRODUCT_ID = " + id);
      try {
         res.next();

         ImageView imageView = Runner.getImageView(id, res.getString(8));
         imageView.imageProperty().addListener((obs, oldImage, newImage) -> {
            if (newImage != null) {
               images.add(newImage);
               image.setImage(images.get(0)); // Set the first image
            }
         });

         // images.add(Runner.getImageView(id, res.getString(8)).getImage());
         for(String im : res.getString(11).split(",")) {
            images.add(new Image(im, 235, 255, true, true));
         }
         image.setImage(images.get(0));

         name.setText(name.getText() + res.getString(2));
         name.autosize();

         p = res.getFloat(3);
         float discount =  res.getFloat(5);
         Text t1 = new Text("Price: ");
         Text t2 = new Text(Float.toString(p));

         if(discount == 0) {
            price.getChildren().setAll(t1, t2);
            
         }else {
            t2.setStrikethrough(true);
            p -= (discount / 100) * p; 
            price.getChildren().setAll(t1, t2, new Text(Float.toString(p)));
         }
         
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
         add_to_cart.setVisible(false);
         suggestButton.setVisible(false);
         buy.setVisible(false);

      }else {
         balance.setText(balance.getText() + Runner.user.getBalance());
      }
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

   @FXML
   void addToCart(ActionEvent event) {
      DB.execQuery("""
         UPDATE USERS
         SET CART = CART || num_arr(%d)
         WHERE USER_ID = %d
         AND %d NOT MEMBER OF CART;
      """.formatted(id, Runner.user.getId(), id));
   }

   @FXML
   void buyProduct(ActionEvent event) {
      if(Runner.user.balance < p) {
         Runner.showAlert("No enough balance", "Your Balance is not enough\nfor buying this product");
         return;
      }
      Runner.user.balance -= p;
      balance.setText("Balance: " + Runner.user.balance);
      DB.execQuery("""
         UPDATE USERS 
         SET BALANCE = %d 
         WHERE USER_ID = %d
      """.formatted(Runner.user.balance, Runner.user.getId()));

   }
   
   @FXML
   void suggest(ActionEvent ev) {
      suggestPane.setVisible(true);
      
      recieverUserName.setOnKeyPressed(event -> {
         if(event.getCode() == KeyCode.ENTER) {
            ResultSet res = DB.execQuery("SELECT USER_ID FROM USERS WHERE USERNAME = '" + recieverUserName.getText() + "'");
            if(DB.emptyQuery(res)) {
               Runner.showAlert("Invalid Username","Username is not found");
            }else {
               try {
                  res.next();
                  int recieverId = res.getInt(1);
                  DB.execQuery("INSERT INTO SUGGESTIONS VALUES (%d, %d, %d, %d)"
                  .formatted(Runner.user.getId(), recieverId, id, 0));
               } catch (SQLException e) {
                  System.out.println("In suggest in ProductDetails");
                  // e.printStackTrace();
               }
            }
            suggestPane.setVisible(false);
         }
      });
            
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
