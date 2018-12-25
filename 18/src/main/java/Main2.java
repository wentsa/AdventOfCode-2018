import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main2 {

    static final char OPEN = '.';
    static final char TREE = '|';
    static final char YARD = '#';

    public static void main(String[] args) {
        List<String> lines = new LinkedList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main2.class.getResourceAsStream("/input.txt")))) {
            while(reader.ready()) {
                String line = reader.readLine();
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int w = lines.get(0).length();
        int h = lines.size();

        Field[][] map = new Field[w][h];
        for (int y = 0; y < h; y++) {
            String line = lines.get(y);
            for (int x = 0; x < w; x++) {
                map[x][y] = new Field(line.charAt(x));
            }
        }

//        print(map, w, h);
        long start = System.currentTimeMillis();

        int time = 1_000_000_000;
        for (int i = 0; i < time; i++) {
            if (i % 100_000 == 0) {
                double portion = (double) i / time;
                long duration = System.currentTimeMillis() - start;
                double totalTime = duration / portion;
                long remainingMillis = (long) (totalTime - duration);

                long hours = TimeUnit.MILLISECONDS.toHours(remainingMillis);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMillis) - (hours * 60);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingMillis) - ((hours * 60 * 60) + (minutes * 60));

                String remainingTime = String.format("%d:%d:%d", hours, minutes, seconds);
                System.out.println(i + " = " + String.format("%.2f" ,100 * portion) + "%, " + remainingTime);

                print(map, w, h);
            }
            map = step(map, w, h);
        }

        int trees = 0, opens = 0, yards = 0;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Field f = map[x][y];
                switch (f.type) {
                    case OPEN: {
                        opens++;
                    } break;
                    case TREE: {
                        trees++;
                    } break;
                    case YARD: {
                        yards++;
                    } break;
                }
            }
        }

        System.out.println(trees * yards);
    }

    private static Field[][] step(Field[][] map, int w, int h) {
        Field[][] modified = new Field[w][h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                modified[x][y] = modify(map, w, h, x, y);
            }
        }
        return modified;
    }

    private static Field modify(Field[][] map, int w, int h, int x, int y) {
        Field f = map[x][y];
        Surroundings surroundings = getSurroundings(map, w, h, x, y);
        switch (f.type) {
            case OPEN: {
                if (surroundings.tree >= 3) {
                    return new Field(TREE);
                }
                return f;
            }
            case TREE: {
                if (surroundings.yard >= 3) {
                    return new Field(YARD);
                }
                return f;
            }
            case YARD: {
                if (surroundings.yard >= 1 && surroundings.tree >= 1) {
                    return f;
                }
                return new Field(OPEN);
            }
        }
        return null;
    }

    static void print(Field[][] map, int w, int h) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                System.out.print(map[x][y].type);
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }

    private static Surroundings getSurroundings(Field[][] map, int w, int h, int x, int y) {
        Surroundings surroundings = new Surroundings();
        for (int y2 = Math.max(0, y - 1); y2 <= Math.min(y + 1, h - 1); y2++) {
            for (int x2 = Math.max(0, x - 1); x2 <= Math.min(x + 1, w - 1); x2++) {
                if (x != x2 || y != y2) {
                    Field f = map[x2][y2];
                    switch (f.type) {
                        case OPEN: {
                            surroundings.open++;
                        } break;
                        case TREE: {
                            surroundings.tree++;
                        } break;
                        case YARD: {
                            surroundings.yard++;
                        } break;
                    }
                }
            }
        }
        return surroundings;
    }

    static class Field {
        final char type;

        Field(char type) {
            this.type = type;
        }
    }

    static class Surroundings {
        int open = 0, tree = 0, yard = 0;
    }
}
