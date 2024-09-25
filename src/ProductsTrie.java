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
   boolean sentenceEnd, wordEnd;
   
   public TrieNode() {
      this.sentenceEnd = false;
      this.wordEnd = false;
      letters = new HashMap<>();
      this.ids = new ArrayList<>();
   }
   
   public TrieNode(Character c) {
      this.sentenceEnd = false;
      this.wordEnd = false;
      letters = new HashMap<>();
      letters.put(c, new TrieNode());
      this.ids = new ArrayList<>();
   }
   
   public TrieNode(Character c, boolean end) {
      this.sentenceEnd = end;
      this.wordEnd = end;
      letters = new HashMap<>();
      letters.put(c, new TrieNode());
      this.ids = new ArrayList<>();
   }

}

public class ProductsTrie implements Serializable { 
   private TrieNode head;
   public Stack<TrieNode> stackTrieNodes;
   public TrieNode current;
   int size;

   public ProductsTrie() {
      this.head = new TrieNode();
      this.size = 0;
      this.current = head;
      this.stackTrieNodes = new Stack<>();
   }

   boolean isEmpty() {
      return (this.size == 0);
   }

   void clear() {
      this.size = 0;
      this.head = new TrieNode();
   }

   private String toLowerAlpha(String word) {
      return word.replaceAll("[^a-zA-Z]", "").toLowerCase();
  }

   public void insert(String productName, int id) {
      String[] words = productName.split("\\s+");
      // System.out.println(productName);
      for (int i = 0; i < words.length; i++) {
         TrieNode temp = insertWord(words[i], id, head);
         for (int j = i + 1; j < words.length; j++) {
            temp = insertWord(words[j], id, temp);
         }
         temp.sentenceEnd = true;
         this.size++;
      }
   }
   
   private TrieNode insertWord(String word, int id, TrieNode temp) {
      word = toLowerAlpha(word);
      for(char c : word.toCharArray()) {
         temp.letters.putIfAbsent(c, new TrieNode());
         
         temp = temp.letters.get(c);
         temp.ids.add(id);
      }
      temp.wordEnd = true;
      return temp;
   }

   public boolean search(String sentence) {
      sentence = toLowerAlpha(sentence);
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

   private List<StringBuilder> toList(TrieNode tempNode, List<StringBuilder> list, StringBuilder str) {
      if(tempNode.sentenceEnd)
         list.add(str);

      for(char c : tempNode.letters.keySet()) {
         toList(tempNode.letters.get(c), list, str.append(c));
      }

      return list;
   }

   
   public List<StringBuilder> getWordsWithPrefix(TrieNode tempNode) {
      List<StringBuilder> list = new ArrayList<>();

      list = toList(tempNode, list, new StringBuilder());
      return list;
   }
   
   // void suggest(String prefix) {
   //    int count = countWordsWithPrefix(prefix);
   //    if(count == 0) {
   //       System.out.println("No suggestions");
   //       return;
   //    } else if(count == 1) {
   //       System.out.println(count + " suggestion : ");
   //    }else {
   //       System.out.println(count + " suggestions : ");
   //    }
      
   //    System.out.println(getWordsWithPrefix(prefix));
   // }
   
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
      current = current.letters.get(typed);
      stackTrieNodes.push(current);
   }
   public void removeChar() {
      current = stackTrieNodes.pop();
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
      ResultSet res = DB.execQuery("SELECT PRODUCT_ID, PRODUCT_NAME FROM PRODUCTS");
      Runner.trie = new ProductsTrie();
      try {
         while (res.next()) {
            Runner.trie.insert(res.getString(2), res.getInt(1));
         }
      } catch (SQLException e) {
         System.out.println(e);
         // e.printStackTrace();
      }
   }
}
