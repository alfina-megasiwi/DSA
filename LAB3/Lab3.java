import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

class Tree {

    Node root;
    
    /**
     * Membuat Constructor untuk class Tree
     */
    public Tree() {

        // TODO: Implementasi method ini
        root = null;

    }

    /**
     * Membangun Binary Search Tree
     * @param postorder : list urutan angka postorder
     * @param start : start index
     * @param end : end index
     * @return Root Node
     */
 
    public Node constructBST(ArrayList<Integer> postorder, int start, int end) {
        
        if(start > end){
            return null;
        }

        Node cr = new Node(postorder.get(end));
        
        int index = 0;
        for (index = end-1; index >= start ; index--){
            if(postorder.get(index) < cr.element){
                break;
            }
        }
        cr.left = constructBST(postorder, start, index);
        cr.right = constructBST(postorder, index+1, end-1);
        return cr;

    }

    // test the tree
    public void inorderr(){
        inorder(root);
    }
    public void inorder(Node root)
    {
        if (root == null) {
            return;
        }
 
        inorder(root.left);
        System.out.print(root.element + " ");
        inorder(root.right);
    }

    public void remove(int jarak) {
        root = remove(root, jarak);
    }

    /**
     * Menghapus Node yang memiliki nilai jarak
     * @param node : node 
     * @param jarak : jarak
     * @return Node
     */

    // from slide
    private Node remove(Node node, int jarak) {

        // TODO: Lengkapi Method ini
        if(node == null){
            return null;
        }

        if(jarak<node.element){
            node.left = remove(node.left, jarak);
        }else if(jarak>node.element){
            node.right = remove(node.right, jarak);
        }else{
            if(node.left != null && node.right != null){
                node.element = minValue(node.right);
                node.right = removeMin(node.right);
            }else{
                if(node.left!=null){
                    node = node.left;
                }else{
                    node = node.right;
                }
            }
        }
        return node;
    }

    // from slide
    private Node removeMin(Node node) {

        if(node == null){
            return null;
        }
         
        if(node.left != null){
            node.left = removeMin(node.left);
            return node;
        }else{
            return node.right;
        }
        
    }

    /**
     * Mengembalikan Minimum Value dari Subtree Anak kanan dari node
     * @param node : node 
     * @return integer
     */
    private int minValue(Node node) {

        // TODO: Lengkapi Method ini
        while(node.left!=null){
            node = node.left;
        }
        return node.element;
    }

    /**
     * Mengembalikan tinggi dari Tree
     * @param node : node 
     * @return tinggi Tree
     */

    public int treeheight(){
        return height(root);
    }

    public int height(Node node){
        
        // TODO: Lengkapi Method ini
        if(node == null){
            return -1;
        }else{
            int leftsubtree = height(node.left);
            int rightsubtree = height(node.right);
            return(Math.max(leftsubtree, rightsubtree))+1;
        }

    }
}

class Node {

    // TODO: Inisiasi attribute dari class Node
    int element;
    Node left;
    Node right;

    public Node(int jarak) {
        
        // TODO: Implementasi method ini
        this.element = jarak;
        this.left = this.right = null;
        
    }
    
}

public class Lab3 {
    private static InputReader in = new InputReader(System.in);
    private static PrintWriter out = new PrintWriter(System.out);
    public static void main(String[] args) throws IOException {

        // TODO: Implementasi method ini

        ArrayList<Integer> data;
        Tree pohonBinary;

        int totalTree = in.nextInt();
        
        for(int i=0 ;i<totalTree;i++){

            data = new ArrayList<>();
            pohonBinary = new Tree();

            int totalNode = in.nextInt();
            for(int j=0 ; j<totalNode; j++){
                data.add(in.nextInt());
            }

            pohonBinary.root = pohonBinary.constructBST(data, 0, data.size()-1);

            int totalremove = in.nextInt();
            for(int k=0 ; k<totalremove ; k++){
                if(in.next().equals("REMOVE")){
                    pohonBinary.remove(in.nextInt());
                }
            }
            out.println(pohonBinary.height(pohonBinary.root));
            // pohonBinary.inorderr();
            
        }
        out.close();
    }
    

    // taken from https://codeforces.com/submissions/Petr
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
 
        public String nextLine() throws IOException {
            return reader.readLine();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
 
    }
}