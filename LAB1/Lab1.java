import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

public class Lab1 {

    private static InputReader in = new InputReader(System.in);
    private static PrintWriter out = new PrintWriter(System.out);

    private static String S; // input string S


	public static void main(String[] args) throws IOException {
		S = in.next();
		int n = S.length(); // length of the string S
        char[] arr = S.toCharArray();

        List<TandaKurung> initializeit = new ArrayList<>();

        for (int i = 0; i < n; i++){
            TandaKurung tk = new TandaKurung(arr[i]);
            initializeit.add(tk);
        }

        Stack<TandaKurung> stackName = new Stack<>();
        Map<Integer,Integer> map=new TreeMap<Integer,Integer>();

        PriorityQueue<Entry> q = new PriorityQueue<>();

        boolean specialcase = false;
        for (int i = 0; i < n; i++){
            if(initializeit.get(i).sifat.equals("open")){
                stackName.push(initializeit.get(i));
                initializeit.get(i).setIndex(i+1);
            }else{
                if(stackName.empty()){
                    specialcase = true;
                }else{
                    TandaKurung top = stackName.peek();
                    if(initializeit.get(i).compare(top)==true){
                        initializeit.get(i).setIndex(i+1);
                        map.put(stackName.pop().getIndex(), initializeit.get(i).getIndex());
                    }
                }
            }

        }        

        for(Integer key : map.keySet()){
            q.add(new Entry(key, map.get(key)));
        }

        if(specialcase == true){
            out.println("INVALID");
        }else if(stackName.empty()) {
            out.println("VALID");
            out.println(map.size());
            for (int i = 0; i < map.size(); i++) {
                Entry temp = q.poll();
                out.println(temp + " "+ temp.getValue());
            }
        }else{
            out.println("INVALID");
        }

        // don't erase this, otherwise the output will not be written
        out.close();
	}

    static class TandaKurung{
        private char karakter;
        private String sifat;
        private int index;

        public TandaKurung(char karakter){
            this.karakter=karakter;
            if(karakter == '(' | karakter == '{' | karakter == '['){
                this.sifat = "open";
            }else{
                this.sifat = "close";
            }
        }
        public char getKarakter(){
            return this.karakter;
        }
        public String getSifat(){
            return this.sifat;
        }
        public int getIndex(){
            return this.index;
        }
        public void setIndex(int index){
            this.index = index;
        }
        public boolean compare(TandaKurung other){
            if(this.karakter == ')' && other.getKarakter()=='('){
                return true; 
            }else if (this.karakter == '}' && other.getKarakter()=='{'){
                return true;
            }else if (this.karakter == ']' && other.getKarakter()=='['){
                return true;
            }else{
                return false;
            }
        }

    }

    static class Entry implements Comparable<Entry>{
        private Integer key;
        private Integer value;
    
        public Entry(int key, int value) {
            this.key = key;
            this.value = value;
        }
    
        public Integer getKey(){
            return this.key;
        }

        public Integer getValue(){
            return this.value;
        }
    
        public int compareTo(Entry other) {
            return this.getKey().compareTo(other.getKey());
        }

        public String toString(){
            return Integer.toString(this.key);
        }
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;
 
        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }
 
        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }
 
        public int nextInt() {
            return Integer.parseInt(next());
        }
 
    }
}