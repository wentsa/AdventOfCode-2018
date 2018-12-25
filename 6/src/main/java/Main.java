import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        List<Point> points = new LinkedList<>();

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = -Integer.MAX_VALUE, maxY = -Integer.MAX_VALUE;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/input.txt")))) {
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
                Point closest = null;
                int count = 0;
                for (Point p : points) {
                    int d = distance(x, y, p);
                    if (closest == null) {
                        closest = p;
                        closest.distanceActual = d;
                        count = 1;
                    } else {
                        if (d == closest.distanceActual) {
                            count++;
                        } else if (d < closest.distanceActual) {
                            closest = p;
                            closest.distanceActual = d;
                            count = 1;
                        }
                    }
                }
                if (count > 1) {
                    arr[x - minX][y - minY] = -1;
                } else {
                    arr[x - minX][y - minY] = closest.id;
                }
            }
        }

        Map<Integer, Integer> areas = new HashMap<>();
        Set<Integer> banned = new HashSet<>();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int val = arr[x][y];
                if (val > -1) {
                    if (y == 0 || y == h - 1 || x == 0 || x == w - 1) {
                            banned.add(val);
                    } else {
                        if (!areas.containsKey(val)) {
                            areas.put(val, 1);
                        } else {
                            areas.put(val, areas.get(val) + 1);
                        }
                    }
                }
            }
        }

        int maxValue = -1;
        for (Integer k : areas.keySet()) {
            if (!banned.contains(k)) {
                if (areas.get(k) > maxValue) {
                    maxValue = areas.get(k);
                }
            }
        }

        System.out.println(maxValue);
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
