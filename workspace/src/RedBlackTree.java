/**

 * a Red Black Tree

 * @author Jiajing Liao

 * @Time Nov 16, 2019

 *

 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// the class of Red Black Tree
public class RedBlackTree {
    // root node
    TreeNode root;
    // size of tree
    int size;
    // construction function
    public RedBlackTree(){
        root = null;
        size = 0;
    }
    // provide help for String
    private static String addBracket(int a, int b){
        return "("+a+","+b+")";
    }
    private static String addBracket(int a, int b, int c){
        return "("+a+","+b+","+c+")";
    }

    // find the single node in the Tree, return the node if found, else return the (0,0,0)
    public static String find(TreeNode node, int r1){
        if(node==null) return "(0,0,0)";

        if(node.num<r1){
            return find(node.right, r1);
        }
        else if(node.num>r1){
            return find(node.left, r1);
        }
        return addBracket(node.num, node.eTime, node.tTime);
    }

    // range query in the tree, r1 is the left bound, r2 is the right bound
    public static String find(TreeNode node, int r1, int r2){
        String out = "";
        if(node==null) return out;
        if(node.num >= r1) {
            out += find(node.left, r1, r2);
        }
        if(node.num>=r1 && node.num<=r2){
            out += addBracket(node.num, node.eTime, node.tTime)+",";
        }
        if(node.num<=r2){
            out += find(node.right, r1, r2);
        }
        return out;
    }
    // compare node by num
    public int compare(TreeNode n1, TreeNode n2){
        return n1.num - n2.num;
    }

    // delete a node
    public void delete(TreeNode node){
//        println("deleting..."+node2Str(node));
//        println(this.toString());
        if(node==null) return;
        size--;
        // 0 or 1 degree
        if(node.left==null || node.right==null){
            TreeNode py = null;
            TreeNode y = null;
            if(node==this.root){
                this.root = (node.left==null)?node.right:node.left;
                if(this.root !=null) {
                    this.root.color = Color.black;
                    this.root.parent = null;
                }
//                node.left = null;
//                node.right = null;
//                node.parent = null;
//                return;
            }
            else{
                py = node.parent;
                y = (node.left==null)?node.right:node.left;
                if(y!=null) y.parent = py;
                if(py.left==node){
                    py.left = y;
                }
                else {
                    py.right = y;
                }
            }

            node.left = null;
            node.right = null;
            node.parent = null;
            // deal with deficiency
            // red, done
            if(node.color==Color.red) return;

            // deficiency flip
            deleteFlip(py, y);
        }
        // 2 degree
        else{
            TreeNode leftBig = findLeftBig(node);
            swap(leftBig, node);
            delete(node);
            size++;
        }
    }

    // fix the delete deficiency
    public void deleteFlip(TreeNode py, TreeNode y){
        if(y==this.root) return;
        if(py==null) return;
        if(y!=null && y.color==Color.red) {
            y.color = Color.black;
            return;
        }

        TreeNode v = (y==py.left)?py.right:py.left;
        TreeNode a = v.left;
        TreeNode b = v.right;
        // b
        if(v.color==Color.black){
            // Xb0
            if((a==null || a.color==Color.black) && (b==null || b.color==Color.black)){
                //case 1
                if(py.color==Color.black){
                    v.color = Color.red;
                    deleteFlip(py.parent, py);
                }
                // case 2
                else{
                    py.color = Color.black;
                    v.color = Color.red;
                }
            }
            // Xb1
            else if((a!=null && a.color==Color.red) ^ (b!=null && b.color==Color.red)){
                // Rb1
                if(v==py.left){
                    // case 1
                    if(a!=null && a.color==Color.red){
                        LL(py);
                        a.color = Color.black;
                        v.color = py.color;
                        py.color = Color.black;
                    }
                    // case 2
                    else{
                        LR(py);
                        b.color = py.color;
                        py.color = Color.black;
                    }
                }
                // Lb1
                else{
                    //case 1
                    if(b!=null && b.color==Color.red){
                        RR(py);
                        b.color = Color.black;
                        v.color = py.color;
                        py.color = Color.black;
                    }
                    //case 2
                    else{
                        RL(py);
                        a.color = py.color;
                        py.color = Color.black;
                    }
                }
            }
            // Xb2
            else if((a!=null && a.color==Color.red) && (b!=null && b.color==Color.red)){
                if(v==py.left){
                    LR(py);
                    b.color = py.color;
                    py.color = Color.black;
                }
                else{
                    RL(py);
                    a.color = py.color;
                    py.color = Color.black;
                }
            }
        }
        // r
        else if(v.color==Color.red){
            // Rr
            if(v==py.left){
                TreeNode w = v.right, bb = w.left, cc = w.right;
                // Rr(0)  Rr(1)  Rr(2)
                if(redChild(w)==0){
                    LL(py);
                    v.color = Color.black;
                    w.color = Color.red;
                }
                else if(redChild(w)==1){
                    if(bb!=null && bb.color==Color.red){
                        LR(py);
                        bb.color = Color.black;
                    }
                    else{
                        LR2(py);
                    }
                }
                else if(redChild(w)==2){
                    LR2(py);
                }
            }
            // Lr
            else if(v==py.right){
                TreeNode w = v.left, aa = v.right, cc = w.left, bb = w.right;
                // Lr(0)
                if(redChild(w)==0){
                    RR(py);
                    v.color = Color.black;
                    w.color = Color.red;
                }
                // Lr(1)
                else if(redChild(w)==1){
                    if(bb!=null && bb.color==Color.red){
                        RL(py);
                        bb.color = Color.black;
                    }
                    else{
                        RL2(py);
                    }
                }
                // Lr(2)
                else if(redChild(w)==2){
                    RL2(py);
                }
            }
        }
    }
    // count the number of red child
    public int redChild(TreeNode node){
        if(node.color== Color.red){
            System.out.println("=========red node has no red child======");
            return -1;
        }

        int res = 0;
        if(node.left!=null && node.left.color==Color.red) res++;
        if(node.right!=null && node.right.color==Color.red) res++;
        return res;
    }
    // swap 2 child, be careful when n1 is the left child of n2
    public void swap(TreeNode n1, TreeNode n2){
        // because you need to maintain a link between minHeap and RedBlackTree
        // you have to keep this link, so you can't swap content, you can only swap the TreeNode(address)
        // be careful to deal the the cases when n2 is left child of n1
        if(this.root==n1 || this.root==n2) this.root = (this.root==n2)?n1:n2;

        if(n1 == n2.left){
            TreeNode a = n1.left, b = n1.right, c = n2.right, p = n2.parent;
            Color c1 = n1.color, c2 = n2.color;

            n1.color = c2;
            n2.color = c1;
            n2.left = a;
            if(a!=null) a.parent = n2;
            n2.right = b;
            if(b!=null) b.parent = n2;
            n1.left = n2;
            n2.parent = n1;
            n1.right = c;
            if(c!=null) c.parent = n1;
            if(p!=null){
                if(p.left==n2) p.left = n1;
                else p.right = n1;
            }
            n1.parent = p;
        }
        else{
            TreeNode p1 = n1.parent, l1 = n1.left, r1 = n1.right;
            TreeNode p2 = n2.parent, l2 = n2.left, r2 = n2.right;
            Color c1 = n1.color, c2 = n2.color;

            n1.parent = p2;
            n2.parent = p1;
            if(p1!=null){
                if(p1.left==n1) p1.left = n2;
                else p1.right = n2;
            }
            if(p2!=null){
                if(p2.left==n2) p2.left = n1;
                else p2.right = n1;
            }

            n1.left = l2;
            if(l2!=null) l2.parent = n1;
            n2.left = l1;
            if(l1!=null) l1.parent = n2;

            n1.right = r2;
            if(r2!=null) r2.parent = n1;
            n2.right = r1;
            if(r1!=null) r1.parent = n2;

            n2.color = c1;
            n1.color = c2;
        }
    }

    // find the left Biggest child, used for deleting 2 degree node
    public TreeNode findLeftBig(TreeNode node){
        node = node.left;
        while(node.right!=null){
            node = node.right;
        }
        return node;
    }

    // insert a node
    public boolean insert(TreeNode node){
//        System.out.println("inserting.."+node2Str(node));
        if(node==null) return true;
        size++;
        node.color = Color.red;
        if(root==null) {
            node.color = Color.black;
            root = node;
            return false;
        }
        if(insertRecursion(root, node)){
            return true;
        }
        insertFlip(node);
//        println(this.toString());
//        if(this.check()==false){
//            print("-=================");
//            while(true);
//        }
        return false;
    }

    // insert rotate, fix the situation of 2 consecutive red points
    public void insertFlip(TreeNode p){
        TreeNode pp = p.parent;
        // p is root
        if(pp==null){
            p.color = Color.black;
            return;
        }
        // pp is black
        if(pp.color == Color.black) return;

        // now the p.color and pp.color are both red, need color flip and rotate
        TreeNode gp = pp.parent;
        TreeNode d = (pp==gp.left)?gp.right:gp.left;
        // XYr
        if(d!=null && d.color==Color.red){
            gp.color = Color.red;
            pp.color = Color.black;
            d.color = Color.black;
            insertFlip(gp);
            return;
        }
        else{ // XYb
            if(pp==gp.left){
                if(p==pp.left){
                    //LLb
                    pp.color = Color.black;
                    gp.color = Color.red;
                    LL(gp);
                }
                else{
                    //LRb
                    p.color = Color.black;
                    gp.color = Color.red;
                    LR(gp);
                }
            }
            else{
                if(p==pp.left){
                    //RLb
                    p.color = Color.black;
                    gp.color = Color.red;
                    RL(gp);
                }
                else{
                    //RRb
                    gp.color = Color.red;
                    pp.color = Color.black;
                    RR(gp);
                }
            }
        }
    }

    // insert recursively
    public boolean insertRecursion(TreeNode root, TreeNode node){
        if(root==null) System.out.println("========error======");
        if(root.num==node.num) return true;
        if(compare(root, node)<0){
            if(root.right!=null){
                insertRecursion(root.right, node);
            }
            else{
                root.right = node;
                node.parent = root;
            }
        }
        else{
            if(root.left!=null){
                insertRecursion(root.left, node);
            }
            else{
                root.left = node;
                node.parent = root;
            }
        }
        return false;
    }

    // for debug
    public void updateBy1(TreeNode node){
        if(node==null) return;
        node.eTime+=1;
        this.delete(node);
        if(node.eTime<node.tTime) this.insert(node);
    }
    // for debug, make a node to String
    public String node2Str(TreeNode node){
        if(node==null) return "(    null    )";
        return "("+node.num+","+node.eTime+","+node.tTime+","+node.color+")";
    }
    // for debug, print
    private static void print(String s){
        System.out.print(s);
    }
    private static void println(String s){
        System.out.println(s);
    }

    // for debug, output the red black tree to String
    public String toString(){
        String out = "";
        if(size==0) return "empty tree";
//        println(root+" "+root.left+" "+root.right);
        List<TreeNode> f1 = new ArrayList<>();
        f1.add(root);
        while(f1.size()>0){
            List<TreeNode> f2 = new ArrayList<>();
            for(TreeNode node:f1){
                if(node!=null){
                    f2.add(node.left);
                    f2.add(node.right);
                    out += "root:"+node2Str(node)+", left:"+node2Str(node.left)+", right:" + node2Str(node.right)+"  ||  ";
                }
            }
            f1 = f2;
            out += "\n";
        }
        return out;
    }

    // for debug
    public void range(){

    }

    // check the correctness of the tree
    public boolean check(){
        TreeNode node = root;
        if(node.color!=Color.black) return false;
        int blackLen = 0;
        while(node!=null){
//            System.out.println(node2Str(node));
            if(node.color==Color.black){
                blackLen++;
            }
            node = node.left;
        }
//        System.out.println("test");
        return checkDFS(root, blackLen, 0);
    }

    // for debug, check recursively
    public boolean checkDFS(TreeNode node, int blackLen, int curLen){
//        System.out.println(node2Str(node));
        if(node==null){
            return blackLen==curLen;
        }
        if(checkDFS(node.left, blackLen, (node.color==Color.black)?curLen+1:curLen)==false) return false;
        if(checkDFS(node.right, blackLen, (node.color==Color.black)?curLen+1:curLen)==false) return false;
        return true;
    }

    // for debug, generate random array for inserting and deleting
    private static void swapReference(int[] a, int i, int j){
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
    public static int randInt(int i, int j){
        Random rand = new Random(37);
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

    // test
    public static void test(){

        for(int iter = 0; iter<1000; iter++){
            RedBlackTree tree = new RedBlackTree();
            System.out.println("################TEST "+iter+"################");
            int[] inputSeq = randArr(100);
            int[] delSeq = randArr(60);

            showArray("inputSeq: ", inputSeq);
            showArray("deletSeq: ", delSeq);

            int n = inputSeq.length;
            TreeNode[] nodes = new TreeNode[200];
            for(int i: inputSeq){
                nodes[i] = new TreeNode(i, 0, 0);
                tree.insert(nodes[i]);
            }

            for(int i: delSeq){
                tree.delete(nodes[i]);
            }
            System.out.println("size " + tree.size);
            boolean res = tree.check();
            System.out.println("check " + res);
            if(!res){
                System.out.println("==================false detect================");
                return;
            }
//            System.out.println(tree.toString());
            System.out.println(" ");
        }
    }
    // main for RedBlack Tree
    public static void main(String[] args){
        test();
    }

    // 6 rotation in the AVL tree and RBT tree
    public void LL(TreeNode A){
        TreeNode parent=(A==this.root)?(new TreeNode()):A.parent;

        TreeNode B = A.left, Bl = B.left, Br = B.right, Ar = A.right;
        B.right = A;
        A.parent = B;
        A.left = Br;
        if(Br!=null) Br.parent = A;
        if(parent.left==A){
            parent.left = B;
        }
        else{
            parent.right = B;
        }
        B.parent = parent;

        if(A==this.root){
            this.root = B;
            this.root.parent = null;
        }
    }
    public void RR(TreeNode A){
        TreeNode parent = (A==this.root)?(new TreeNode()):A.parent;

        TreeNode B = A.right, Al = A.left, Bl = B.left, Br = B.right;
        A.right = Bl;
        if(Bl!=null) Bl.parent = A;
        B.left = A;
        A.parent = B;
        if(parent.left==A){
            parent.left = B;
        }
        else{
            parent.right = B;
        }
        B.parent = parent;

        if(A==this.root){
            this.root = B;
            this.root.parent = null;
        }
    }
    public void LR(TreeNode A){
        TreeNode Al = A.left;
        RR(Al);
        LL(A);
    }
    public void RL(TreeNode A){
        TreeNode Ar = A.right;
        LL(Ar);
        RR(A);
    }
    public void LR2(TreeNode py){
        TreeNode parent = (py==this.root)?(new TreeNode()):py.parent;
        TreeNode v = py.left, y = py.right, a = v.left, w = v.right, b = w.left, x = w.right, c = x.left, d = x.right;

        w.right = c;
        if(c!=null)c.parent = w;
        v.parent = x;
        x.left = v;
        x.right = py;
        py.parent = x;
        py.left = d;
        if(d!=null) d.parent = py;
        x.parent = parent;
        if(parent.left==py) parent.left = x;
        else parent.right = x;
        x.color = Color.black;

        if(py==this.root){
            this.root = x;
            this.root.parent = null;
        }
    }
    public void RL2(TreeNode py){
        TreeNode parent = (py==this.root)?(new TreeNode()):py.parent;
        TreeNode v = py.right, y = py.left, w = v.left, a = v.right, x = w.left, b = w.right, d = x.left, c = x.right;

        w.left = c;
        if(c!=null) c.parent = w;
        x.right = v;
        v.parent = x;
        x.left = py;
        py.parent = x;
        py.right = d;
        if(d!=null) d.parent = py;
        x.parent = parent;
        if(parent.left==py) parent.left = x;
        else parent.right = x;
        x.color = Color.black;

        if(py==this.root){
            this.root = x;
            this.root.parent = null;
        }
    }
}

// TreeNode for RBT, with 2 construction
class TreeNode{
    TreeNode left;
    TreeNode right;
    TreeNode parent;
    Color color;
    int num;
    int eTime;
    int tTime;
    public TreeNode(){
        left = right = parent = null;
        color = Color.red;
        num = eTime = tTime = 0;
    }
    public TreeNode(int num, int eTime, int tTime){
        this.num = num;
        this.eTime = eTime;
        this.tTime = tTime;
        color = Color.red;
    }
}

// a color class, for red black tree
// there are 2 color, red and black
enum Color{
    red, black;
}