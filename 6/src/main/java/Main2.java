import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main2 {

    public static void main(String[] args) {
        List<Point> points = new LinkedList<>();

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = -Integer.MAX_VALUE, maxY = -Integer.MAX_VALUE;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main2.class.getResourceAsStream("/input.txt")))) {
            int id = 0;
            while(reader.ready()) {
                String line = reader.readLine();

                Point p = new Point();
                p.x = Integer.valueOf(line.split(",")[0].trim());
                p.y = Integer.valueOf(line.split(",")[1].trim());
                p.id = id++;
                points.add(p);

                minX = Math.min(minX, p.x);
                minY = Math.min(minY, p.y);
                maxX = Math.max(maxX, p.x);
                maxY = Math.max(maxY, p.y);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        minX -= 150;
        minY -= 150;
        maxX += 150;
        maxY += 150;

        int w = maxX - minX;
        int h = maxY - minY;

        int[][] arr = new int[w][h];

        for (int y = minY; y < maxY; y++) {
            for (int x = minX; x < maxX; x++) {
                int totalDistance = 0;
                for (Point p : points) {
                    totalDistance += distance(x, y, p);
                }

                if (totalDistance < 10000) {
                    arr[x - minX][y - minY] = 0;
                } else {
                    arr[x - minX][y - minY] = -1;
                }
            }
        }

        int c = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int val = arr[x][y];
                if (val > -1) {
                    c++;
                }
            }
        }

        System.out.println(c);
    }

    static int distance(int x, int y, Point p) {
        return Math.abs(x - p.x) + Math.abs(y - p.y);
    }

    static class Point {
        int id;
        int x, y;
        int distanceActual;
    }
}
