import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

public class Product {

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
      // if(DB.emptyQuery(res)) {
      //    System.out.println("No Product");
      //    return;
      // }
      try {
         this.id = res.getInt("PRODUCT_ID");
         System.out.println(this.id);
         this.name = res.getString("PRODUCT_NAME");
         this.price = res.getFloat("PRICE");
         this.rate = res.getFloat("RATE");
         this.discount = res.getByte("DISCOUNT");
         this.remaining = res.getInt("Quantity");
         this.category = res.getString("CATEGORY");
         this.brand = res.getString("BRAND");
         this.description = res.getString("DESCRIPTION");
         
         this.features = modifyFeatures(res.getString("FEATURES"));
         this.images = modifyImages(res.getString("IMAGE_URL"), res.getString("IMAGES"));
         
      } catch (Exception e) {
         System.out.println("In Product Constructer - id = " + this.id);
         System.out.println(e);
         System.out.println(this.images.get(0));
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

   // public List<Product> fillProducts(ResultSet res) {
   //    List<Product> list = new ArrayList<>();
   //    while (res.next()) {
         
   //    }
   // }


   // public List<Product> getProductsInCategory(String category_name) {

   // }

   public ResultSet get_products_in_price_range(float min, float max) {
      String query = "select NAME, PRICE, DESCRIPTION from PRODUCTS where PRICE is between ";
      query += Float.toString(min) + " and " + Float.toString(max);
      return DB.execQuery(query);
   }
}
