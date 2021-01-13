/**

 * risingCity for the Project 1 of ADS

 * @author Jiajing Liao

 * @Time Nov 16, 2019z

 *

 */


import java.io.*;
import java.util.ArrayList;
import java.util.List;

// this is the main body for the project of ADS
// which contains Logic of building.
public class risingCity {

    // global counter, the "day"
    private static int counter = 0;
    // current active HeapNode
    private static HeapNode aHeapNode = null;
    // current active TreeNode
    private static TreeNode aTreeNode = null;
    // a MinHeap for extracting minimal executedTime
    private static MinHeap heap = new MinHeap(2001);
    // a RBT for search, printbuilding
    private static RedBlackTree tree = new RedBlackTree();
    // the String to write
    private static String out = "";
    // the current number of day working on the active building
    private static int workDay = 0;

    // main function
    public static void main(String[] args) throws IOException {
        int n = args.length;
        for(String s: args){
            println(s);
        }
        // read input data
        int[][] in = read(args);
        int index = 0;

        // main process
        while(aHeapNode!=null || heap.size>0 || counter<=in[in.length-1][0]){

            process();

            // if there is a input at this day
            if(index<in.length && counter==in[index][0]){
                if(in[index][1]==0){ // insert, then process
                    TreeNode temp2 = new TreeNode(in[index][2], in[index][3], in[index][4]);
                    HeapNode temp1 = new HeapNode(in[index][2], in[index][3], in[index][4], temp2);
                    heap.insert(temp1);
                    if(tree.insert(temp2)){
                        println("num duplicate error, progress stop");
                        break;
                    }
                }
                else if(in[index][1]==1){
                    out += tree.find(tree.root, in[index][2])+"\n";
                }
                else if(in[index][1]==2){
                    String temp= tree.find(tree.root, in[index][2], in[index][3]);
                    if(!temp.equals("")) out+=temp.substring(0, temp.length()-1)+"\n";
                    else out+="(0,0,0)\n";
                }

                index++;
            }

            // if this building is finished
            if(aHeapNode!=null && aHeapNode.eTime==aHeapNode.tTime){
//                println("building finish update");
                workDay = 0;
                out += addBracket(aHeapNode.num, counter);
                aHeapNode = null;
                tree.delete(aTreeNode);
                updateActiveNode(heap);
            }
            // if they have been working on the current active building for 5 days
            if(workDay==5){
//                println("workday = 5 update");
                workDay = 0;
                updateActiveNode(heap);
            }

            counter++;
//            println("=====================");
        }
//        out += "("+(counter-1)+")";

        if(out.length()>1) out = out.substring(0, out.length()-1);
        println("result:");
        print(out);
        write("./output_file.txt", out);
    }

    // for file writing
    public static void write(String filename, String out) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(out);

        writer.close();
    }

    // the every day process
    public static void process(){
        if(heap.size==0 && aHeapNode==null) {
            return;
        }
        if(aHeapNode==null) updateActiveNode(heap);

        workDay++;
        aHeapNode.eTime++;
        aTreeNode.eTime++;
        tree.delete(aTreeNode);
        tree.insert(aTreeNode);

//        println("Counter = "+counter);
//        println("workday = "+workDay);
//        println("aHeapNode " + heap.charA2Str(aHeapNode));
    }
    // update active building, every 5 days, or when a building is finished
    public static void updateActiveNode(MinHeap heap){
        if(aHeapNode!=null){
            heap.insert(aHeapNode);
        }
        if(heap.size>0){
            aHeapNode = heap.extractMin();
            aTreeNode = aHeapNode.link;
        }
    }

    // for String output
    private static String addBracket(int a, int b){
        return "("+a+","+b+")\n";
    }
    private static String addBracket(int a, int b, int c){
        return "("+a+","+b+","+c+")\n";
    }

    /**
     * function: read input file
     *
     * @param filePath
     * @return List<String> for each line of file
     */
    public static List<String> readTxtFileIntoStringArrList(String filePath)
    {
        List<String> list = new ArrayList<String>();
        try
        {
            String encoding = "GBK";
            File file = new File(filePath);
            if (file.isFile() && file.exists())
            { // if file exist
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);// for encoding
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;

                while ((lineTxt = bufferedReader.readLine()) != null)
                {
                    list.add(lineTxt);
                }
                bufferedReader.close();
                read.close();
            }
            else
            {
                System.out.println("file not found");
            }
        }
        catch (Exception e)
        {
            System.out.println("file error");
            e.printStackTrace();
        }

        return list;
    }
    // for test
    public static void test(){

    }
    // for debug, print
    private static void print(String s){
        System.out.print(s);
    }
    private static void println(String s){
        System.out.println(s);
    }
    // change the String input to int[], easy to process
    public static int[][] read(String[] args){
        List<String> sentenseInputs = readTxtFileIntoStringArrList(args[0]);
        List<int[]> inputs = new ArrayList<>();
        for(String oriInput: sentenseInputs){
            oriInput = oriInput.replace(": ", " ");
            oriInput = oriInput.replace("(", " ");
            oriInput = oriInput.replace(",", " ");
            oriInput = oriInput.replace(")", " ");
            String[] sp = oriInput.split(" ");
            if(sp.length==4 && sp[1].equals("Insert")){
                int ctime = Integer.parseInt(sp[0]);

                int num = Integer.parseInt(sp[2]);
                int etime = 0;
                int ttime = Integer.parseInt(sp[3]);
                inputs.add(new int[]{ctime, 0, num, etime, ttime});
            }
            else if(sp.length==4 && (sp[1].equals("PrintBuliding") || sp[1].equals("PrintBuilding"))){
                int ctime = Integer.parseInt(sp[0]);

                int r1 = Integer.parseInt(sp[2]);
                int r2 = Integer.parseInt(sp[3]);
                inputs.add(new int[]{ctime, 2, r1, r2});
            }
            else if(sp.length==3 && (sp[1].equals("PrintBuliding") || sp[1].equals("PrintBuilding"))){
                int ctime = Integer.parseInt(sp[0]);

                int r1 = Integer.parseInt(sp[2]);
                inputs.add(new int[]{ctime, 1, r1});
            }
            else{
                print("error, file input error");
                while(true);
            }
        }
        return inputs.toArray(new int[inputs.size()][]);
    }
}
