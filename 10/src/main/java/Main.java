import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.*;
import java.util.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Point> points = new LinkedList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/input.txt")))) {
            while(reader.ready()) {
                String line = reader.readLine();

                String[] position = line.split("<")[1].split(">")[0].split(",");
                String[] velocity = line.split(">")[1].split("<")[1].split(",");

                Point p = new Point();
                p.x = Integer.valueOf(position[0].trim());
                p.y = Integer.valueOf(position[1].trim());
                p.vx = Integer.valueOf(velocity[0].trim());
                p.vy = Integer.valueOf(velocity[1].trim());
                points.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 100_000; i++) {
            if (i >= 10639) {
                outer:
                for (int k = 0; k < points.size(); k++) {
                    Point p = points.get(k);
                    int count = 0;
                    for (int j = k + 1; j < points.size(); j++) {
                        if (p.x == points.get(j).x && Math.abs(p.y - points.get(j).y) < 7) {
                            count++;
                            if (count == 4) {
                                print(points, i);
                                break outer;
                            }
                        }
                    }
                }
            }

            for(Point p : points) {
                p.x += p.vx;
                p.y += p.vy;
            }
        }
    }

    private static void print(List<Point> points, int id) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = -Integer.MAX_VALUE, maxY = -Integer.MAX_VALUE;

        for(Point p : points) {
            minX = Math.min(minX, p.x);
            minY = Math.min(minY, p.y);
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);
        }


//        try(PrintWriter writer = new PrintWriter("out" + id + ".txt", "UTF-8")) {
//            for (int y = minY; y < maxY; y++) {
//                for (int x = minX; x < maxX; x++) {
//                    boolean found = false;
//                    for(Point p : points) {
//                        if (p.x == x && p.y == y) {
//                            found = true;
//                            break;
//                        }
//                    }
//                    writer.print(found ? "X" : " ");
//                }
//                writer.println();
//            }
//
//        } catch (FileNotFoundException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        int w = maxX - minX + 1;
        int h = maxY - minY + 1;

        IndexColorModel colorModel = new IndexColorModel(
                1,                           // bits per pixel
                2,                           // number of colors
                new byte[]{0, (byte) 0xff},  // red values
                new byte[]{0, (byte) 0xff},  // green values
                new byte[]{0, (byte) 0xff}); // blue values
        BufferedImage image = new BufferedImage(w, h,
                BufferedImage.TYPE_BYTE_BINARY, colorModel);

        BufferedImage imageOut = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                boolean found = false;
                for(Point p : points) {
                    if (p.x == x + minX && p.y == y + minY) {
                        found = true;
                        break;
                    }
                }
                imageOut.setRGB(x, y, found ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }

        try {
            ImageIO.write(imageOut, "png", new File("image_" + id + ".png"));
        } catch (IOException e) {
            System.out.println("Some exception occured " + e);
        }
    }



    static class Point {
        int x, y;
        int vx, vy;
    }
}
