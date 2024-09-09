import java.sql.ResultSet;
//import java.util.List;

public class Product {

   private int id, remaining;
   public String name, category, company, description;
   public float price, rate;
   public byte discount;
   public String imageUrl;

   public Product() {}

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

   // // get product by its id
   // public Product getProduct(int id) {
   
   // }

   // // get product by its name
   // public Product getProduct(String name) {
   
   // }

   // public List<Product> getProductsInCategory(String category_name) {

   // }

   public ResultSet get_products_in_price_range(float min, float max) {
      String query = "select NAME, PRICE, DESCRIPTION from PRODUCTS where PRICE is between ";
      query += Float.toString(min) + " and " + Float.toString(max);
      return DB.execQuery(query);
   }
}