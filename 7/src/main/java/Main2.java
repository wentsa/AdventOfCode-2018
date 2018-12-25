import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main2 {

    public static void main(String[] args) {
        List<Line> lines = new LinkedList<>();

        Set<String> nodes = new HashSet<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main2.class.getResourceAsStream("/input.txt")))) {
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
        LinkedList<String> available = new LinkedList<>();
        List<String> inProgress = new LinkedList<>();
        List<Worker> workers = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            workers.add(new Worker());
        }

        // available.pollFirst();

        int i = 0;
        for (;; i++) {
            if (steps.size() == nodesOrdered.size()) {
                break;
            }

            for (Worker w : workers) {
                if (w.job != null) {
                    if (--w.remaining == 0) {
                        steps.add(w.job);
                        for (int x = 0; x < nodes.size(); x++) {
                            graph[nodesOrdered.indexOf(w.job)][x] = false;
                        }
                        w.job = null;
                    }
                }
            }

            for (int to = 0; to < nodes.size(); to++) {
                if (
                        !steps.contains(nodesOrdered.get(to))
                        && !inProgress.contains(nodesOrdered.get(to))
                        && !available.contains(nodesOrdered.get(to))
                ) {
                    boolean found = true;
                    for (int from = 0; from < nodes.size(); from++) {
                        if (graph[from][to]) {
                            found = false;
                            break;
                        }
                    }

                    if (found) {
                        available.addLast(nodesOrdered.get(to));
                    }
                }
            }

            for (Worker w : workers) {
                if (w.job == null) {
                    String job = available.pollFirst();
                    if (job != null) {
                        w.job = job;
                        w.remaining = 61 + nodesOrdered.indexOf(job);
                        inProgress.add(job);
                    }
                }
            }


            System.out.println(i + " " + workers.get(0).job + " " +  workers.get(1).job + " " +  steps.stream().reduce(String::concat).orElse(""));
        }


        for (String s : steps) {
            System.out.print(s);
        }

        System.out.println();

        System.out.println(--i);

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

    static class Worker {
        String job;
        int remaining;
    }
}
