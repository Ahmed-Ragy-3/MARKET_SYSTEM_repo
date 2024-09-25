import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

class TrieNode implements Serializable {
   
   Map<Character, TrieNode> letters;
   List<Integer> ids;
   boolean sentenceEnd = false;
   boolean wordEnd;
   
   public TrieNode() {
      this.wordEnd = false;
      letters = new HashMap<>();
      this.ids = new ArrayList<>();
   }
   
   public TrieNode(Character c) {
      this.wordEnd = false;
      letters = new HashMap<>();
      letters.put(c, new TrieNode());
      this.ids = new ArrayList<>();
   }
   
   public TrieNode(Character c, boolean end) {
      this.wordEnd = end;
      letters = new HashMap<>();
      letters.put(c, new TrieNode());
      this.ids = new ArrayList<>();
   }

}

public class ProductsTrie extends Thread implements Serializable { 
   private TrieNode head;
   public Stack<TrieNode> stackTrieNodes;
   int size;

   public ProductsTrie() {
      this.head = new TrieNode();
      this.size = 0;
      this.stackTrieNodes = new Stack<>();
   }

   boolean isEmpty() {
      return (this.size == 0);
   }

   void clear() {
      this.size = 0;
      this.head = new TrieNode();
   }

   private String toLowerAlphaNum(String word) {
      return word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
   }
   
   public void insert(String productName, int id) {
      String[] words = productName.split("\\s+");
      // System.out.println(productName);
      TrieNode temp = head;
      
      // for (int i = 0; i < words.length; i++) {
      //    temp = insertWord(words[i], id, temp);
      //    this.size++;
      // }
      // temp.sentenceEnd = true;

      for (int i = 0; i < words.length; i++) {
         for (int j = i; j < words.length; j++) {
            temp = insertWord(words[j], id, temp);
         }
         this.size++;
         temp.sentenceEnd = true;
         temp = head;
      }
   }
   
   private TrieNode insertWord(String word, int id, TrieNode temp) {
      word = toLowerAlphaNum(word);
      for(char c : word.toCharArray()) {
         temp.letters.putIfAbsent(c, new TrieNode());
         
         temp = temp.letters.get(c);
         temp.ids.add(id);
      }
      // add space
      temp.letters.putIfAbsent(' ', new TrieNode());
      temp = temp.letters.get(' ');
      temp.ids.add(id);

      temp.wordEnd = true;
      return temp;
   }

   public boolean search(String sentence) {
      sentence = toLowerAlphaNum(sentence);
      TrieNode temp = head;
      for(char c : sentence.toCharArray()) {
         if(!temp.letters.containsKey(c))
            return false;

         temp = temp.letters.get(c);
      }
      return temp.wordEnd;
   }
   
   public int getId(String suggestion) {
      System.out.println(suggestion);
      char[] suggestionChars = suggestion.toCharArray();
      int i = Runner.typedText.length();
      TrieNode temp = stackTrieNodes.peek().letters.get(suggestionChars[i]);

      while(++i < suggestionChars.length) {
         if(temp.ids.size() == 1) {
            return temp.ids.get(0);
         }
         temp = temp.letters.get(suggestionChars[i]);
      }
      return temp.ids.get(0);
   }

   private List<String> toList(TrieNode tempNode, List<String> list, StringBuilder str) {
      if(tempNode.ids.size() == 1 && (tempNode.sentenceEnd || (str.length() > 50 && str.charAt(str.length() - 1) == ' '))) {
         list.add(Runner.typedText + str.toString());
         return list;
      }
      
      for(char c : tempNode.letters.keySet()) {
         str.append(c);
         toList(tempNode.letters.get(c), list, str);
         str.deleteCharAt(str.length() - 1);
      }
      return list;
   }

   public List<String> suggest() {
      if(stackTrieNodes.isEmpty() || stackTrieNodes.peek() == null)  return new ArrayList<>();

      return toList(stackTrieNodes.peek(), new ArrayList<>(), new StringBuilder());
   }
   
   public void addChar(char typed) {
      stackTrieNodes.push(stackTrieNodes.isEmpty() ? head.letters.get(typed) : stackTrieNodes.peek().letters.get(typed));
   }
   public void removeChar() {
      stackTrieNodes.pop();
   }

   // public static void saveTrie(ProductsTrie trie, String filePath) throws IOException {
   //    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
   //       oos.writeObject(trie);
   //    }
   // }
   
   // public static ProductsTrie loadTrie(String filePath) throws IOException, ClassNotFoundException {
   //    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
   //       return (ProductsTrie) ois.readObject();
   //    }
   // }

   public static void main(String[] args) {
      // building trie
   }

   @Override
   public void run() {
      // Thread method
      ResultSet res = DB.execQuery("SELECT PRODUCT_ID, PRODUCT_NAME FROM PRODUCTS");
      try {
         while (res.next()) {
            insert(res.getString(2), res.getInt(1));
         }
      } catch (SQLException e) {
         System.out.println(e);
      }
   }
}