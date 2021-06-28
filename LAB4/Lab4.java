import java.util.*;
import java.io.*;


/**
 * Silahkan lengkapi class MinHeap berikut untuk mempermudah pengerjaan soal
 * Silahkan menambahkan method lain dalam class jika dirasa perlu
 */
class MinHeap {
    List<Integer> heap;
    int dompet = 0;
    /**
     * Constructor untuk class MinHeap
     */
    
    public MinHeap() {
        heap = new ArrayList<Integer>();
    }

    /**
     * Mengembalikan parent dari suatu indeks pada heap
     * @param n : sebuah indeks padah heap
     * @return parent dari indeks n
     */
    private int parent(int n) {
        return (n - 1) / 2;
    }

    private int leftChild(int n){
        return 2*n+1;
    }

    private int rightChild(int n){
        return 2*(n+1);
    }

    /**
     * Mengecek apakah heap kosong
     * @return true jika heap kosong, false jika tidak
     */
    public boolean isEmpty() {
        // TODO : Implementasi method ini
        return heap.size() == 0;
    }
    
    
    /**
     * Menambahkan suatu nilai ke dalam heap. Setelah nilai dimasukkan, properti dari heap tetap terjaga
     * @param val : nilai yang ingin ditambhakan ke dalam heap
     */
    public void add(int val) {
        // TODO : Implementasi method ini
        heap.add(val);
        // percolateUp();
        percolateUp(heap.size()-1);
    };
    

    
    /**
     * Mengembalikan elemen minimum pada heap TANPA MENGELUARKAN elemen tersebut dari heap
     * @return elemen minimum pada heap
     */
    public int peek() {
        // TODO : Implementasi method ini
        return heap.get(0);
    }
    
    
    /**
     * Mengembalikan elemen minimum pada heap SEKALIGUS MENGELUARKAN elemen tersebut dari heap
     * @return elemen minimum pada heap yang dikeluarkan
     */
    public int poll() {
        // TODO : Implementasi method ini
        int first = peek();
        int tmp = heap.get(heap.size()-1);
        heap.set(0, tmp);
        heap.remove(heap.size()-1);
        
        if(heap.size()>1){
            percolateDown(0);

        }
        return first;
        
    }

    // from slide 
    public void percolateUp(int leaf){
        int parent = parent(leaf);
        int value = heap.get(leaf);
        while(leaf>0 && value<heap.get(parent)){
            heap.set(leaf, heap.get(parent));
            leaf = parent;
            parent = parent(leaf);
        }
        heap.set(leaf, value);
    }

    // from slide 
    public void percolateDown(int root){
        int heapSize = heap.size();
        int value = heap.get(root);
        while(root<heapSize){
            int childpos = leftChild(root);
            if(childpos <heapSize){
                if(rightChild(root)<heapSize && heap.get(childpos+1)<heap.get(childpos)){
                    childpos++;
                }
                if(heap.get(childpos)<value){
                    heap.set(root, heap.get(childpos));
                    root = childpos;
                }else{
                    heap.set(root, value);
                    return;
                }
            }else{
                heap.set(root, value);
                return;
            }
        }
    }
}

public class Lab4 {
    static InputReader in = new InputReader(System.in);
    static PrintWriter out = new PrintWriter(System.out);
    
    public static void main(String[] args) {
        int N = in.nextInt();
        List<Integer> T = new ArrayList<>();
        List<Integer> A = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            T.add(in.nextInt());
        }
        for (int i = 0; i < N; i++) {
            A.add(in.nextInt());
        }

        // TODO : Implementasikan solusi disini
        int howmuch = 0;
        int dompet = 0;
        MinHeap heapsort = new MinHeap();
        for(int i = 0; i<T.size(); i++){
            
            if(T.get(i)==0){
                heapsort.add(A.get(i));
            }else{
                dompet = A.get(i);
                while(!heapsort.isEmpty() && dompet>= heapsort.peek()){
                    dompet = dompet - heapsort.peek();
                    heapsort.poll();
                    howmuch++;
                }

            } 
        }
        
        out.println(howmuch);
        out.close();
    }
    
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

        public long nextLong() {
            return Long.parseLong(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

    }
}