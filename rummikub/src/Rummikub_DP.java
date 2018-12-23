import java.util.*;

public class Rummikub_DP {

    private ArrayList<Integer> R;
    private ArrayList<Integer> G;
    private ArrayList<Integer> B;
    private ArrayList<Integer> Z;

    Rummikub_DP() {
        R = new ArrayList<>();
        G = new ArrayList<>();
        B = new ArrayList<>();
        Z = new ArrayList<>();
    }

    Rummikub_DP(ArrayList<Integer> r,
                ArrayList<Integer> g,
                ArrayList<Integer> b,
                ArrayList<Integer> z) {
        R = r;
        G = g;
        B = b;
        Z = z;
        sortPieces();
    }

    void addR(int x) {
        R.add(x);
    }

    void addR(int[] a) {
        for (int x : a) {
            R.add(x);
        }
    }

    void addG(int x) {
        G.add(x);
    }

    void addG(int[] a) {
        for (int x : a) {
            G.add(x);
        }
    }

    void addB(int x) {
        B.add(x);
    }

    void addB(int[] a) {
        for (int x : a) {
            B.add(x);
        }
    }

    void addZ(int x) {
        Z.add(x);
    }

    void addZ(int[] a) {
        for (int x : a) {
            Z.add(x);
        }
    }

    private void sortPieces() {
        Collections.sort(R);
        Collections.sort(G);
        Collections.sort(B);
        Collections.sort(Z);
    }

    /*
     * checks if the ArrayList contains a valid (consequential) run
     * from start index to the end index
     *
     * @pre A contains distinct Integer values
     * @post \return = true if the arrayList contains
     * a run from a.get(start) to a.get(end)
     */
    boolean validRun(int start, int end, ArrayList<Integer> A) {
        int i = start;
        while (i < end) {
            if (A.get(i) != A.get(i + 1) - 1) {
                return false;
            }
            i++;
        }
        return true;
    }

    void printList(ArrayList<Integer> A) {
        for (int x : A) {
            System.out.print(x + " ");
        }
        System.out.println();
    }

    void printList(int a, int b, ArrayList<Integer> A){
        printList(a, b, A, "");
    }
    void printList(int a, int b, ArrayList<Integer> A, String str) {
        if (a < 0 || b < a || b > A.size()) {
            throw new IllegalArgumentException("wrong input:" + a + " " + b);
        }
        while (a < b) {
            System.out.print(str + A.get(a) + " ");
            a++;
        }
        System.out.println();
    }

    boolean DPSolvable() {
        sortPieces();

        int nR = R.size() + 1;
        int nG = G.size() + 1;
        int nB = B.size() + 1;
        int nZ = Z.size() + 1;
        boolean[][][][] T = new boolean[nR][nG][nB][nZ];
        // no tiles left = true;
        T[0][0][0][0] = true;
        for (int w = 0; w < nZ; w++) {
            for (int z = 0; z < nB; z++) {
                for (int y = 0; y < nG; y++) {
                    for (int x = 0; x < nR; x++) {
                        // Don't change T[0][0][0]!
                        if (x > 0 || y > 0 || z > 0 || w > 0) {
                            boolean able = false;
                            // Match of four
                            if (x > 0 && y > 0 && z > 0 && w > 0
                                    && R.get(x - 1).equals(G.get(y - 1))
                                    && G.get(y - 1).equals(B.get(z - 1))
                                    && B.get(z - 1).equals(Z.get(w - 1))
                                    && T[x - 1][y - 1][z - 1][w - 1]) {
                                able = true;
                            }

                            // Match of three without RED
                            if (y > 0 && z > 0 && w > 0
                                    && G.get(y - 1).equals(B.get(z - 1))
                                    && B.get(z - 1).equals(Z.get(w - 1))
                                    && T[x][y - 1][z - 1][w - 1]) {
                                able = true;
                            }
                            // Match of three without GREEN
                            if (x > 0 && z > 0 && w > 0
                                    && R.get(x - 1).equals(B.get(z - 1))
                                    && B.get(z - 1).equals(Z.get(w - 1))
                                    && T[x - 1][y][z - 1][w - 1]) {
                                able = true;
                            }
                            // Match of three without BLUE
                            if (x > 0 && y > 0 && w > 0
                                    && R.get(x - 1) == G.get(y - 1)
                                    && G.get(y - 1) == Z.get(w - 1)
                                    && T[x - 1][y - 1][z][w - 1]) {
                                able = true;
                            }
                            // Match of three without BLACK
                            if (x > 0 && y > 0 && z > 0
                                    && R.get(x - 1).equals(G.get(y - 1))
                                    && G.get(y - 1).equals(B.get(z - 1))
                                    && T[x - 1][y - 1][z - 1][w]) {
                                able = true;
                            }

                            // RED Run
                            for (int i = 1; i <= x - 2; i++) {
                                if (validRun(i - 1, x - 1, R)
                                        && T[i - 1][y][z][w]) {
                                    able = true;
                                }
                            }
                            // GREEN Run
                            for (int i = 1; i <= y - 2; i++) {
                                if (validRun(i - 1, y - 1, G)
                                        && T[x][i - 1][z][w]) {
                                    able = true;
                                }
                            }
                            // BLUE Run
                            for (int i = 1; i <= z - 2; i++) {
                                if (validRun(i - 1, z - 1, B)
                                        && T[x][y][i - 1][w]) {
                                    able = true;
                                }
                            }
                            // BLACK Run
                            for (int i = 1; i <= w - 2; i++) {
                                if (validRun(i - 1, w - 1, Z)
                                        && T[x][y][z][i - 1]) {
                                    able = true;
                                }
                            }

                            T[x][y][z][w] = able;
                        }
                    }
                }
            }
        }
        //System.out.println(Arrays.deepToString(T));

        // calculate solution from recurrence variable

        int pR = nR - 1;
        int pG = nG - 1;
        int pB = nB - 1;
        int pZ = nZ - 1;
        // if there exists a solution, construct one
        if (T[pR][pG][pB][pZ]) {
            System.out.println("solution");

            while (pR != 0 || pG != 0 || pB != 0 || pZ != 0) {
                // check for match of four
                if (pR > 0 && pG > 0 && pB > 0 && pZ > 0
                        && T[pR - 1][pG - 1][pB - 1][pZ - 1]
                        && R.get(pR - 1).equals(G.get(pG - 1))
                        && G.get(pG - 1).equals(B.get(pB - 1))
                        && B.get(pB - 1).equals(Z.get(pZ - 1))) {
                    System.out.print("R" + R.get(pR - 1) + " ");
                    System.out.print("G" + G.get(pG - 1) + " ");
                    System.out.print("B" + B.get(pB - 1) + " ");
                    System.out.print("Z" + Z.get(pZ - 1) + " ");
                    System.out.println();
                    pR--;
                    pG--;
                    pB--;
                    pZ--;
                }

                // check for match of three without RED
                if (pG > 0 && pB > 0 && pZ > 0
                        && T[pR][pG - 1][pB - 1][pZ - 1]
                        && G.get(pG - 1).equals(B.get(pB - 1))
                        && B.get(pB - 1).equals(Z.get(pZ - 1))) {
                    System.out.print("G" + G.get(pG - 1) + " ");
                    System.out.print("B" + B.get(pB - 1) + " ");
                    System.out.print("Z" + Z.get(pZ - 1) + " ");
                    System.out.println();
                    pG--;
                    pB--;
                    pZ--;
                }
                // check for match of three without GREEN
                if (pR > 0 && pB > 0 && pZ > 0
                        && T[pR - 1][pG][pB - 1][pZ - 1]
                        && R.get(pR - 1).equals(B.get(pB - 1))
                        && B.get(pB - 1).equals(Z.get(pZ - 1))) {
                    System.out.print("R" + R.get(pR - 1) + " ");
                    System.out.print("B" + B.get(pB - 1) + " ");
                    System.out.print("Z" + Z.get(pZ - 1) + " ");
                    System.out.println();
                    pR--;
                    pB--;
                    pZ--;
                }
                // check for match of three without BLUE
                if (pR > 0 && pG > 0 && pZ > 0
                        && T[pR - 1][pG - 1][pB][pZ - 1]
                        && R.get(pR - 1).equals(G.get(pG - 1))
                        && G.get(pG - 1).equals(Z.get(pZ - 1))) {
                    System.out.print("R" + R.get(pR - 1) + " ");
                    System.out.print("G" + G.get(pG - 1) + " ");
                    System.out.print("Z" + Z.get(pZ - 1) + " ");
                    System.out.println();
                    pR--;
                    pG--;
                    pZ--;
                }
                // check for match without BLACK
                if (pR > 0 && pG > 0 && pB > 0
                        && T[pR - 1][pG - 1][pB - 1][pZ]
                        && R.get(pR - 1).equals(G.get(pG - 1))
                        && G.get(pG - 1).equals(B.get(pB - 1))) {
                    System.out.print("R" + R.get(pR - 1) + " ");
                    System.out.print("G" + G.get(pG - 1) + " ");
                    System.out.print("B" + B.get(pB - 1) + " ");
                    System.out.println();
                    pR--;
                    pG--;
                    pB--;
                }
                // check for Red Run
                for (int r = 1; r <= pR - 2; r++) {
                    if (T[r - 1][pG][pB][pZ]
                            && validRun(r - 1, pR - 1, R)) {
                        printList(r - 1, pR, R, "R");
                        pR = r - 1;
                    }
                }
                // check for Green Run
                for (int g = 1; g <= pG - 2; g++) {
                    if (T[pR][g - 1][pB][pZ]
                            && validRun(g - 1, pG - 1, G)) {
                        printList(g - 1, pG, G, "G");
                        pG = g - 1;
                    }
                }
                // check for Blue Run
                for (int b = 1; b <= pB - 2; b++) {
                    if (T[pR][pG][b - 1][pZ]
                            && validRun(b - 1, pB - 1, B)) {
                        printList(b - 1, pB, B, "B");
                        pB = b - 1;
                    }
                }
                // check for Black Run
                for (int z = 1; z <= pZ - 2; z++) {
                    if (T[pR][pG][pB][z - 1]
                            && validRun(z - 1, pZ - 1, Z)) {
                        printList(z - 1, pZ, Z, "Z");
                        pZ = z - 1;
                    }
                }
            }
            System.out.println();
        }

        return T[nR - 1][nG - 1][nB - 1][nZ - 1];
    }

    public static void main(String[] args) {
        Rummikub_DP rk = new Rummikub_DP();
        int[] red = {
                1, 4, 2, 3, 6, 7, 10
        };
        int[] green = {
                4, 5, 6, 7, 8, 9, 10
        };
        int[] blue = {
                5, 6, 7, 10
        };
        int[] black = {
                4, 5, 6, 7
        };
        rk.addR(red);
        rk.addG(green);
        rk.addB(blue);
        rk.addZ(black);
        rk.sortPieces();

        System.out.println("pieces");
        System.out.print("RED: \t");
        rk.printList(rk.R);
        System.out.print("GREEN: \t");
        rk.printList(rk.G);
        System.out.print("BLUE: \t");
        rk.printList(rk.B);
        System.out.print("BLACK: \t");
        rk.printList(rk.Z);
        System.out.println();

        System.out.println(rk.DPSolvable());
    }
}
