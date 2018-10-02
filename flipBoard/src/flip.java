import java.util.Arrays; 

class Board {
    private boolean array[];

    private Board(int a){
        this.array = new boolean[a + 1];
        Arrays.fill(this.array, Boolean.FALSE);
    }
    private void flipAndPrint() {
        for (int i = 2; i < array.length; i++) {
            for (int j = i; j < array.length; j = j + i) {
                array[j] = !array[j];
            }
        }
        for (int i = 1; i < array.length; i++) {
            if (!array[i]) {
                System.out.println(i + "\t--> " + (int)Math.sqrt(i));
            }
        }
    }

    public static void main(String[] args) {
        Board b = new Board(400);
        b.flipAndPrint();
    }
}
    
  
  
  
  