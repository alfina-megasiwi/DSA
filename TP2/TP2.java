import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

class TP2 {
    private static int N;
    private static NegaraApi negara;
    private static NegaraApi negara2;
    private static int jalan2;
    private static InputReader in = new InputReader(System.in);
    private static PrintWriter out = new PrintWriter(System.out);
    private static Map<Kota, Integer> visited = new HashMap<>();
    private static int count;
    private static int min = Integer.MAX_VALUE;
    public static void main(String[] args) {
        // initialize negara
        N = in.nextInt();
        negara = new NegaraApi();
        negara2 = new NegaraApi();
        for(int i = 0; i<N;i++){
            Kota tmp = new Kota(i);
            Kota tmp2 = new Kota(i);
            negara.getMapKota().put(i, tmp);
            negara2.getMapKota().put(i, tmp2);
            visited.put(tmp, 0);
        }

        int loop = in.nextInt();
        for(int i = 0 ; i<loop ; i++){
            query(in.next());            
        }

        out.close();
    }


    /*------------------------------------------------------ KUMPULAN METHOD ---------------------------------------------------------*/

    // Handling Query
    public static void query(String x){
        if(x.equals("INSERT")){
            int tem = in.nextInt();
            if(tem == 0){
                negara.INSERT(0, in.nextInt(), in.nextInt(), in.nextInt());
            }else if (tem == 1){
                negara.INSERT(tem, in.nextInt(), in.nextInt());
            }else{
                negara2.INSERT(tem, in.nextInt(), in.nextInt());
            }
        }else if(x.equals("DELETE")){
            negara.DELETE(in.nextInt(), in.nextInt(), in.nextInt());
        }else if(x.equals("SHORTEST_PATH")){
            int temp = in.nextInt();
            if(temp == 0){
                SHORTEST_PATH(temp, in.nextInt(), in.nextInt());
            }else{
                printAllPathsHelper(in.nextInt(), in.nextInt());
                if(min < 0 || min > Integer.MAX_VALUE-1){
                    out.println(-1);
                }else{
                    out.println(min);
                }
            }
            reset();
        }else if(x.equals("MIN_PATH")){
            MIN_PATH(in.nextInt(), in.nextInt());
            reset();
        }else if(x.equals("IS_CONNECTED")){
            IS_CONNECTED(in.nextInt(), in.nextInt());
            reset();
        }else if(x.equals("COUNT_CITY")){
            out.println(COUNT_CITY(in.nextInt(), in.nextInt()));
            reset();
        }else if(x.equals("COUNT_CONNECTED")){
            out.println(COUNT_CONNECTED());
            specialrestart(visited);
        }else{
            out.println(SIMULATE_WALK());
        }
    }

    // RESET
    public static void reset(){
        for(Integer city : negara.getMapKota().keySet()){
            negara.getKota(city).restart();
        }
    }

    // QUERY COUNT_CITY
    public static int COUNT_CITY(int source, int jarak){
        int temp = 0;
        Kota kotasource = negara.getKota(source);
        DIJKSTRA(0, kotasource);
        for(int kota : negara.getMapKota().keySet()){
            if(negara.getKota(kota).getDistance()<=jarak){
                temp++;
            }
        }
        return temp;
    }

    // QUERY SIMULATE_WALK
    public static int SIMULATE_WALK(){
        int source = in.nextInt();
        String how_many = in.next();
        double test1 = Double.valueOf(how_many);
        int test2 = (int)(test1-(Math.floor((test1/jalan2))*jalan2));
        for(int i = 0; i<test2; i++){
            Jalan tmp = negara2.getKota(source).getOneNeightbor();
            source = tmp.getD().getName();
        }
        return source;
    }

    // QUERY COUNT_CONNECTED
    public static int COUNT_CONNECTED(){
        for(Kota kota : visited.keySet()){
            if(visited.get(kota) == 0){
                DFS(kota);
                count++;
            }
        }
        return count;
    }
    public static void DFS(Kota kota){
        visited.put(kota, 1);
        for(String adj : kota.getNeighbor().keySet()){
            Jalan adjacent = kota.getNeighbor().get(adj);
            Kota thekota = adjacent.getD();
            if(adjacent.getTipe() == 0 && visited.get(thekota) == 0){
                DFS(thekota);
            }
        }
    }
    public static void specialrestart(Map<Kota, Integer> map){
        for(Kota kota : map.keySet()){
            map.put(kota, 0);
        }
        count = 0;
    }

    // QUERY MIN_PATH
    public static void MIN_PATH(int A, int B){
        DIJKSTRA(3, negara.getKota(A));
        if(negara.getKota(B).getDistance() == Double.MAX_VALUE){
            out.println("-1");
        }else{
            out.println((int)negara.getKota(B).getDistance());
        }
    }
    
    // QUERY IS_CONNECTED
    public static void IS_CONNECTED(int a, int b){
        Kota A = negara.getKota(a);
        Kota B = negara.getKota(b);
        DIJKSTRA(0, A);
        if(B.getDistance() == Double.MAX_VALUE){
            out.println("0");
        }else{
            out.println("1");
        }
    }

    // QUERY SHORTEST_PATH 0
    public static void SHORTEST_PATH(int tipe, int A, int B){
        DIJKSTRA(tipe, negara.getKota(A));
        if(negara.getKota(B).getDistance() == Double.MAX_VALUE){
            out.println("-1");
        }else{
            out.println((int)negara.getKota(B).getDistance());
        }
    }
    public static void DIJKSTRA(int tipe, Kota source){
        source.setDistance(0);
        PriorityQueue<Kota> gray = new PriorityQueue<>();
        gray.add(source);
        source.setVisited(true);
        source.setPredecessor(source);

        if(tipe == 0 || tipe == 3){
            while(!gray.isEmpty()){
                Kota minimum = gray.poll();
                for(String adj : minimum.getNeighbor().keySet()){
                    Jalan jalan = minimum.getNeighbor().get(adj);
                    Kota kotadj = jalan.getD();
                    if(!kotadj.isVisited() && jalan.getTipe() == 0){
                        double newdist;
                        if (tipe == 0){
                            newdist = minimum.getDistance() + minimum.getNeighbor().get(adj).getWeight();
                        }else{
                            newdist = minimum.getDistance() + minimum.getNeighbor().get(adj).getBfs();
                        }
                        if(newdist < kotadj.getDistance()){
                            gray.remove(kotadj);
                            kotadj.setDistance(newdist);
                            kotadj.setPredecessor(minimum);
                            gray.add(kotadj);
                        }
                    }
                }
                // masuk ke white cloud (udah visited)
                minimum.setVisited(true); 
            }
        }
    }

    // QUERY SHORTEST_PATH 1
    public static void printAllPathsHelper(int s, int d) { 
        Kota source = negara.getKota(s);
        Kota destination = negara.getKota(d);
        boolean[] isVisited = new boolean[N]; 
        ArrayList<Kota> pathList = new ArrayList<>(); 
        int cost = 0;
        int tipe = 0;
        pathList.add(source); 
          
        printAllPaths(source, destination, isVisited, pathList, cost, tipe); 
    } 
    private static void printAllPaths(Kota s, Kota d, boolean[] isVisited, List<Kota> localPathList, int cost, int tipe) { 
        isVisited[s.getName()] = true; 
        if (s==d){   
            if(min>cost && tipe<2){
                min = cost;
            }
            isVisited[s.getName()]= false; 
            return ; 
        } 
        for (String i : s.getNeighbor().keySet()){   
            Jalan jalan = s.getNeighbor().get(i);
            Kota kota = jalan.getD(); 
            
            if (!isVisited[kota.getName()]){ 
                tipe = tipe +jalan.getTipe();
                cost = cost + jalan.getWeight();
                localPathList.add(kota); 
                printAllPaths(kota, d, isVisited, localPathList, cost, tipe); 
                localPathList.remove(kota);
                cost = cost - jalan.getWeight();
                tipe = tipe - jalan.getTipe();
            } 
        } 
        isVisited[s.getName()] = false; 
    }


    /*------------------------------------------------------ KUMPULAN CLASS ---------------------------------------------------------*/

    // NEGARA == GRAPH
    static class NegaraApi{
        private Map<Integer, Kota> kotas;

        public NegaraApi(){
            this.kotas = new HashMap<Integer, Kota>();
        }
        public Map<Integer, Kota> getMapKota(){
            return this.kotas;
        }
        public Kota getKota(int nomorkota){
            return this.kotas.get(nomorkota);
        }
        public void INSERT(int tipe, int s, int d, int w){
            Kota source = this.kotas.get(s);
            Kota destination = this.kotas.get(d);
            Jalan tmpJalan1 = new Jalan(tipe, w, source, destination);
            Jalan tmpJalan2 = new Jalan(tipe, w, destination, source);
            source.addNeighbor(tipe, s, d, tmpJalan1);
            destination.addNeighbor(tipe, d, s, tmpJalan2);
        }
        public void INSERT(int tipe, int s, int d){
            if(tipe == 1){
                Kota source = this.kotas.get(s);
                Kota destination = this.kotas.get(d);
                Jalan tmpJalan1 = new Jalan(tipe, source, destination);
                Jalan tmpJalan2 = new Jalan(tipe, destination, source);
                source.addNeighbor(tipe, s, d, tmpJalan1);
                destination.addNeighbor(tipe, d, s, tmpJalan2);
                source.setKhusus(true);
                destination.setKhusus(true);
            }else{
                Kota source = this.kotas.get(s);
                Kota destination = this.kotas.get(d);
                Jalan tmpJalan = new Jalan(tipe, source, destination);
                source.setOneNeightbor(tmpJalan);
                jalan2++;
            }
               
        }

        public void DELETE(int tipe, int s, int d){
            Kota tmp1 = this.kotas.get(s);
            Kota tmp2 = this.kotas.get(d);
            if(tipe == 0 || tipe == 1){
                tmp1.REMOVE(tipe, s, d);
                tmp2.REMOVE(tipe, d, s);
            }else{
                tmp1.REMOVE(tipe, s, d);
            }
        }
        public void printit(){
            for(Integer city : this.kotas.keySet()){
                out.println(this.kotas.get(city));
                
            }
        }
    }

    // KOTA == Vertex
    static class Kota implements Comparable<Kota>{
        private int nomorKota;
        private Map<String, Jalan> neighbor;
        private Jalan one_neighbor;
        private boolean visited;
        private Kota predecessor;
        private double distance = Double.MAX_VALUE;
        private boolean khusus;

        public Kota(int nomorKota){
            this.nomorKota = nomorKota;
            this.neighbor = new HashMap<>();
        }
        public void addNeighbor(int tipe, int s, int d, Jalan jalan){
            this.neighbor.put(tipe+"-"+s+"-"+d, jalan);
        }
        public void REMOVE(int tipe, int s, int d){
            if(tipe == 0 || tipe == 1){
                this.neighbor.remove(tipe+"-"+s+"-"+d);
            }else{
                this.one_neighbor = null;
            }
        }
        public int getName(){
            return this.nomorKota;
        }
        public Map<String, Jalan> getNeighbor(){
            return neighbor;
        }
        public boolean isVisited(){
            return this.visited;
        }
        public void setVisited(boolean apa){
            this.visited = apa;
        }
        public void setKhusus(boolean bulean){
            this.khusus = bulean;
        }
        public boolean getKhusus(){
            return this.khusus;
        }
        public Kota getPredecessor(){
            return this.predecessor;
        }
        public void setPredecessor(Kota kota){
            this.predecessor = kota;
        }
        public double getDistance(){
            return this.distance;
        }
        public void setDistance(double distance){
            this.distance = distance;
        }
        public void restart(){
            this.distance = Double.MAX_VALUE;
            this.setPredecessor(null);
            this.setVisited(false);
            min = Integer.MAX_VALUE;
        }
        public void setOneNeightbor(Jalan jalan){
            this.one_neighbor = jalan;
        }
        public Jalan getOneNeightbor(){
            return this.one_neighbor;
        }
        public int compareTo(Kota other){
            return Double.compare(this.distance, other.distance);
        }
        public String toString(){
            return String.valueOf(this.getName());
        }
    }

    // JALAN == EDGE
    static class Jalan{
        private int tipe;
        private int weight;
        private Kota s;
        private Kota d;
        private int bfs = 1;

        public Jalan(int tipe, int weight, Kota s, Kota d){
            this.tipe = tipe;
            this.weight = weight;
            this.s = s;
            this.d = d;
        }
        public Jalan(int tipe, Kota s, Kota d){
            this.tipe = tipe;
            this.s = s;
            this.d = d;
            this.weight = 0;
        }
        public int getWeight(){
            return this.weight;
        }
        public Kota getS(){
            return this.s;
        }
        public Kota getD(){
            return this.d;
        }
        public int getTipe(){
            return this.tipe;
        }
        public int getBfs(){
            return this.bfs;
        }
        public String toString(){
            return "(" + this.s.getName() + " " +this.d.getName() + " " + this.weight + ")";
        }
    }
    
    // for input and output
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
