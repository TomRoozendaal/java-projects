/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplebst;

import java.util.Arrays;

/**
 *
 * @author s147682
 */
public class BSTDemo{
    BST T; // Binary Search Tree
    int n; // amount of nodes
    
    // constructor
    public BSTDemo(int n){
        this.n = n;
        T = new BST( (n + (int)(2 * Math.random())) / 2 );
        
        for (int i = 0; i < n-1; i++){
            int r = 1 + (int)( n * Math.random() );
            T.insert( r );
        }
    }
    
    // demo method
    void demo(){
        int sKey = 1 + (int)( n * Math.random() );
        Node p = T.search( sKey );
        if ( p != null ){
            System.out.println( p + " contains key " + sKey );
        } else {
            System.out.println( "key " + sKey + " not found :(" );
        }
        
        System.out.println();
        T.inOrderPrint();
        System.out.println();
        T.augmentAmount();
        T.printCompact();
        
        System.out.println();
        if ( T.root.left != null ){
            T.delete( T.root.left );
        } else if ( T.root.right != null ){
            T.delete( T.root.right );
        }
        T.inOrderPrint();
        System.out.println();
        T.printCompact();
        System.out.println( "The height of the tree is " + T.calcHeight() );
        
        // making and printing the tree layout
        T.makeLayout();
    }
    
    // ---------------- MAIN ---------------- 
    public static void main(String[] args) {
        int n = 8;
        new BSTDemo(n).demo();
    }
}
