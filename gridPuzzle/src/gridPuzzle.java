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
    private int grid[][];
    private int challengeGrid[][];
    private ArrayList<Integer> challengeNums = new ArrayList<>();
    private int rows, cols;

    private int curr;
    private int solCount;

    // starting position
    private int[] pos = new int[2];
    /*
     *  relMoves is the array that contains the possible relative moves a player could make.
     *
     *  star pattern:           {{2, -2},{0, -3},{-2, -2},{-3, 0},{-2, 2},{0, 3},{2, 2},{3, 0}};
     *  horsing (checkers):     {{-1, -2},{-2, -1},{-2, 1},{-1, 2},{1, 2},{2, 1},{2, -1},{1, -2}};
     *  moonwalk:               {{-1, 0},{0, -1},{2, 2}}
     */
    private int[][] relMoves = {{2, -2},{0, -3},{-2, -2},{-3, 0},{-2, 2},{0, 3},{2, 2},{3, 0}};

    // rotate the elements of the relMoves array
    private int arrayShift = new Random().nextInt(relMoves.length);
    // randomize the initial conditions for looking
    // COULD POTENTIALLY INCREASE COMPUTATION TIME
    boolean randomInit = false;
    boolean verbose = false;

    // enabling this is probably not a good idea: repeating moves (which occurs more without randomization)
    // usually spreads out the occupied cells in the grid more. this leaves more room for
    // the player in a later states to move to.
    // ! - WILL MOST LIKELY INCREASE COMPUTATION TIME
    boolean randomizeSolution = false;

    // print all possible solutions
    // ! - VERY TIME CONSUMING FOR LARGE GRID SIZES
    boolean printAllSolutions = false;

    private gridPuzzle(int rows, int cols, int startingRow, int startingCol) {
        this.rows = rows;
        this.cols = cols;
        this.pos[0] = startingCol;
        this.pos[1] = startingRow;

        this.solCount = 0;
        this.curr = 1;
        this.grid = new int[rows][cols];
        grid[pos[0]][pos[1]] = curr;

        // you can use the challenge grid to 'lock' certain cells with a value
        this.challengeGrid = new int[grid.length][];
        for(int i = 0; i < grid.length; i++)
            this.challengeGrid[i] = grid[i].clone();
        this.curr++;
    }

    private void run() {
        if (randomInit){
            shuffleArray(relMoves);
        } else if(arrayShift != 0){
            rotateArray(relMoves, arrayShift);
            System.out.println("relative moves: " + Arrays.deepToString(relMoves));
        } else {
            System.out.println("relative moves: " + Arrays.deepToString(relMoves));
        }

        setupChallengeGrid();
        printGrid(challengeGrid);
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
    private void setupChallengeGrid(){
        //challengeGrid[2][4] = 25;

        for (int x = 0; x < challengeGrid[0].length; x++) {
            for (int y = 0; y < challengeGrid.length; y++) {
                int p = challengeGrid[y][x];
                if (p != 0) {
                    challengeNums.add(p);
                }
            }
        }
    }

    private void printGrid(int[][] grd){
        for (int x = 0; x < grd[0].length; x++) {
            System.out.printf("%3s", "+------" );
        }
        System.out.println("+");
        for (int y = 0; y < grd.length; y++) {
            if (y % rows == 0 && y != 0) {
                System.out.println("-------------------------");
            }
            for (int x = 0; x < grd[0].length; x++) {
                if (x % cols == 0) {
                    System.out.print("| ");
                }
                if (grd[y][x] != 0) {
                    System.out.printf("%3d    ", grd[y][x] );
                } else {
                    System.out.printf("%3s    ", ".");
                }
            }
            System.out.println("|");
        }
        for (int x = 0; x < grd[0].length; x++) {
            System.out.printf("%3s", "+------" );
        }
        System.out.println("+");
        System.out.println();
    }

    // ------------ calculations ------------
    // recursive Depth-first-searching algorithm to find a solution grid
    private void solve(){
        if (curr > rows * cols){
            solCount++;
            printGrid(grid);
        }
        // obtain the options (in a possibly randomized order)
        ArrayList<int[]> options = findOptions(pos[0], pos[1]);
        if (options.size() > 0 && (solCount == 0 || printAllSolutions)){
            for (int[] option : options) {
                int[] pre = pos;
                pos = option;
                grid[pos[0]][pos[1]] = curr;

                curr++;
                // recurse
                solve();
                curr--;

                if (solCount == 0 && verbose) {
                    printGrid(grid);
                    float percent = 100 * (float) curr /((float)rows*(float)cols);
                    System.out.println("peak-value " + curr + ":\t" + String.format("%.1f", percent)
                    + "%\n");
                }
                // reset
                grid[pos[0]][pos[1]] = 0;
                pos = pre;
            }
        }
    }

    // ------------ Options methods ------------
    private ArrayList<int[]> findOptions(int y, int x) {
        ArrayList<int[]> options = new ArrayList<>();
        if (randomizeSolution){
            shuffleArray(relMoves);
        }
        for (int[] relMove : relMoves) {
            if (y + relMove[0] >= 0 && y + relMove[0] < rows && x + relMove[1] >= 0 && x + relMove[1] < cols
                    && ((grid[y + relMove[0]][x + relMove[1]] == 0 && challengeGrid[y + relMove[0]][x + relMove[1]] == 0 && !challengeNums.contains(curr))
                    || (grid[y + relMove[0]][x + relMove[1]] == 0 && challengeGrid[y + relMove[0]][x + relMove[1]] == curr))) {
                int[] opt = {y + relMove[0], x + relMove[1]};
                options.add(opt);
            }
        }
        return options;
    }

    // ------------ array operations ------------
    private static void shuffleArray(Object[] ar)
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

    private static void rotateArray(Object[] ar, int shift)
    {
        Object[] tmp = ar.clone();
        for (int i = 0; i < ar.length; i++)
        {
            int index = (i + shift) % ar.length;
            ar[i] = tmp[index];
        }
    }

    // ------------ array operations ------------
    public static void main(String[] args){
        long startingTime = System.currentTimeMillis();

        // gridPuzzle( NROF_ROWS, NROF_COLS, STARTING_ROW, STARTING_COL);
        // starting positions start from 0.
        gridPuzzle gp = new gridPuzzle(7, 7, 0, 0);
        gp.verbose = true;
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

