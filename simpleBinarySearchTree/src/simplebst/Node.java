/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplebst;

/**
 *
 * @author s147682
 */
public class Node {
    int key;
    boolean red = false; //indicates color, true = red
    int amount = 0;
    Node parent;
    Node left;
    Node right;

    
    // constructors
    public Node( int k ){
        this.key = k;
        this.parent = null; // root constructor
    }
    public Node( int k, Node p ){
        this.key = k;
        this.parent = p;
    }
    
    // get Predecessor, O(h) time
    public Node getPred(){
        if ( this.left != null ) {
            Node y = this.left;
            while ( y.right != null ){
                y = y.right;
            }
            return y;
        } else if ( this.parent.key <= this.key ) {
            return this.parent;
        } else {
            return null;
        }
    }
    
    // get Successor, O(h) time
    public Node getSucc(){
        if ( this.right != null ) {
            Node y = this.right;
            while ( y.left != null ){
                y = y.left;
            }
            return y;
        } else if ( this.parent.key >= this.key ) {
            return this.parent;
        } else {
            return null;
        }
    }
    
    // splits node from its barent ( could be used as deleting of leaf/sub-tree )
    public void split() {
        if ( this.parent.right == this ){ // this is a right child
            this.parent.right = null;
        } else if ( this.parent.left == this ){ // this is a left child
            this.parent.left = null;
        }
    }
}
