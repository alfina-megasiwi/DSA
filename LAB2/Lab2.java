import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

public class Lab2{
    private static InputReader in = new InputReader(System.in);
    private static PrintWriter out = new PrintWriter(System.out);
    private static LinkedList theLL = new LinkedList();
    private static ListNode printIt = new ListNode();
    private static ListNode current = theLL.header;
    private static int ins = 0;

	public static void main(String[] args) throws IOException {
        int loop = in.nextInt();
        for(int i = 0; i<loop;i++){
            queryHandler(in.next());
        }

        printIt = theLL.header.next;
        while(printIt!=null){
            out.print(printIt.element);
            printIt = printIt.next;
        }
        // don't erase this, otherwise the output will not be written
        out.close();
	}
    public static void queryHandler(String query){
        if(query.equals("ADD")){
            ADD();
        }else if(query.equals("DEL")){
            DEL();
        }else if(query.equals("HOME")){
            HOME();
        }else if(query.equals("END")){
            END();
        }else if(query.equals("RIGHT")){
            RIGHT();
        }else if(query.equals("LEFT")){
            LEFT();
        }else if(query.equals("INS")){
            ins++;
        }else{
            return;
        }

    }
    
    public static void ADD(){
        if(ins%2 == 0){
            if(current.next == null){
                ListNode tmp = new ListNode(in.next().charAt(0),current,null);
                current.next = tmp;
                current = tmp;
                theLL.last = tmp;
            }else{
                ListNode tmp = new ListNode(in.next().charAt(0),current,current.next);
                current.next.prev = tmp;
                current.next = tmp;
                current = tmp;
            }
        }else{
            if(current.next == null){
                ListNode tmp = new ListNode(in.next().charAt(0),current,null);
                current.next = tmp;
                current = tmp;
                theLL.last = tmp;
            }else{
                current.next.element = in.next().charAt(0);
                current = current.next;
            }
        }
    }

    public static void DEL(){
        if(current.next == null){
            return;
        }else{
            if(current.next.next == null){
                current.next = null;
                theLL.last = current;
            }else{
                ListNode tmp = current.next.next;
                tmp.prev = current;
                current.next = tmp;
            }            
            
       }
    }

    public static void HOME(){
        current = theLL.header;
    }

    public static void END(){
        current = theLL.last;
    }

    public static void RIGHT(){
        if(current.next == null){
            return;
        }else{
            current = current.next;
        }
    }

    public static void LEFT(){
        if(current.prev == null){
            return;
        }else{
            current = current.prev;
        }
    }

    public static class LinkedList{
        public ListNode header;
        public ListNode last;

        public LinkedList(){
            header = new ListNode();
            last = header;
        }
    }

    public static class ListNode{
        public Character element;
        public ListNode next;
        public ListNode prev;

        public ListNode(Character theElement, ListNode p, ListNode n){
            element = theElement;
            prev = p;
            next = n;

        }
        public ListNode(Character theElement){
            element = theElement;
        }
        public ListNode(){
            this(null,null,null);
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