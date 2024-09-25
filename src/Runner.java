import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class Runner extends Application {
   public static final String SHOPNAME = "OnShopping";
   public static final String PATH = "C:/VIP/MARKET_SYSTEM_repo";
   public static final int MAX_SUGGESTIONS = 10;

   public static User user;

   public static Stage stage;
   
   @FXML
   private AnchorPane fxmlContent;  // For Scene Builder
   private static AnchorPane content;  // Main content area
   
   @FXML
   private Button login, usernameButton;
   public static Button nameButton;    // same as usernameButton
   public static Button loginButton;

   @FXML
   private ChoiceBox<String> adminChoices;
   public static ChoiceBox<String> myadminChoices;

   @FXML
   private Label adminLabel;
   public static Label myadminLabel;
   
   @FXML
   private ListView<String> searchSuggestions = new ListView<>();   
   @FXML
   private TextField searchBar;

   public static Button adminButton;
   public static ProductsTrie trie;
   public static StringBuilder typedText = new StringBuilder();

   @FXML
   public void initialize() {
      System.out.println("Initialize");
      nameButton = usernameButton;
      loginButton = login;
      content = fxmlContent;

      myadminChoices = adminChoices;
      myadminLabel = adminLabel;

      searchBar.textProperty().addListener((observable, oldText, newText) -> searching(newText));
      searchSuggestions.setCellFactory(lv -> new ListCell<>() {
         @Override
         protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
               setText(null);
            } else {
               setText(item);
            }
            getStyleClass().add("suggestions");
         }
      });

      searchSuggestions.setOnMouseClicked(event -> {
         System.out.println(trie.getId(searchSuggestions.getSelectionModel().getSelectedItem()));
      });
   }

   @FXML
   void profile(ActionEvent event) {
      display("UserProfile");
   }

   
   public static void setUser(String name) {
      user = new User(name);
      nameButton.setText(name);
      nameButton.setVisible(true);
      loginButton.setVisible(false);

      if(user.isAdmin) {
         myadminChoices.getItems().addAll(new String[] {
            "Add Product", "Edit Product", "Delete Product",
            "Delete user account", "Review purchase movement",
            "Add Admin account"
         });
         myadminChoices.setVisible(true);
         myadminChoices.setOnAction(Runner::adminChoice);
         myadminLabel.setVisible(true);
      }
   }
   
   private static void adminChoice(ActionEvent event) {
      String choice = myadminChoices.getValue();
      if(choice.compareTo("Add Product") == 0) {
         ProductController.status = "Add";
         display("Product");
         
      } else if(choice.compareTo("Edit Product") == 0) {
         ProductController.status = "Edit";
         display("Product");
         
      } else if(choice.compareTo("Delete Product") == 0) {
         ProductController.status = "Delete";
         display("Product");

      } else if(choice.compareTo("Review purchase movement") == 0) {
         
      } else {
         
      }
   }
   
   public static void display(String fxmlScreen) {
      try {
         Parent inroot = FXMLLoader.load(Runner.class.getResource(fxmlScreen + ".fxml"));
         stage.setTitle(SHOPNAME + " - " + fxmlScreen);
         content.getChildren().setAll(inroot);
         
      } catch (Exception e) {
         System.out.println("Error in display method when displaying " + fxmlScreen);
         System.out.println(e);
         // e.printStackTrace();
      }
   }
   
   public void OnShopping(ActionEvent event) {
      display("Home");
   }
   
   public void login_button(ActionEvent event) {
      display("Login");
   }
   
   @FXML
   void searching(String newText) {

      if(newText.length() < typedText.length()) {
         typedText.deleteCharAt(typedText.length() - 1);
         trie.removeChar();
      }else {
         char recentChar = Character.toLowerCase(newText.charAt(newText.length() - 1));
         trie.addChar(recentChar);
         typedText.append(recentChar);
      }

      searchSuggestions.getItems().setAll(trie.suggest());

      if(searchSuggestions.getItems().isEmpty()) {
         searchSuggestions.setVisible(false);

      }else {
         searchSuggestions.setVisible(true);
      }

   }

   public void search_button(ActionEvent event) {
      // Implement the functionality to display the appropriate screen
      if(searchSuggestions.getItems().size() == 1) {
         // display("");   // omar page
      }else {
         // display all items
      }
      searchSuggestions.getItems().clear();
      searchSuggestions.setVisible(false);
      // display("SearchScreen");
   }
   
   public static void showAlert(String title, String content) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle(title);
      alert.setContentText(content);
      alert.showAndWait();
   }
   
   @Override
   public void start(Stage primaryStage) throws Exception {
      stage = primaryStage;
      Parent root = FXMLLoader.load(getClass().getResource("Home_bar.fxml"));
      stage.setScene(new Scene(root));
      stage.setMaximized(true);
      stage.getIcons().add(new Image("shopping_icon.png"));
      stage.show();

      display("Home");
   }

   public static void main(String[] args) {
      user = null;
      trie = new ProductsTrie();
      trie.start();      // thread
      launch(args);
   }
   
}
