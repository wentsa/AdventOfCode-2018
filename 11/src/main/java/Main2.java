import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Main2 {
    static int serialNumber = 7672;
    //    static int serialNumber = 42;
    static int maxSize = 300;
    static int[][] field = new int[maxSize][maxSize];

    public static void main(String[] args) {
        assert calculatePower(3, 5, 8) == 4;
        assert calculatePower(122, 79, 57) == -5;
        assert calculatePower(217, 196, 39) == 0;
        assert calculatePower(101, 153, 71) == 4;

        for (int y = 0; y < maxSize; y++) {
            for (int x = 0; x < maxSize; x++) {
                field[x][y] = calculatePower(x + 1, y + 1, serialNumber);
            }
        }

//        print();

        List<Result> results = new LinkedList<>();
        for (int i = 1; i <= maxSize; i++) {
            results.add(printMaxSumSub(field, i));
        }

        results.sort((o1, o2) -> o2.score - o1.score);

        System.out.println(results.get(0));
    }

    private static void print() {
        for (int y = 0; y < maxSize; y++) {
            for (int x = 0; x < maxSize; x++) {
                System.out.print(String.format("%3d", field[x][y]));
            }
            System.out.println();
        }
    }

    private static int calculatePower(int x, int y, int serialNo) {
        int rackId = x + 10;
        int power = rackId * y;
        power += serialNo;
        power *= rackId;

        power /= 100;
        power %= 10;

        return power - 5;
    }


    static int N = maxSize;
    // A O(n^2) function to the maximum sum sub-
// squares of size k x k in a given square
// matrix of size n x n
    static Result printMaxSumSub(int[][] mat, int k) {

        // 1: PREPROCESSING
        // To store sums of all strips of size k x 1
        int stripSum[][] = new int[N][N];

        // Go column by column
        for (int j=0; j<N; j++) {
            // Calculate sum of first k x 1 rectangle
            // in this column
            int sum = 0;
            for (int i=0; i<k; i++)
                sum += mat[i][j];
            stripSum[0][j] = sum;

            // Calculate sum of remaining rectangles
            for (int i=1; i<N-k+1; i++)
            {
                sum += (mat[i+k-1][j] - mat[i-1][j]);
                stripSum[i][j] = sum;
            }
        }

        // max_sum stores maximum sum and its
        // position in matrix
        int max_sum = -Integer.MAX_VALUE;
        int posX = -1, posY = -1;

        // 2: CALCULATE SUM of Sub-Squares using stripSum[][]
        for (int i=0; i<N-k+1; i++)
        {
            // Calculate and print sum of first subsquare
            // in this row
            int sum = 0;
            for (int j = 0; j<k; j++)
                sum += stripSum[i][j];

            // Update max_sum and position of result
            if (sum > max_sum)
            {
                max_sum = sum;
                posX = i;
                posY = 0;
//                pos = &(mat[i][0]);
            }

            // Calculate sum of remaining squares in
            // current row by removing the leftmost
            // strip of previous sub-square and adding
            // a new strip
            for (int j=1; j<N-k+1; j++)
            {
                sum += (stripSum[i][j+k-1] - stripSum[i][j-1]);

                // Update max_sum and position of result
                if (sum > max_sum)
                {
                    max_sum = sum;
                    posX = i;
                    posY = j;
//                    pos = &(mat[i][j]);
                }
            }
        }

        Result r = new Result();
        r.x = posX + 1;
        r.y = posY + 1;
        r.d = k;
        r.score = max_sum;

//        System.out.println((posX + 1) + ":" + (posY + 1));
//        System.out.println(max_sum);
//
//        // Print the result matrix
//        for (int i=0; i<k; i++) {
//            for (int j=0; j<k; j++) {
//                System.out.print(String.format("%3d", mat[posX + j][posY + i]));
////                cout << *(pos + i * N + j) << " ";
//            }
//            System.out.println();
//        }
        return r;
    }

    static class Result {
        int x, y, d, score;
    }

}
