/*
 *  Tom van Roozendaal
 *  Grid Puzzle solver
 *  date: 05/10/2018
 *
 *  This sketch solves a puzzle game using a brute-force, recursive Depth-First-search algorithm.
 *
 *  ~ The puzzle game to be solved goes as follows:
 *  On an empty grid (of arbitrary size, usually 10x10), put a 1 on an arbitrary square. Starting from that square,
 *  you can move horizontally or vertically by jumping over two squares or move diagonally by jumping over one square.
 *  There you will place the number 2. Your goal is to fill the entire grid by jumping from cell to cell.
 */
import java.util.*;

public class gridPuzzle {
    int grid[][];
    int rows, cols;

    int curr;
    int solCount;

    // starting position
    int[] pos = new int[2];
    /*
     *  relMoves is the array that contains the possible relative moves a player could make.
     *
     *  predefined game:            {{-2, -2},{-3, 0},{-2, 2},{0, 3},{2, 2},{3, 0},{2, -2},{0, -3}};
     *  horse moves (checkers):     {{-1, -2},{-2, -1},{-2, 1},{-1, 2},{1, 2},{2, 1},{2, -1},{1, -2}};
     */
    int[][] relMoves = {{-2, -2},{-3, 0},{-2, 2},{0, 3},{2, 2},{3, 0},{2, -2},{0, -3}};

    // randomize the initial conditions for looking
    // COULD POTENTIALLY INCREASE COMPUTATION TIME
    boolean randomInit = false;
    // fully randomize the solution, will be disabled if printAllSolutions is set to true
    // WILL MOST LIKELY INCREASE COMPUTATION TIME
    boolean randomizeSolution = false;
    // print all possible solutions
    // VERY TIME CONSUMING FOR LARGE GRID SIZES
    boolean printAllSolutions = false;

    gridPuzzle(int rows, int cols, int startingRow, int startingCol) {
        this.rows = rows;
        this.cols = cols;
        this.pos[0] = startingCol - 1;
        this.pos[1] = startingRow - 1;

        this.solCount = 0;
        this.curr = 1;
        this.grid = new int[rows][cols];
        this.grid[pos[0]][pos[1]] = curr;
        this.curr++;
    }

    void run() {
        if (randomInit){
            shuffleArray(relMoves);
        }

        solve();

        if (solCount == 1){
            System.out.println("finished, solution found.");
        } else if (solCount > 1){
            System.out.println("finished, solutions: " + solCount + ".");
        } else {
            System.out.println("finished, no solutions found.");
        }
    }
    // ------------ misc ------------
    void printGrid(){
        for (int x = 0; x < grid[0].length; x++) {
            System.out.printf("%3s", "+------" );
        }
        System.out.println("+");
        for (int y = 0; y < grid.length; y++) {
            if (y % rows == 0 && y != 0) {
                System.out.println("-------------------------");
            }
            for (int x = 0; x < grid[0].length; x++) {
                if (x % cols == 0) {
                    System.out.print("| ");
                }
                if (grid[y][x] != 0) {
                    System.out.printf("%3d    ", grid[y][x] );
                } else {
                    System.out.printf("%3s    ", ".");
                }
            }
            System.out.println("|");
        }
        for (int x = 0; x < grid[0].length; x++) {
            System.out.printf("%3s", "+------" );
        }
        System.out.println("+");
        System.out.println();
    }

    // ------------ calculations ------------
    void solve(){
        if (curr > rows * cols){
            solCount++;
            printGrid();
        }
        ArrayList<int[]> opt = findOptions(pos[0], pos[1]);
        while (opt.size() > 0 && (solCount == 0 || printAllSolutions)){
            for (int k = 0; k < opt.size(); k++ ){
                int[] pre = pos;
                pos = opt.get(k);
                grid[pos[0]][pos[1]] = curr;
                curr++;
                // recurse
                solve();
//                if (solCount == 0) {
//                    printGrid();
//                }
                // reset
                grid[pos[0]][pos[1]] = 0;
                pos = pre;
                curr--;
            }
            break;
        }
    }


    // ------------ Options methods ------------
    ArrayList<int[]> findOptions(int y, int x) {
        ArrayList<int[]> options = new ArrayList<>();
        if (randomizeSolution){
            shuffleArray(relMoves);
        }
        for (int i = 0; i < relMoves.length; i++) {
            if (y + relMoves[i][0] >= 0 && y + relMoves[i][0] < rows && x + relMoves[i][1] >= 0 && x + relMoves[i][1] < cols
                    && grid[y + relMoves[i][0]][x + relMoves[i][1]] == 0) {
                int[] opt = {y + relMoves[i][0], x + relMoves[i][1]};
                options.add(opt);
            }
        }
        return options;
    }

    // ------------ array shuffle ------------
    static void shuffleArray(Object[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            Object a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public static void main(String[] args){
        long startingTime = System.currentTimeMillis();

        // gridPuzzle( NROF_ROWS, NROF_COLS, STARTING_ROW, STARTING_COL);
        // starting positions start from 1.
        gridPuzzle gp = new gridPuzzle(6, 6, 1, 1);
        gp.run();

        // Running time stuff
        long totalTime = (System.currentTimeMillis() - startingTime);
        long millis = totalTime % 1000;
        long second = (totalTime / 1000) % 60;
        long minute = (totalTime / (1000 * 60)) % 60;
        long hour = (totalTime / (1000 * 60 * 60)) % 24;

        String time = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);
        System.out.println("running time: " + time);
    }
}

