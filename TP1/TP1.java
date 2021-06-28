import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

class Solution{
    private static InputReader in = new InputReader(System.in);
    private static PrintWriter out = new PrintWriter(System.out);
    private static Map<String, Material> material = new LinkedHashMap<>();
    private static Map<String, Pabrik> pabrik = new LinkedHashMap<>();
    private static List<Vaksin> semuaVaksin = new ArrayList<>();
    private static List<Vaksin> bankVaksin = new ArrayList<>();
    private static List<Integer> weight = new ArrayList<>();
    private static List<Integer> value = new ArrayList<>();
    private static int N = 0;
    private static int NN = 0;
    private static int P = 0;
    private static int H = 0;
    

    public static void main(String[] args) {
        // TOTAL NUMBER OF MATERIAL
		N = in.nextInt();

        // ASSIGN MATERIAL AND ITS QUALITY
        for(int i = 0;i<N;i++){
            String cekcek = in.next();
            material.put(cekcek, new Material(in.nextInt()));

        }

        // TOTAL NUMBER OF FACTORY
		P = in.nextInt();

        NN = material.size();

        // ASSIGN FACTORY AND ITS MATERIAL
        for(int i = 0;i<P;i++){
            String tempname = in.next();
            Pabrik tmp = new Pabrik(material);
            for(int j = 0;j<NN;j++){
                tmp.setMaterial(in.next(), in.nextInt());
            }
            pabrik.put(tempname, tmp);
        }
        
        // TOTAL NUMBER OF DAY
        H = in.nextInt();

        // QUERY PROCESS
        for(int i = 0;i<H;i++){
            int jj = in.nextInt();
            for(int j = 0;j<jj;j++){
                String test = in.next();
                query(test);
            }
            out.println("Hari ke-"+(i+1)+": "+ semuaVaksin.size() + " "+ bankVaksin.size());

        }
        
        // LAST QUERY
        query(in.next());

        // CLOSE
        out.close();

    }
    
    
    /* ============================================ COLLECTION OF METHOD ================================================= */

    // for handling query
    static void query(String x){
        if(x.equals("RESTOCK")){
            RESTOCK(in.next(), in.next(), in.nextInt());
        }else if(x.equals("PRODUCE")){
            PRODUCE(in.next(), in.next());
        }else if(x.equals("DISTRIBUTE")){
            DISTRIBUTE(in.next(), in.nextInt());
        }else if(x.equals("CEK_KINERJA_PABRIK")){
            CKP();
        }else if(x.equals("CEK_SEMUA_VAKSIN")){
            CSV();
        }else if(x.equals("CEK_TOTAL_KUALITAS_UJICOBA")){
            out.println(KS(weight.size(), in.nextInt()));

        }
    }

    // restock query
    static void RESTOCK(String S, String M, int Y){
        pabrik.get(S).getMaterial().get(M).addQuantity(Y);
    }

    // produce query
    static void PRODUCE(String S, String V){
        Map<String, Material> thematerial = pabrik.get(S).getMaterial();

        int[] tempint = new int[NN];
        int totalkualitas = 0;
        int biayaujicoba = 0;

        boolean cek = false;
        
        int index = 0;
        for(String i : material.keySet()){
            int Ai = in.nextInt();
            if(cek!=true){
                Material tmp = thematerial.get(i);
                if(tmp.getQuantity()<Ai){
                    cek=true;
                }else{
                    tempint[index]=Ai;
                    index++;
                }
            }
        }

        int index2 = 0;
        if(cek!=true){
            for(String i : material.keySet()){
                Material tmp2 = thematerial.get(i);
                tmp2.setFormula(tempint[index2]);
                totalkualitas += tmp2.getFormula()*tmp2.getQuality();
                biayaujicoba += tmp2.getFormula();
                tmp2.decQuantity(tempint[index2]);
                index2++;
            }

            totalkualitas = (totalkualitas%1000)+1;
            Vaksin temp = new Vaksin(V,totalkualitas,biayaujicoba);
            pabrik.get(S).addtoGudang(temp);
            pabrik.get(S).jmlvaksinAdder();
            semuaVaksin.add(temp);
        }
        
    }

    // distribute query
    static void DISTRIBUTE(String S, int Z){
        Queue<Vaksin> tmpabrik = pabrik.get(S).getGudang();
        
        int temploop = 0;
        if(tmpabrik!=null){
            if(Z>tmpabrik.size()){
                temploop = tmpabrik.size();
            }else{
                temploop = Z;
            }

            for (int i = 0 ;i<temploop;i++){
                Vaksin vaccine = tmpabrik.poll();
                bankVaksin.add(vaccine);
                vaccine.setStatus("TRUE");
                pabrik.get(S).jmldistribusiAdder();
    
                // for knapsack :
                weight.add(vaccine.getBiaya());
                value.add(vaccine.getKualitas());
            }
        }
        

        
    }

    // query CEK_KINERJA_PABRIK
    static void CKP(){
        for (String i : pabrik.keySet()){
            out.println(i +" "+ pabrik.get(i).getCKP());
        }
    }

    // query CEK_SEMUA_VAKSIN
    static void CSV(){
        sort();
        for(Vaksin i : semuaVaksin){
            out.println(i.getCSV());
        }
    }

    /* ================ FOR SORTING VAKSIN BASED ON [1. Quality] [2. Cost Production] [3. Vaccine's name] ================ */

    static void sort(){
        List<Vaksin> vaksins = new ArrayList<>();
        mergeSort(vaksins, 0, semuaVaksin.size()-1);
    }
    
    static void mergeSort(List<Vaksin> arr, int loweridx, int upperidx){
        if(loweridx == upperidx){
            return; 

        }else{
            int middleidx = (loweridx+upperidx)/2;
            mergeSort(arr, loweridx, middleidx);
            mergeSort(arr, middleidx+1, upperidx);
            merge(arr, loweridx,middleidx+1,upperidx);
        }
    }

    static void merge(List<Vaksin> arr, int loweridxcursor, int higheridx, int upperidx){
        arr = new ArrayList<>();
        int loweridx = loweridxcursor;
        int mididx = higheridx-1;
        int total = upperidx-loweridx+1;

        while(loweridx<=mididx && higheridx<=upperidx){
            // sort by quality
            if(semuaVaksin.get(loweridx).getKualitas() > semuaVaksin.get(higheridx).getKualitas()){
                arr.add(semuaVaksin.get(loweridx));
                loweridx++;
            }else if(semuaVaksin.get(loweridx).getKualitas() == semuaVaksin.get(higheridx).getKualitas()){
                // sort by the cost of vaccine production
                if(semuaVaksin.get(loweridx).getBiaya() < semuaVaksin.get(higheridx).getBiaya()){
                    arr.add(semuaVaksin.get(loweridx));
                    loweridx++;
                }else if(semuaVaksin.get(loweridx).getBiaya() == semuaVaksin.get(higheridx).getBiaya()){
                    // sort by lexicography
                    if(semuaVaksin.get(loweridx).getName().compareTo(semuaVaksin.get(higheridx).getName())<0){
                        arr.add(semuaVaksin.get(loweridx));
                        loweridx++;
                    }else{
                        arr.add(semuaVaksin.get(higheridx));
                        higheridx++;
                    }
                }else{
                    arr.add(semuaVaksin.get(higheridx));
                    higheridx++;
                }
            }else{
                arr.add(semuaVaksin.get(higheridx));
                higheridx++;
            }
        }
        while(loweridx<=mididx){
            arr.add(semuaVaksin.get(loweridx));
            loweridx++;
        }
        while(higheridx<=upperidx){
            arr.add(semuaVaksin.get(higheridx));
            higheridx++;
        }
        for(int i = 0; i<total;i++){
            semuaVaksin.set(loweridxcursor+i, arr.get(i));
        }
    }

    /* =============================== CEK_KUALITAS_UJICOBA USING KNAPSACK ALGORITHM  ==================================== */

    static int KS(int n, int C){
        if (n <= 0 || C <= 0) {
            return 0;
        }

        // table for dynammic programming (including 0 fund)
        int[][] table = new int[n+1][C+1];
        for (int j = 0; j <= C; j++) {
            table[0][j] = 0;
        }
    
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= C; j++) { 
                if(weight.get(i-1) > j) {
                    table[i][j] = table[i - 1][j];
                }else{
                    table[i][j] = Math.max(value.get(i-1) + table[i - 1][j - weight.get(i-1)], table[i - 1][j]);
                }
            }
        }

        // maximum value always in last column and row
        return table[n][C];
        }

    /* ============================================ COLLECTION OF CLASS ================================================= */
    
    // class for vaccine
    static class Vaksin{
        private String name;
        private int kualitas = 0;
        private int biaya = 0;
        private String status;

        public Vaksin(String name, int kualitas, int biaya){
            this.name = name;
            this.kualitas = kualitas;
            this.biaya = biaya;
        }
        public String getName(){
            return this.name;
        }
        public int getKualitas(){
            return this.kualitas;
        }
        public int getBiaya(){
            return this.biaya;
        }
        public void setStatus(String status){
            this.status=status;
        }
        public String getCSV(){
            if(this.status==null){
                this.status = "FALSE";
            }
            return this.name + " "+ this.kualitas+ " "+this.biaya+ " "+this.status;
        }
        public String toString(){
            return this.name;
        }
    }

    // class for material
    static class Material{
        private int quality = 0;
        private int quantity = 0;
        private int formula = 0;

        public Material(int quality) {
            this.quality = quality;
        }
        public int getQuality(){
            return this.quality;
        }
        public int getQuantity(){
            return this.quantity;
        }
        public int getFormula(){
            return this.formula;
        }
        public void setQuantity(int quantity){
            this.quantity=quantity;
        }
        public void addQuantity(int adder){
            this.quantity = this.quantity+adder;
        }
        public void decQuantity(int decrement){
            this.quantity = this.quantity-decrement;
        }
        public void setFormula(int formula){
            this.formula=formula;
        }
        public String toString(){
            return Integer.toString(quantity);
        }
    }

    // class for factory
    static class Pabrik {
        private Map<String, Material> materiald = new LinkedHashMap<>();
        private Map<String, Material> material = new LinkedHashMap<>();
        private Queue<Vaksin> gudang = new LinkedList<>();
        private int jmlvaksin = 0;
        private int jmldistribusi = 0;

        public Pabrik(Map<String, Material> materiald) {
            if(materiald!=null){
                this.materiald = materiald;
            }else{
                this.materiald = null;
            }
        }
        public Map<String, Material> getMaterial(){
            if(this.material!=null){
                return this.material;
            }else{
                return null;
            }
        }
        public void setMaterial(String name, int quantity){
            int quality = materiald.get(name).getQuality();
            Material materialtmp = new Material(quality);
            materialtmp.setQuantity(quantity);
            this.material.put(name, materialtmp);
        }
        public void jmlvaksinAdder(){
            this.jmlvaksin++;
        }
        public void jmldistribusiAdder(){
            this.jmldistribusi++;
        }
        public void addtoGudang(Vaksin vaksin){
            this.gudang.add(vaksin);
        }
        public Queue<Vaksin> getGudang(){
            if(this.gudang!= null){
                return this.gudang;
            }else{
                return null;
            }

        }
        public void TestPrint(){
            for(String aaa : material.keySet()){
                out.println(aaa + " quantity " + material.get(aaa).getQuantity()+ " quality " + material.get(aaa).getQuality());
            }
        }

        // untuk query CEK_KINERJA_PABRIK
        public String getCKP(){
            return this.jmlvaksin + " " + this.jmldistribusi;
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