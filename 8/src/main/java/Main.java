import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

public class Main {

    private static int idCounter = 0;
    private static Node rootNode;

    public static void main(String[] args) {
        String g = null;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/input.txt")))) {
            g = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (g != null) {
            AtomicInteger count = new AtomicInteger(0);

            int[] s = Arrays.stream(g.split(" ")).mapToInt(Integer::valueOf).peek(value -> count.incrementAndGet()).toArray();

            rootNode = getNode(s, count.get());

            System.out.println(getMetadata(rootNode));
        }
    }

    private static int getMetadata(Node node) {
        return Stream.concat(
                node.metadata.stream(),
                node.nodes.stream().map(Main::getMetadata)
        ).mapToInt(value -> value).sum();
    }

    static Node getNode(int[] s, int l) {
        int childNodesCount = s[0];
        int metadataCount = s[1];

        Node n = new Node();
        n.id = idCounter++;


        int childLength = 0;
        for (int i = 0; i < childNodesCount; i++) {
            int[] newA = Arrays.copyOfRange(s, childLength + 2, l);
            Node child = getNode(newA, l - 2 - childLength);
            childLength += child.length;
            n.nodes.add(child);
        }

        for (int i = 0; i < metadataCount; i++) {
            n.metadata.add(s[i + 2 + childLength]);
        }

        n.length = 2 + metadataCount + childLength;

        return n;
    }

    static class Node {

        int id, length;
        List<Node> nodes = new LinkedList<>();
        List<Integer> metadata = new LinkedList<>();
    }
}
