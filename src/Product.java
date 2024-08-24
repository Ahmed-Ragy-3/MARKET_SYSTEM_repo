public class Product {

   private int id, remaining;
   public String name, categoryName, CompanyName, Description;
   public float price, rate;
   private byte discount;
   //Image(s)

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

   public byte getDiscount() {
      return discount;
   }
   public void setDiscount(byte discount) {
      this.discount = discount;
   }
   /////////////////////////////////
   
   public void getItemData(int id) {
      
   }
}