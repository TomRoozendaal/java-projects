/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplebst;

import java.util.*;

/**
 *
 * @author s147682
 */
public class BST {
    Node root;
    int height;
    Node[][] layout; 
    // 2d array containing nodes, example:
    // [null, null, null, null, null, null, null, NODE, null, null, null, null, null, null, null]
    // [null, null, null, NODE, null, null, null, null, null, null, null, NODE, null, null, null]
    // [null, NODE, null, null, null, null, null, null, null, NODE, null, null, null, NODE, null]
    // [null, null, NODE, null, null, null, null, null, null, null, null, null, NODE, null, NODE]
    int lvl;
    
    // Constructors
    public BST( int k ){
        root = new Node( k );
    }
    public BST( int[] ks ){
        root = new Node( ks[0] );
        for (int i = 1; i < ks.length; i++){
            this.insert( ks[i] );
        }
    }
    
    // Insert, O(h) time
    final void insert( int k ){
        Node x = root;
        boolean done = false;
        
        while ( x != null && !done ){
            if ( x.key > k ){
                if ( x.left == null ){
                    x.left = new Node(k, x);     // create sub-tree
                    done = true;
                } else {
                    x = x.left;                  // insert
                }
            } else {
                if ( x.right == null ){
                    x.right = new Node(k, x);    // create sub-tree
                    done = true;
                } else {
                    x = x.right;                 // insert
                }
            }
        }
    }
    // Delete, O(h) time
    final void delete( Node x ){
        if ( x.left == null && x.right == null ){
            x.split();
        } else if ( x.right != null ){ // there is a right child 
            Node y = x.getSucc();
            x.key = y.key;
            delete(y);
        } else if ( x.left != null ){ // there is a left child
            Node y = x.getPred();
            x.key = y.key;
            delete(y);
        }
    }
    
    // Search, O(h) time
    Node search( int p ){
        Node x = root;
        
        while ( x != null ){
            if ( x.key == p ){ // return that node
                return x;
            } else if ( x.key > p ){
                if ( x.left == null ) {
                    return null;
                }
                x = x.left; // recurse into left sub-tree
            } else {
                if ( x.right == null ){
                    return null;
                }
                x = x.right; // recurse into right sub-tree
            }
        }
        return null;
    }
    
    // inOrderTreeWalk with printing, O(n) time
    void inOrderPrint(){
        inOrderPrint( this.root );
    }
    void inOrderPrint( Node x ){        
        if ( x != null ){
            // left
            if (x.left !=  null){
                inOrderPrint( x.left );
            }
            // node
            System.out.print( x.key + " " );
            // right
            if (x.right != null){
                inOrderPrint( x.right );
            }
        }
    }
    
    // postOrderTreeWalk to augment with amount, O(n) times
    void augmentAmount(){
        augmentAmount( this.root );
    }
    void augmentAmount( Node x ){
        if ( x != null ){
            // left
            if (x.left !=  null){
                augmentAmount( x.left );
            }
            // right
            if (x.right != null){
                augmentAmount( x.right );
            }
            //node
            if (x.right != null){
                x.amount += x.right.amount;
            }
            if (x.left != null){
                x.amount += x.left.amount;
            }
            x.amount++;
        }
    }
    
    // calculate height, O(n) time
    int calcHeight(){
        this.height = 0;
        calcHeight( this.root, 0 );
        return this.height;
    }
    void calcHeight( Node x, int h ){
        if ( x != null ){
            // left
            if (x.left !=  null){
                calcHeight( x.left, h + 1 );
            }
            // node
            if( this.height < h ){
                this.height = h;
            }
            // right
            if (x.right != null){
                calcHeight( x.right, h + 1 );
            }
        }
    }
    // make and print layout, O(n) time
    void makeLayout(){
        calcHeight();
        layout = new Node[ this.height + 1][(int)Math.pow(2, this.height + 1) - 1];
        lvl = 0;
        int index = 0 + (int) ( Math.pow( 2, this.height ) - lvl );
        layoutInsert( this.root, index );
        printLayout();
    }
    void layoutInsert( Node v, int index){
        if ( v != null ){
            lvl++;  // going down a level
            
            // node
            if ( layout[ lvl - 1 ][ index - 1 ] == null ){
                layout[ lvl - 1 ][ index - 1 ] = v;
            } else {
                System.out.println("cell not vacant");
            }
            // right
            if (v.right != null){
                int rIndex = index + (int) ( Math.pow( 2, this.height - lvl) );
                layoutInsert( v.right, rIndex );
            }
            // left
            if (v.left !=  null){
                int lIndex = index - (int)( Math.pow( 2, this.height - lvl) );
                layoutInsert( v.left, lIndex );
            }

            lvl--;  // going (back) up a level
        }
    }
    void printLayout(){
        for ( int i = 0; i < this.layout.length; i++ ){
            for ( int j = 0; j < this.layout[0].length; j++ ){
                String str = "";
                if (  this.layout[i][j] != null ){
                    Node x = this.layout[i][j];
                    if ( x.left != null ){
                        str = "/" + str;
                    }
                    str = str + x.key;
                    if ( x.right != null ){
                        str = str + "\\";
                    }
                }
                System.out.format("%4s", str );
            }
            System.out.println();
        }
        
    }
    
    // print the tree structure (compact), O(n) time
    void printCompact(){
        Queue<Node> PQ = new LinkedList<>(); // print queue
        Queue<Node> NQ = new LinkedList<>(); // next queue
        calcHeight();        

        // Add first level (root) to the print queue
        PQ.add(this.root);
        printLevel( PQ, NQ );
    }
    void getLevel( Queue<Node> PQ, Queue<Node> NQ ){
        PQ.addAll(NQ);
        NQ.clear();
        // PQ <- NQ
        // NQ should be empty here
        printLevel( PQ, NQ );
    }
    void printLevel( Queue<Node> PQ, Queue<Node> NQ ) {
        while ( PQ.size() > 0 ) {
            Node x = (Node) PQ.poll();
            if ( x.left != null ){
                NQ.add( x.left );
                System.out.print(" /");
            }
            System.out.print("(" + x.key + ")" );
            if ( x.right != null ){
                NQ.add( x.right );
                System.out.print("\\ ");
            }
        }
        System.out.println();
        if( NQ.size() > 0){
            getLevel( PQ, NQ );
        }
    }
}
