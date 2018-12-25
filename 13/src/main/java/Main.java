import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Track[][] map;
        int w = 0, h = 0;

        List<String> lines = new LinkedList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/input.txt")))) {
            while(reader.ready()) {
                String line = reader.readLine();
                w = Math.max(w, line.length());
                h++;
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        map = new Track[w][h];

        List<Vehicle> vehicles = new LinkedList<>();

        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                Track track = null;
                Vehicle vehicle = null;

                char c = line.charAt(x);
                switch (c) {
                    case '/':
                    case '\\': {
                        track = new Bend(c);
                    } break;

                    case '-':
                    case '|': {
                        track = new Straight();
                    } break;

                    case '+': {
                        track = new Intersection();
                    } break;

                    case '<':
                    case '>':
                    case 'v':
                    case '^': {
                        track = new Straight();
                        vehicle = new Vehicle(x, y);
                    } break;
                }

                if (vehicle != null) {
                    Direction d = null;
                    switch (c) {
                        case '<': {
                            d = Direction.left;
                        } break;
                        case '>': {
                            d = Direction.right;
                        } break;
                        case 'v': {
                            d = Direction.down;
                        } break;
                        case '^': {
                            d = Direction.up;
                        } break;
                    }
                    vehicle.direction = d;
                    vehicles.add(vehicle);
                }

                map[x][y] = track;
            }
        }

        print(map, w, h, vehicles);

        outer:
        while(true) {
            vehicles.sort((o1, o2) -> {
                int y = o1.posY - o2.posY;
                if (y == 0) {
                    return o1.posX - o2.posX;
                }
                return y;
            });

            for (Vehicle v : vehicles) {
                v.move();
                Track t = map[v.posX][v.posY];
                if (t != null) {
                    t.process(v);
                }

                for(Vehicle v2 : vehicles) {
                    if (v != v2 && v.posY == v2.posY && v.posX == v2.posX) {
                        System.out.println(v.posX + "," + v.posY);
                        break outer;
                    }
                }
            }

//            print(map, w, h, vehicles);
        }

    }

    private static void print(Track[][] map, int w, int h, List<Vehicle> vehicles) {
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                boolean found = false;
                for (Vehicle v : vehicles) {
                    if (v.posX == x && v.posY == y) {
                        System.out.print('O');
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Track value = map[x][y];
                    System.out.print(value != null ? value : ' ');
                }
            }
            System.out.println();
        }

        System.out.println("\n");
    }

    enum Direction {
        up(0), left(3), right(1), down(2);

        int val;
        Direction(int i) {
            val = i;
        }
    }

    static Direction turnLeft(Direction c) {
        int current = c.val;
        current--;
        if (current < 0) {
            current += Direction.values().length;
        }
        int finalCurrent = current;
        return Arrays.stream(Direction.values())
                        .filter(direction -> direction.val == finalCurrent)
                        .findFirst()
                        .get();
    }

    static Direction turnRight(Direction c) {
        int current = c.val;
        current = (current + 1) % Direction.values().length;

        int finalCurrent = current;
        return Arrays.stream(Direction.values())
                        .filter(direction -> direction.val == finalCurrent)
                        .findFirst()
                        .get();
    }

    enum Turn {
        left, straight, right
    }

    static class Vehicle {
        Direction direction;
        int posX, posY;
        Turn next = null;

        Vehicle(int x, int y) {
            posX = x;
            posY = y;
        }

        void move() {
            switch (direction) {
                case up: {
                    posY--;
                } break;
                case down: {
                    posY++;
                } break;
                case left: {
                    posX--;
                } break;
                case right: {
                    posX++;
                } break;
            }
        }
    }

    abstract static class Track {
        abstract void process(Vehicle v);
    }

    static class Bend extends Track {
        final char type;

        Bend(char type) {
            this.type = type;
        }

        @Override
        void process(Vehicle v) {
            Direction r = null;
            switch (v.direction) {
                case up: {
                    if (type == '/') {
                        r = Direction.right;
                    } else {
                        r = Direction.left;
                    }
                } break;
                case down: {
                    if (type == '/') {
                        r = Direction.left;
                    } else {
                        r = Direction.right;
                    }
                } break;
                case left: {
                    if (type == '/') {
                        r = Direction.down;
                    } else {
                        r = Direction.up;
                    }
                } break;
                case right: {
                    if (type == '/') {
                        r = Direction.up;
                    } else {
                        r = Direction.down;
                    }
                } break;
            }

            v.direction = r;
        }

        @Override
        public String toString() {
            return type + "";
        }
    }

    static class Straight extends Track {

        @Override
        void process(Vehicle v) {}

        @Override
        public String toString() {
            return ".";
        }
    }

    static class Intersection extends Track {

        @Override
        void process(Vehicle v) {
            if (v.next != null) {
                switch (v.next) {
                    case left: {
                        v.next = Turn.straight;
                    }
                    break;
                    case straight: {
                        v.next = Turn.right;
                    }
                    break;
                    case right: {
                        v.next = Turn.left;
                    }
                }
            } else {
                v.next = Turn.left;
            }

            switch (v.next) {
                case straight: break;
                case left: {
                    v.direction = turnLeft(v.direction);
                } break;
                case right: {
                    v.direction = turnRight(v.direction);
                } break;
            }
        }

        @Override
        public String toString() {
            return "+";
        }
    }


}
