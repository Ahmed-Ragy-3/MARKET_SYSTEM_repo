import java.sql.ResultSet;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


// public class Frame extends ListCell<Frame> {
//    public final static int image_height = 260;
//    public final static int height = 300;
//    public final static int width = 230;
   
//    public int id;
//    private ImageView image;
//    private Label name, price;
   
//    private VBox vbox = new VBox();
   
//    public Frame() {}
   
//    public Frame(ResultSet res) {
//       try {
//          System.out.println("start");

//          this.id = res.getInt(1);
//          this.name = new Label(res.getString(2));

//          this.image = new ImageView(new Image(res.getString(3)));
//          image.setFitHeight(image_height);
//          image.setFitWidth(width);
         
//          float initial_price = res.getFloat(4);
//          float discount = res.getInt(5) / 100;
//          initial_price -= (initial_price * discount);
//          this.price = new Label(Float.toString(initial_price));
         
         
//          vbox.getChildren().addAll(image, name, price);
//          vbox.setSpacing(50);

//          System.out.println("end");
         
//       } catch (Exception e) {
//          System.out.println("In Frame Constructer in product id: " + this.id);
//          System.out.println(e);
//       }
//    }

//    @Override
//    protected void updateItem(Frame frame, boolean empty) {
//       System.out.println("in update item in frame");
//       super.updateItem(frame, empty);
//       if (empty || frame == null) {
//          setText(null);
//          setGraphic(null);
//       } else {
//          // Set frame details
//          image.setImage(frame.image.getImage());
//          name.setText(frame.name.getText());
//          price.setText(frame.price.getText());

//          setGraphic(vbox);
//       }
//    }
   
//    // Getters and Setters
//    public Label getName() {
//       return name;
//    }
//    public void setName(Label name) {
//       this.name = name;
//    }
   
//    public ImageView getImage() {
//       return image;
//    }
//    public void setImage(ImageView image) {
//       this.image = image;
//    }
   
//    public Label getPrice() {
//       return price;
//    }
//    public void setPrice(Label price) {
//       this.price = price;
//    }
   
// }


class CategoryFrame extends ListCell<CategoryFrame> {
   // public ListView<Frame> topProducts = new ListView<>();
   public ListView<String> topProducts = new ListView<>();
   public Label categoryName = new Label();

   private VBox vbox = new VBox();
   
   public CategoryFrame() {}

   public CategoryFrame(String name) {
      this.categoryName.setText(name);;
      this.categoryName.setVisible(true);
      
      // ResultSet res = DB.execQuery("SELECT * FROM HOME_VIEW WHERE CATEGORY = '" + name + "'");
      // try {
      //    // DB.printQuery(res);
      //    byte count = 0;
      //    while (res.next()) {
      //       topProducts.getItems().add(new Frame(res));
      //       if (++count == 7) break;
      //    }
      // } catch (Exception e) {
      //    System.out.println("In CategoryFrame Constructer");
      //    System.out.println(e);
      //    // e.printStackTrace();
      // }

      topProducts.getItems().add("ahmed");
      topProducts.getItems().add("Ragy");
      topProducts.getItems().add("hussein");
      topProducts.getItems().add("Hafez");

      // topProducts.setPrefWidth(150);
   }

   @Override
   protected void updateItem(CategoryFrame categoryFrame, boolean empty) {
      // System.out.println("In update Item in CategoryFrame Class");
      super.updateItem(categoryFrame, empty);
      if (empty || categoryFrame == null) {
         setText(null);
         setGraphic(null);
      } else {
         // Set the category name and load frames into the list view
         categoryName.setText(categoryFrame.categoryName.getText());
         categoryName.getStyleClass().add("category-label");

         topProducts.getItems().clear();
         topProducts.getItems().addAll(categoryFrame.topProducts.getItems());
         topProducts.setOrientation(Orientation.HORIZONTAL);
         topProducts.getStyleClass().add("list-view-categoryframe");
         
         topProducts.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.getStyleClass().add("list-cell-categoryframe");
            return cell;
        });
         // topProducts.setPrefWidth(150);

         vbox = new VBox(categoryName, topProducts);
         vbox.getStyleClass().add("vbox-container-categoryframe");
         // Custom cell factory for Frame objects
         // topProducts.setCellFactory(param -> new Frame());
         setGraphic(vbox);
      }
   }

}