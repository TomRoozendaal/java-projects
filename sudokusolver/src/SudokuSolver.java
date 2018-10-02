class SudokuSolver {
    int SIZE = 9;     // size of the grid
    int DMAX = 9;     // maximal digit to be filled in
    int BOXSIZE = 3;  // size of the boxes (subgrids that should contain all digits)
    int[][] solGrid;  // the solution grid; 0 represents empty
    int count = 0;    // counter for tries in solve();
    
    int solutionnr = 0; //solution counter
    int[][] somesudoku;
    
    public SudokuSolver(int[][] grid){
        this.somesudoku = grid;
    }
    
    // ----------------- conflict calculation --------------------
    
    // is there a conflict when we fill in d at position r,c?
    boolean givesConflict(int r, int  c, int d) {
        if (rowConflict(r, d) || colConflict(c, d) || boxConflict(r, c, d)) {
            return true; 
        }
        return false;
    }
    
    // returns true if the same number is in its row 
    boolean rowConflict(int r, int d) {
        for (int i = 0; i < somesudoku.length; i++) {
            if (somesudoku[r][i] == d) {
                return true;
            }
        }
        return false;
    }
    
    // returns true if the same number is in its column
    boolean colConflict(int c, int d) {
        for (int i = 0; i < somesudoku[0].length; i++) {
            if (somesudoku[i][c] == d) {
                return true;
            }
        }
        return false;
    }
    
    // returns true if the same number is in its box
    boolean boxConflict(int rr, int cc, int d) {
        // finding the box [rr][cc] is in
        int r = rr - (rr % BOXSIZE);
        int c = cc - (cc % BOXSIZE);
        
        for (int i = r; i < (r + BOXSIZE); i++) {
            for (int j = c; j < (c + BOXSIZE); j++) {
                if (i != rr && j != cc && somesudoku[i][j] == d) {
                    return true;
                }
            }
        }
        return false;
    }
    
    // --------- solving ----------
    
    // finds the next empty square (in "reading order")
    // writes the coordinates in rempty and cempty
    // returns false if there is no empty square in the current grid
    int[] findEmptySquare() {
        for (int i = 0; i < somesudoku.length; i++) {
            for (int j = 0; j < somesudoku[0].length; j++) {
                if (somesudoku[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return null; // no empty square found
    }
    
    // prints all solutions that are extensions of current grid
    // leaves grid in original state
    void solve() {
        if (findEmptySquare() == null) { // sudoku is solved, base condition
            solGrid = somesudoku; // store solution
            print(solGrid); // print solution
            solutionnr++;
            
        } else {
            int[] empty = findEmptySquare();
            
            for (int d = 1; d <= DMAX; d++) {
                if (!givesConflict(empty[0], empty[1], d)) {
                    somesudoku[empty[0]][empty[1]] = d;
                    
                    count++; 
                    // show print every n steps (testing purposes)
                    //if (count % 25 == 0){
                    //    print(somesudoku);
                    //}
                    
                    solve(); // recursion
                }
                
                somesudoku[empty[0]][empty[1]] = 0; // try other values
            }
        }
        
        
    }
    
    // ------------------------- misc -------------------------
    
    // prints a grid, 0s are printed as spaces
    void print(int[][] grid) {
        System.out.println("+-----------------------+");
        for (int x = 0; x < grid.length; x++) {
            if (x % BOXSIZE == 0 && x != 0) {
                System.out.println("-------------------------");   
            }
            
            for (int y = 0; y < grid[0].length; y++) {
                if (y % BOXSIZE == 0) {
                    System.out.print("| ");   
                }
                
                if (grid[x][y] != 0) {
                    System.out.print(grid[x][y] + " ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.print("|");
            System.out.println();
        }
        System.out.println("+-----------------------+");
        System.out.println();
    }
    
    // --------------- where it all starts --------------------
    
    void solveIt() {
        solve();
        
        // print grid if there's only one solution (assuming there is one)
        // print amount of solutions if more
        if (solutionnr > 1) {
            System.out.println(solutionnr);
        } else {
            //print(solGrid);
        }
    }
    
    
    public static void main(String[] args) {
        
        int[][] grid = new int[][] {         
//            { 0, 6, 0,   0, 0, 1,    0, 9, 4 },    //original; one solution     
            { 0, 0, 0,   0, 0, 1,    0, 9, 4 }, //to get more solutions
            { 3, 0, 0,   0, 0, 7,    1, 0, 0 }, 
            { 0, 0, 0,   0, 9, 0,    0, 0, 0 }, 

            { 7, 0, 6,   5, 0, 0,    2, 0, 9 }, 
            { 0, 3, 0,   0, 2, 0,    0, 6, 0 }, 
            { 9, 0, 2,   0, 0, 6,    3, 0, 1 }, 

            { 0, 0, 0,   0, 5, 0,    0, 0, 0 }, 
            { 0, 0, 7,   3, 0, 0,    0, 0, 2 }, 
            { 4, 1, 0,   7, 0, 0,    0, 8, 0 }, 
        };
        
        SudokuSolver woah = new SudokuSolver( grid );
        woah.solveIt();
    }
}
