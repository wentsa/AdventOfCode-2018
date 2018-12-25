import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        List<Line> lines = new LinkedList<>();

        Set<String> nodes = new HashSet<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/input.txt")))) {
            while(reader.ready()) {
                String line = reader.readLine();

                Line l = new Line();
                l.from = line.substring(5, 6);
                l.to = line.substring(36, 37);

                nodes.add(l.from);
                nodes.add(l.to);

                lines.add(l);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> nodesOrdered = new ArrayList<>(nodes);
        nodesOrdered.sort((str1, str2) -> {
            int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
            if (res == 0) {
                res = str1.compareTo(str2);
            }
            return res;
        });

        boolean[][] graph = new boolean[nodes.size()][nodes.size()];
        for (int from = 0; from < nodes.size(); from++) {
            for (int to = 0; to < nodes.size(); to++) {
                graph[from][to] = false;
                for (Line l : lines) {
                    if (l.from.equals(nodesOrdered.get(from)) && l.to.equals(nodesOrdered.get(to))) {
                        graph[from][to] = true;
                    }
                }
            }
        }

//        print(graph, nodesOrdered);

        List<String> steps = new LinkedList<>();

        while (true) {
            if (steps.size() == nodes.size()) {
                break;
            }

            for (int to = 0; to < nodes.size(); to++) {
                if (!steps.contains(nodesOrdered.get(to))) {
                    boolean found = true;
                    for (int from = 0; from < nodes.size(); from++) {
                        if (graph[from][to]) {
                            found = false;
                            break;
                        }
                    }

                    if (found) {
                        System.out.println(nodesOrdered.get(to));

                        steps.add(nodesOrdered.get(to));

                        for (int x = 0; x < nodes.size(); x++) {
                            graph[to][x] = false;
                        }

                        print(graph, nodesOrdered);
                        System.out.println();

                        break;
                    }
                }
            }
        }

        for (String s : steps) {
            System.out.print(s);
        }

    }

    private static void print(boolean[][] graph, List<String> nodes) {
        System.out.print("  ");
        for (String node : nodes) {
            System.out.print(node + " ");
        }
        System.out.println();

        for (int from = 0; from < nodes.size(); from++) {
            System.out.print(nodes.get(from) + " ");
            for (int to = 0; to < nodes.size(); to++) {
                System.out.print((graph[from][to] ? 1 : 0) + " ");
            }
            System.out.println();
        }
    }

    static class Line {
        String from, to;
    }
}
