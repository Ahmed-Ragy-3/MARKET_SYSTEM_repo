import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Frame extends VBox {
   public final static int image_height = 260;
   public final static int width = 230;

   public int id;
   private ImageView image;
   private Label discount;
   private Label name, price, rate;

   public Label getName() {
      return name;
   }
   public void setName(Label name) {
      this.name = name;
   }

   public Frame() {}

   public Frame(Product product) {
      image = new ImageView();
      image.setImage(product.images.get(0));
      image.setFitHeight(image_height);
      image.setFitWidth(width);

      name = new Label(product.name);
      price = new Label("Price: " + product.price + " $");
      rate = new Label("Rating: " + product.rate);
      discount = new Label("Discount: " + product.discount + "%");

      this.getChildren().addAll(image, name, price, rate);

      this.setSpacing(10);
      // this.setStyle();
   }

   public ImageView getImage() {
      return image;
   }
   public void setImage(ImageView image) {
      this.image = image;
   }

   public Label getDiscount() {
      return discount;
   }
   public void setDiscount(Label discount) {
      this.discount = discount;
   }

   public Label getPrice() {
      return price;
   }
   public void setPrice(Label price) {
      this.price = price;
   }

   public Label getRate() {
      return rate;
   }
   public void setRate(Label rate) {
      this.rate = rate;
   }

}