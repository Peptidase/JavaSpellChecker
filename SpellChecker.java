package Assignment1;



import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class SpellChecker {

    private class Trie {
        /*
        Each Trie has an array of 26 other Tries representing other 26 letters
        They represent the other letters that can be traversed from that point
        if the value is * then that is the root Trie (the start)
        Idea is to assign values to the Tries and then traverse through all of
        them by referencing the Tries within the array.
         */


        char value;
        Trie[] letters;
        boolean wordEnd;




        public Trie(char c) {
            this.value = c;
            this.wordEnd = false;
            letters = new Trie[26];
            Arrays.fill(letters, nullTrie);
        }

        void createTrie(char c) {
            int loc = toAlphabeticNum(c);
            letters[loc] = new Trie(c);
        }


        boolean contains(char c) {
            for (Trie t : letters) {
                if (t.value == c) {
                    return true;
                }

            }
            return false;
        }

        Trie get(char c) {

            for (Trie t : letters) {
                if (t.value == c) {
                    return t;
                }
            }
            return null;
        }

        public void setWordEnd(boolean wordEnd) { //Specifies if a node is the end of a word
            this.wordEnd = wordEnd;
        }
    }


    private Trie nullTrie;
    private Trie Root;

    public SpellChecker() //Defines what a 'null' node is and the Root node.
    {
        nullTrie = new Trie('0');
        Root = new Trie('*');
    }

    public static void main(String[] args) {
        String filename1 = "dictionary.txt";
        String filename2 = "sample.txt";
        SpellChecker SC = new SpellChecker();
        SC.addFile(filename1);
        System.out.println(SC.toString());
        SC.checkFile(filename2);
    }

    private void addFile(String filename) { //Reads file and adds to the Trie
        try(Scanner wordParser = new Scanner(new File(filename));){
            while(wordParser.hasNext()){
                this.addWord(wordParser.nextLine());
            }

        }catch (FileNotFoundException fnf){
            fnf.printStackTrace();
        }
    }

    private int checkFile(String filename){
        int count = 0;
        try(Scanner wordParser = new Scanner(new File(filename));){
            while(wordParser.hasNext()){

                String line = wordParser.nextLine();
                if(line.length() == 0) continue;
                String[] linewords = line.split("[\\p{Punct}\\s]+");
                for (String s:linewords)
                {
                    if(!containsWord(s.toLowerCase()))
                    {
                        System.out.println(s);
                        count++;
                    }
                }
            }

        }catch (FileNotFoundException fnf){
            fnf.printStackTrace();
        }
        return count;
    }



    @Override
    public String toString() {
        StringBuilder Sb = new StringBuilder("The Trie Contains: ");
        Sb.append(CountWords(Root));
        Sb.append(" Words");

        return Sb.toString();

    }

    private void addWord(String word) { //Adds the specified word to the trie and flags the end node as the end of a word
        char[] charArray = word.toCharArray();
        Trie T = Root;
        for (char c : charArray) {
            if (!T.contains(c)) {
                T.createTrie(c);
            }
            T = T.get(c);
        }
        T.setWordEnd(true);
    }

    private boolean containsWord(String word) { //Returns boolean if the parameter is flagged as a word
        Trie Trie = Root;
        for (char c : word.toCharArray()) {
            if (!Trie.contains(c)) {
                return false;
            }
            Trie = Trie.get(c);
        }
        if(Trie.wordEnd){
            return true;
        }
        return false;

    }

    private int CountWords(Trie trie)
    {
        int count = 0;
        for (Trie t: trie.letters)
        {
            if(t.value != '0'){
                count+=CountWords(t);
            }
            if(t.wordEnd){
                count+=1;
            }
        }

        return count;
    }

    //Method used for converting the characters to their correct location in array.
    private int toAlphabeticNum(char c) {
        int temp = (int) c;
        temp -= 97;
        return temp;
    }
}











