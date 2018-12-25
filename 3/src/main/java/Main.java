import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Claim> list = new LinkedList<>();
        int maxW = 0;
        int maxH = 0;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/input.txt")))) {
            while(reader.ready()) {
                String line = reader.readLine();

                Claim c = new Claim();
                String[] s = line.split("@");
                c.id = s[0].substring(1).trim();

                s = s[1].split(":");
                String[] pos = s[0].split(",");
                String[] dim = s[1].split("x");

                c.x = Integer.valueOf(pos[0].trim());
                c.y = Integer.valueOf(pos[1].trim());

                c.w = Integer.valueOf(dim[0].trim());
                c.h = Integer.valueOf(dim[1].trim());

                maxW = Math.max(maxW, c.x + c.w);
                maxH = Math.max(maxH, c.y + c.h);

                list.add(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[][] arr = new int[maxW][maxH];
        for (int x = 0; x < maxW; x++) {
            for (int y = 0; y < maxH; y++) {
                arr[x][y] = 0;
            }
        }

//        print(arr, maxW, maxH);

        for (int i = 0; i < list.size(); i++) {
            for (int k = i + 1; k < list.size(); k++) {
                Claim c1 = list.get(i);
                Claim c2 = list.get(k);

                for (int x = c1.x; x < c1.x + c1.w; x++) {
                    for (int y = c1.y; y < c1.y + c1.h; y++) {
                        if (c2.isMy(x, y)) {
                            arr[x][y] = 1;
                        }
                    }
                }
            }
        }
//        for (Claim c : list) {
//            for (int x = c.x; x < c.x + c.w; x++) {
//                for (int y = c.y; y < c.y + c.h; y++) {
//                    arr[x][y] = 1; // TODO blbe, vybarvit pouze prekryvy
//                }
//            }
//        }

//        print(arr, maxW, maxH);

        int count = 0;
//        for (int y = 0; y < maxH; y++) {
//            for (int x = 0; x < maxW; x++) {
//                if(arr[x][y] == 1) {
//                    count++;
////                    print(arr, maxW, maxH);
//                    fill(arr, x, y, maxW, maxH);
////                    print(arr, maxW, maxH);
//                }
//            }
//        }
        for (int y = 0; y < maxH; y++) {
            for (int x = 0; x < maxW; x++) {
                if(arr[x][y] == 1) {
                    count++;
                }
            }
        }

        System.out.println(count);
    }

    static void fill(int[][] arr, int x, int y, int maxW, int maxH) {
        if (x >= 0 && x < maxW && y >= 0 && y < maxH) {
            int currentColor = arr[x][y];
            if (currentColor == 1) {
                arr[x][y] = 2;
                fill(arr, x + 1, y, maxW, maxH);
                fill(arr, x - 1, y, maxW, maxH);
                fill(arr, x, y + 1, maxW, maxH);
                fill(arr, x, y - 1, maxW, maxH);
            }
        }
    }

    static void print(int[][] arr, int w, int h) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                System.out.print(arr[x][y]);
            }
            System.out.println();
        }
    }

    private static class Claim {
        String id;
        int x, y, w, h;

        boolean isMy(int x2, int y2) {
            return x2 >= x && x2 < x + w && y2 >= y && y2 < y + h;
        }
    }
}
