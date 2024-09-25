import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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

   public void delete(int id, String...words) {
      // Case 1 : deleting a word that doesn't exist in the ProductsTrie
      // Case 2 : ahmed         (delete ahmed) ---> remove (a) in first hashmap
      // Case 3 : do - done     (delete do)   ----> set wordEnd to false
      // Case 4 : do - done     (delete done) ----> remove hashmap of letter (o)
      // Case 5 : bear - bean   (delete bean) ----> remove key (n) in the hashmap of letter(a)
      for(String word : words) delete(word, id);
   }

   private void delete(String word, int id) {
      if(!search(word)) return; // Case 1
      this.size--;
      TrieNode temp = head;
      Stack<TrieNode> stack = new Stack<>();
      char[] wordArr = word.toCharArray();

      for(char c : wordArr) {
         stack.push(temp.letters.get(c));
         temp = temp.letters.get(c);
      }
      if(!stack.peek().letters.isEmpty()) { // Case 3
         stack.peek().sentenceEnd = false;
         return;
      }

      int i = wordArr.length - 1;
      boolean commonChar = false;
      while (true) {
         temp = stack.pop(); // First iteration ---> temp = last letter (must be removed)
         if(!commonChar) {
            if(stack.isEmpty()) break;
            stack.peek().letters.remove(wordArr[i]);
         }
         i--;
         if(stack.isEmpty()) break;
         commonChar = !stack.peek().letters.isEmpty();
      }
   }

   private List<String> toList(TrieNode tempNode, List<String> list, StringBuilder str) {
      if(tempNode.sentenceEnd || (str.length() > 50 && str.charAt(str.length() - 1) == ' ')) {
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
   
   void printLevels() {
      // print the letters that is in front of the queue
      // then add all letters of the printed hashmap
      Queue<Map<Character, TrieNode>> queue = new LinkedList<>();
      queue.add(head.letters);
      int prev_level = 1;
      int current_level = 0;

      while (!queue.isEmpty()) {
         for(TrieNode tempNode : queue.peek().values()) {
            queue.add(tempNode.letters);
            current_level++;
         }
         System.out.print(queue.poll().keySet().toString() + " ");
         if(--prev_level == 0) {
            prev_level = current_level;
            current_level = 0;
            System.out.println();
         }
      }
   }
   
   public void addChar(char typed) {
      stackTrieNodes.push(stackTrieNodes.isEmpty() ? head.letters.get(typed) : stackTrieNodes.peek().letters.get(typed));
   }
   public void removeChar() {
      stackTrieNodes.pop();
   }

   public static void saveTrie(ProductsTrie trie, String filePath) throws IOException {
      try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
         oos.writeObject(trie);
      }
   }
   
   public static ProductsTrie loadTrie(String filePath) throws IOException, ClassNotFoundException {
      try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
         return (ProductsTrie) ois.readObject();
      }
   }

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
