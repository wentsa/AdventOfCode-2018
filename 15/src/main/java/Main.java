import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main {
    static List<Goblin> goblins = new ArrayList<>();
    static List<Elf> elves = new ArrayList<>();
    static State[][] map;
    static int w = 0, h = 0;

    public static void main(String[] args) {


        List<String> lines = new LinkedList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/alg.txt")))) {
            while(reader.ready()) {
                String line = reader.readLine();
                w = Math.max(w, line.length());
                h++;
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // true -> volno
        map = new State[w][h];

        for (int y = 0; y < h; y++) {
            String line = lines.get(y);
            for (int x = 0; x < w; x++) {
                char c = line.charAt(x);
                if (c == 'G') {
                    goblins.add(new Goblin(new Position(x, y)));
                    map[x][y] = State.NPC;
                } else if (c == 'E') {
                    elves.add(new Elf(new Position(x, y)));
                    map[x][y] = State.NPC;
                } else if (c == '#') {
                    map[x][y] = State.WALL;
                } else {
                    map[x][y] = State.FREE;
                }
            }
        }

        int i = 0;
        for (;; i++) {
            List<Npc> npcs = new ArrayList<>(goblins);
            npcs.addAll(elves);

            npcs.sort((o1, o2) -> {
                int y = o1.p.y - o2.p.y;
                if (y == 0) {
                    return o1.p.x - o2.p.x;
                }
                return y;
            });

            for (Npc n : npcs) {
                findRoute(n).ifPresent(positions -> n.move(positions.get(0)));
            }

            break; // TODO
        }

    }

    enum State {
        WALL, FREE, NPC
    }

    static class Npc {
        int hp = 200, ap = 3;
        final Position p;

        Npc(Position p) {
            this.p = p;
        }

        public void move(Position position) {
            p.x = position.x;
            p.y = position.y;
        }
    }

    static class Goblin extends Npc {
        Goblin(Position p) {
            super(p);
        }
    }
    static class Elf extends Npc {
        Elf(Position p) {
            super(p);
        }
    }
    static class Position {
        int x, y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return x + "," + y;
        }
    }


    static class PositionCounter {
        final Position p;
        final int counter;

        PositionCounter(Position p, int counter) {
            this.p = p;
            this.counter = counter;
        }

        @Override
        public String toString() {
            return p.x + "," + p.y + "," + counter;
        }
    }

    static Optional<List<Position>> findRoute(Npc npc) {
        List<? extends Npc> enemies = npc instanceof Elf ? goblins : elves;

        List<Position> result = null;

        for (Npc e : enemies) {
            LinkedList<PositionCounter> list = new LinkedList<>();
            list.add(new PositionCounter(e.p, 0));

            boolean found = false;

            int lastListSize = 0;

            outer:
            while (true) {
                if (list.size() == lastListSize) {
                    break;
                }
                lastListSize = list.size();

                LinkedList<PositionCounter> listCopy = new LinkedList<>(list);
                for (PositionCounter current : listCopy) {
                    List<PositionCounter> adjacent = adjacent(current, npc);
                    Iterator<PositionCounter> adjIt = adjacent.iterator();
                    while (adjIt.hasNext()) {
                        PositionCounter adj = adjIt.next();
                        boolean removed = false;
                        for (PositionCounter p : list) {
                            if (p.p.x == adj.p.x && p.p.y == adj.p.y && p.counter <= adj.counter) {
                                adjIt.remove();
                                removed = true;
                                break;
                            }
                        }
                        if (!removed) {
                            list.addLast(adj);
                            if (adj.p.x == npc.p.x && adj.p.y == npc.p.y) {
                                found = true;
                                break outer;
                            }
                        }
                    }
                }
            }

            if (found) {
                List<Position> partResult = new LinkedList<>();
                Position current = npc.p;
                while (true) {
                    List<PositionCounter> adjacent = new LinkedList<>();
                    for (PositionCounter p : list) {
                        if (isAdjacent(p.p, current)) {
                            adjacent.add(p);
                        }
                    }
                    PositionCounter fin = adjacent.stream()
                            .filter(positionCounter -> positionCounter.p.x == e.p.x && positionCounter.p.y == e.p.y)
                            .findFirst()
                            .orElse(null);

                    if (fin != null) {
                        break;
                    }

                    adjacent.sort(Comparator.comparingInt(o -> o.counter));
                    current = adjacent.get(0).p;
                    partResult.add(current);
                }

                if (result == null || partResult.size() < result.size()) {
                    result = partResult;
                }
            }
        }

        return Optional.ofNullable(result);

    }

    private static boolean isAdjacent(Position p, Position p2) {
        return
                (p.x == p2.x && Math.abs(p.y - p2.y) == 1) ||
                (p.y == p2.y && Math.abs(p.x - p2.x) == 1);
    }

    private static List<PositionCounter> adjacent(PositionCounter current, Npc npc) {
        List<PositionCounter> adjacent = new LinkedList<>();
        adjacent.add(new PositionCounter(new Position(current.p.x - 1, current.p.y), current.counter + 1));
        adjacent.add(new PositionCounter(new Position(current.p.x + 1, current.p.y), current.counter + 1));
        adjacent.add(new PositionCounter(new Position(current.p.x, current.p.y - 1), current.counter + 1));
        adjacent.add(new PositionCounter(new Position(current.p.x, current.p.y + 1), current.counter + 1));

        return adjacent.stream()
                .filter(p ->
                        p.p.x >= 0
                                && p.p.x < w
                                && p.p.y >= 0
                                && p.p.y < h
                                && (map[p.p.x][p.p.y] == State.FREE || (p.p.x == npc.p.x && p.p.y == npc.p.y))
                )
                .collect(Collectors.toList());
    }

}
