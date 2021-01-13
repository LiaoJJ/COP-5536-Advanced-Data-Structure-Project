/**

 * a Min Heap

 * @author Jiajing Liao

 * @Time Nov 16, 2019

 *

 */


import java.util.Random;
/*
 * HeapNode is a class for Node in the Heap
 * it has 4 fields:
 * num,
 * eTime is extended time,
 * tTime is total time
 * link is a variable point to a RedBlack Tree
 */
class HeapNode{
    int num;
    int eTime;
    int tTime;
    TreeNode link;
    public HeapNode(){
        num = 0;
        eTime = 0;
        tTime = 0;
        link = null;
    }
    public HeapNode(int num, int eTime, int tTime, TreeNode link){
        this.num = num;
        this.eTime = eTime;
        this.tTime = tTime;
        this.link = link;
    }
}
/*
 *  class MinHeap define a minHeap, implemented by array
 * 2 construction functions
 * heapify() fix a single error point in the heap from top to down
 * swap() can swap 2 node in the array
 * compare will first compare eTime, then compare num if the eTime is the same
 * print() and println() is for debug
 * insertEnd(), updateMinBy1(), getMin() is for debug, it's not used in current version
 * insert() can insert a node into the Heap, then fix the error in the heap, from bottom to top
 * extractMin(): pop the min element, and then use heapify() to fix the heap
 * toString(): for debug, output the entire heap into a String Tree
 * charA2Str(): for debug, output a single node to String Node
 * check(): for debug, check the correctness of entire tree
 * size: size of Heap
 * heap: an array to store the HeapNode
 */
public class MinHeap {

    // size of Heap
    int size;
    // an array to store the HeapNode
    HeapNode[] heap;
    //2 construction functions
    public MinHeap(){
        heap = new HeapNode[2001];
        size = 0;
    }
    public MinHeap(int n){
        heap = new HeapNode[n+1];
        size = 0;
    }
    //heapify() fix a single error point in the heap from top to down
    public void heapify(int node){
        if(this.size==0) return;
        int l = left(node);
        int r = right(node);
        int smaller = node;
        if(l<size && compare(l, smaller)<0){
            smaller = l;
        }
        if(r<size && compare(r, smaller)<0){
            smaller = r;
        }
        if(smaller!=node){
            swap(node, smaller);
            heapify(smaller);
        }
    }
    //swap() can swap 2 node in the array
    public void swap(int i1, int i2){
        HeapNode temp = heap[i1];
        heap[i1] = heap[i2];
        heap[i2] = temp;
    }
    //compare will first compare eTime, then compare num if the eTime is the same
    public int compare(int i1, int i2){
        if(heap[i1].eTime!=heap[i2].eTime){
            return heap[i1].eTime - heap[i2].eTime;
        }
        return heap[i1].num - heap[i2].num;
    }
    //print() and println() is for debug
    private static void print(String s){
        System.out.print(s);
    }
    private static void println(String s){
        System.out.println(s);
    }
    //insertEnd(), updateMinBy1(), getMin() is for debug, it's not used in current version
    public void insertEnd(HeapNode temp){
        int node = size;
        heap[size] = temp;
        size++;
    }
    //insert() can insert a node into the Heap, then fix the error in the heap, from bottom to top
    public void insert(HeapNode temp){
//        System.out.println("insert "+charA2Str(temp));
        if(temp==null) return;
        int node = size;
        heap[size] = temp;
        size++;
        if(size>2000) System.out.println("Size overflow");

        while(node>0){
            int p = parent(node);
            if(compare(node, p)<0){
                swap(node, p);
                node = p;
            }
            else{
                break;
            }
        }
    }
    //insertEnd(), updateMinBy1(), getMin() is for debug, it's not used in current version
    public HeapNode updateMinBy1(){
        if(size==0) return null;
        HeapNode min = heap[0];
        min.eTime +=1;
        if(min.eTime==min.tTime){
            extractMin();
            return min;
        }
        heapify(0);
        return min;
    }

    public HeapNode getMin(){
        if(size==0) return null;
        return heap[0];
    }
    //extractMin(): pop the min element, and then use heapify() to fix the heap
    public HeapNode extractMin(){
        if(size==0) return new HeapNode();

//        System.out.println("Extract Min = "+charA2Str(heap[0]));
        swap(0, size-1);
        size--;
        heapify(0);
        return heap[size];
    }
    public HeapNode extractMin(HeapNode node){
        if(size==0) return new HeapNode();

        int index = 0;
        for(; index<size; index++){
            if(heap[index]==node) break;
        }
        swap(index, size-1);
        size--;
        heapify(index);
        return heap[size];
    }
    //toString(): for debug, output the entire heap into a String Tree
    public String toString(){
        int begin = 0;
        int len = 1;
        String out = "";
        while(begin<size){
            for(int i=0; i<len && begin+i<size; i++){
                out+=charA2Str(heap[begin+i])+" ";
            }
            out+="\n";
            begin+=len;
            len*=2;
        }
        return out;
    }
    public String toString(int n){
        int begin = 0;
        int len = 1;
        String out = "";
        while(begin<n){
            for(int i=0; i<len && begin+i<size; i++){
                out+=charA2Str(heap[begin+i])+" ";
            }
            begin+=len;
            len*=2;
        }
        return out;
    }
    //charA2Str(): for debug, output a single node to String Node
    public String charA2Str(HeapNode a){
        return "("+a.num+","+a.eTime+","+a.tTime+")";
    }
    //check(): for debug, check the correctness of entire tree
    public boolean check(int node){
        int l = left(node);
        if(l<size){
            if(compare(l, node)<0) return false;
            if(check(l)==false) return false;
        }

        int r = right(node);
        if(r<size){
            if(compare(r, node)<0) return false;
            if(check(r)==false) return false;
        }

        return true;
    }
    // for debug, generate a random array, to insert and delete
    private static void swapReference(int[] a, int i, int j){
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
    private static Random rand = new Random(37);
    public static int randInt(int i, int j){
        return rand.nextInt(j-i+1) + i;
    }
    public static int[] randArr(int N){
        int[] a = new int[N];
        for(int i = 1; i <= N; i++)
            a[i-1] = i;
        for(int i = 1; i < N; i++)
            swapReference(a, i, randInt(0, i));
        return a;
    }
    public static void showArray(String prefix, int[] arr){
        System.out.print(prefix);
        for(int i: arr) System.out.print(i+" ");
        System.out.println();
    }
    // test function, test by random array
    public static void test(){
        for(int iter = 0; iter<1000; iter++){
            MinHeap heap = new MinHeap(2001);
            System.out.println("################TEST "+iter+"################");
            int[] inputSeq = randArr(2000);

//            showArray("inputSeq: ", inputSeq);

            System.out.print("insert: ");
            int n = inputSeq.length;
            HeapNode[] nodes = new HeapNode[2001];
            for(int i: inputSeq){
                nodes[i] = new HeapNode(0,i,0,null);
                heap.insert(nodes[i]);
                System.out.print(i+" ");
            }
            System.out.println();

            System.out.print("Extract Min: ");
            for(int i=0; i<2050; i++){
                HeapNode temp = heap.extractMin();
                System.out.print(heap.heap[0].eTime+" ");
            }
            System.out.println();
            System.out.println("size " + heap.size);
            boolean res = heap.check(0);
            System.out.println("check " + res);
            if(!res){
                System.out.println("==================false detect================");
                return;
            }
            System.out.println(" ");
        }
    }
    // main function for MinHeap
    public static void main(String[] args){
        test();
    }
    //left, get the left child index of current index
    public int left(int index){
        return index*2+1;
    }
    //right, get the right child index of the current index
    public int right(int index){
        return index*2+2;
    }
    // get the parent index of current index
    public int parent(int index){
        return (index-1)/2;
    }
}