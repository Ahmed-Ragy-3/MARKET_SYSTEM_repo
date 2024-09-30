import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class ProductController implements Initializable {
   public static String status;
   public static boolean isSelected = false;
      
   @FXML
   private Button statusButton;

   @FXML
   private TextField enterId;

   @FXML
   private TextField name;
   @FXML
   private TextField category;
   @FXML
   private TextField url;
   @FXML
   private TextField price;
   @FXML
   private TextField brand;
   @FXML
   private TextField quantity;

   
   @FXML
   private TextArea description;
   @FXML
   private TextArea features;

   private static String nullIfEmpty(String arg) {
      return (arg.equals("") || arg == null) ? "NULL" : arg;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      statusButton.setText(status);

      if(status.equals("Edit") || status.equals("Delete")) {
         statusButton.setVisible(false);
         enterId.setVisible(true);
      }

      enterId.setOnKeyPressed(event -> {
         if(event.getCode() == KeyCode.ENTER) {
            enterId.setVisible(false);
            statusButton.setVisible(true);
            try {
               int id = Integer.parseInt(enterId.getText());
               ResultSet res = DB.execQuery("SELECT * FROM PRODUCTS WHERE PRODUCT_ID = " + id);
               res.next();
               
               name.setText(res.getString(2));
               price.setText(res.getString(3));

               brand.setText(res.getString(6));
               quantity.setText(res.getString(7));
               url.setText(res.getString(8));
               category.setText(res.getString(9));

               features.setText(res.getString(12));
               description.setText(res.getString(13));

            } catch (NumberFormatException e) {
               Runner.showAlert("Invalid ID", "Invalid ID\nPlease enter valid ID");
               statusButton.setVisible(false);
               enterId.setVisible(true);

            } catch (SQLException e) {
               Runner.showAlert("Invalid ID", "ID is not found\nPlease enter another ID");
               statusButton.setVisible(false);
               enterId.setVisible(true);
            }
         }
      });
   }

   public static String surround(String str) {
      if(str == null || str.equals("")) return "NULL";
      return "'" + str.replaceAll("'", "''") + "'";
   }

   @FXML
   void submit(ActionEvent event) {
      StringBuilder query = new StringBuilder("");

      if(status.equals("Add")) {
         query.append("INSERT INTO PRODUCTS VALUES (NULL, ");

         query.append(surround(name.getText()));
         query.append(", " + price.getText());
         query.append(", 0, 0, BRAND = " + surround(brand.getText()));
         query.append(", " + quantity.getText());
         query.append(", " + surround(url.getText()));
         query.append(", " + surround(category.getText()));
         query.append(", SYSDATE, NULL, " + surround(features.getText()));
         query.append(", " + surround(description.getText()) + ");");

      } else if (status.equals("Edit")) {
         query.append("UPDATE PRODUCTS SET ");

         query.append("PRODUCT_NAME = " + surround(name.getText()));
         query.append(", Category = " + surround(category.getText()));    
         query.append(", BRAND = " + surround(brand.getText()));           
         query.append(", PRICE = " + price.getText());
         query.append(", IMAGE_URL = " + surround(url.getText()));
         query.append(", DESCRIPTION = " + surround(description.getText()));
         query.append(", FEATURES = " + surround(features.getText()));
         query.append(", QUANTITY = " + quantity.getText());

         query.append(" WHERE PRODUCT_ID = " + enterId.getText());

      } else {     // delete
         query.append("DELETE FROM PRODUCTS WHERE PRODUCT_ID = " + enterId.getText());
      }

      DB.execQuery(query.toString());
   }
}
