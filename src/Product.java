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
      if(DB.emptyQuery(res)) {
         System.out.println("No Product with id: " + id);
         return;
      }

      try {
         this.id = res.getInt("PRODUCT_ID");
         this.name = res.getString("PRODUCT_NAME");
         this.price = res.getFloat("PRICE");
         this.rate = res.getFloat("RATE");
         this.discount = res.getByte("DISCOUNT");
         this.remaining = res.getInt("Quantity");
         this.category = res.getString("CATEGORY");
         this.brand = res.getString("BRAND");
         this.description = res.getString("DESCRIPTION");

         String features_str = res.getString("FEATURES");
         if(features_str != null) {
            String[] features_arr = features_str.split("\"\",\"\"");
            features_arr[0].replaceAll("[\"\"", "");
            features_arr[features_arr.length - 1].replaceAll("\"\"]", "");

            features = new ArrayList<>();
            for (String feature : features_arr) {
               features.add(feature);
            }
         }else {
            features = null;
         }

         String image_str = res.getString("IMAGE_URL");
         images = new ArrayList<>();
         images.add(new Image(image_str));
         String images_str = res.getString("IMAGES");

         if(images_str != null) {
            String[] images_arr = images_str.split("\"\",\"\"");
            images_arr[0].replaceAll("[\"\"", "");
            images_arr[images_arr.length - 1].replaceAll("\"\"]", "");

            for(String str : images_arr) {
               images.add(new Image(str));
            }
         }
         
      } catch (SQLException e) {
         System.out.println("In Product Constructer");
         System.out.println(e);
         // e.printStackTrace();
      }
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