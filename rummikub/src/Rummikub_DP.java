import java.util.*;

public class Rummikub_DP {
    /**
     * pieces represented as a Map of ArrayLists
     */
    private ArrayList<Integer> R;
    private ArrayList<Integer> G;
    private ArrayList<Integer> B;

    Rummikub_DP() {
        R = new ArrayList<>();
        G = new ArrayList<>();
        B = new ArrayList<>();
    }

    Rummikub_DP(ArrayList<Integer> r, ArrayList<Integer> g, ArrayList<Integer> b) {
        R = r;
        G = g;
        B = b;
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

    /*
     * checks if the ArrayList contains a valid (consequential) run
     * from start index to the end index
     */
    boolean validRun(int start, int end, ArrayList<Integer> A) {
        int i = start;
        while (i + 1 < end + 1) {
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

    void printList(int a, int b, ArrayList<Integer> A) {
        if (a < 0 || b < a || b > A.size()) {
            throw new IllegalArgumentException("wrong input:" + a + " " + b);
        }
        while (a < b) {
            System.out.print(A.get(a) + " ");
            a++;
        }
        System.out.println();
    }

    boolean DPSolvable() {
        Collections.sort(R);
        Collections.sort(G);
        Collections.sort(B);

        int nR = R.size() + 1;
        int nG = G.size() + 1;
        int nB = B.size() + 1;
        boolean[][][] T = new boolean[nR][nG][nB];
        // no tiles left = true;
        T[0][0][0] = true;

        for (int z = 0; z < nB; z++) {
            for (int y = 0; y < nG; y++) {
                for (int x = 0; x < nR; x++) {
                    // Don't change T[0][0][0]!
                    if (x > 0 || y > 0 || z > 0) {
                        boolean able = false;

                        // Match of three colors
                        if (x > 0 && y > 0 && z > 0
                                && R.get(x - 1) == G.get(y - 1)
                                && G.get(y - 1) == B.get(z - 1)
                                && T[x - 1][y - 1][z - 1]) {
                            able = true;
                        }

                        // Red Run
                        for (int i = 1; i <= x - 2; i++) {
                            if (validRun(i - 1, x - 1, R)
                                    && T[i - 1][y][z]) {
                                able = true;
                            }
                        }
                        // Green Run
                        for (int i = 1; i <= y - 2; i++) {
                            if (validRun(i - 1, y - 1, G)
                                    && T[x][i - 1][z]) {
                                able = true;
                            }
                        }
                        // Blue Run
                        for (int i = 1; i <= z - 2; i++) {
                            if (validRun(i - 1, z - 1, B)
                                    && T[x][y][i - 1]) {
                                able = true;
                            }
                        }

                        T[x][y][z] = able;
                    }
                }
            }
        }
        //System.out.println(Arrays.deepToString(T));

        // calculate solution from recurrence variable

        int pR = nR - 1;
        int pG = nG - 1;
        int pB = nB - 1;
        // if there exists a solution, construct one
        if (T[pR][pG][pB]) {
            while (pR != 0 || pG != 0 || pB != 0) {
                // check for match
                if (pR > 0 && pG > 0 && pB > 0
                        && T[pR - 1][pG - 1][pB - 1]
                        && R.get(pR - 1) == G.get(pG - 1)
                        && G.get(pG - 1) == B.get(pB - 1)) {
                    // there exists a match, now find it!
                    System.out.print("Match:   \t");
                    System.out.print(R.get(pR - 1) + " ");
                    System.out.print(G.get(pG - 1) + " ");
                    System.out.print(B.get(pB - 1) + " ");
                    System.out.println();
                    pR--;
                    pG--;
                    pB--;
                }
                // check for Red Run
                for (int r = 1; r <= pR - 2; r++) {
                    if (T[r - 1][pG][pB]
                            && validRun(r - 1, pR - 1, R)) {
                        System.out.print("Red run: \t");
                        printList(r - 1, pR, R);
                        pR = r - 1;
                    }
                }
                // check for Green Run
                for (int g = 1; g <= pG - 2; g++) {
                    if (T[pR][g - 1][pB]
                            && validRun(g - 1, pG - 1, G)) {
                        System.out.print("Green run: \t");
                        printList(g - 1, pG, G);
                        pG = g - 1;
                    }
                }
                // check for Blue Run
                for (int b = 1; b <= pB - 2; b++) {
                    if (T[pR][pG][b - 1]
                            && validRun(b - 1, pB - 1, B)) {
                        System.out.print("Blue run: \t");
                        printList(b - 1, pB, B);
                        pB = b - 1;
                    }
                }
            }
            System.out.println();
        }

        return T[nR - 1][nG - 1][nB - 1];
    }

    public static void main(String[] args) {
        Rummikub_DP rk = new Rummikub_DP();
        int[] red = {
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10
        };
        int[] green = {
                2, 3, 4, 5, 6
        };
        int[] blue = {
                6, 5
        };
        rk.addR(red);
        rk.addG(green);
        rk.addB(blue);
        rk.printList(rk.R);
        rk.printList(rk.G);
        rk.printList(rk.B);
        System.out.println();

        System.out.println(rk.DPSolvable());
    }
}
