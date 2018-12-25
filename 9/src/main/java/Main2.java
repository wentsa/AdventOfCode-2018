import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main2 {

    private static int playersCount = 493, lastMarble = 7186300;
//    private static int playersCount = 9, lastMarble = 25;
//    private static int playersCount = 10, lastMarble = 1618;
//    private static int playersCount = 30, lastMarble = 5807;

    static Node currentNode = null;
    static int currentPlayer = 1;

    public static void main(String[] args) {
        Map<Integer, Long> playerScore = new HashMap<>();
        for (int i = 1; i <= playersCount; i++) {
            playerScore.put(i, 0L);
        }

        Node first = new Node();
        first.marble = 0;
        first.next = first;
        first.prev = first;
        currentNode = first;

        for (int marble = 1; marble <= lastMarble; marble++) {
            if (marble % 10000 == 0) {
                System.out.println(marble);
            }
            if (marble % 23 == 0) {
                for (int i = 0; i < 7; i++) {
                    currentNode = currentNode.prev;
                }
                Node toRemove = currentNode;
                currentNode = currentNode.next;

                toRemove.prev.next = toRemove.next;
                toRemove.next.prev = toRemove.prev;

                long score = playerScore.get(currentPlayer) + marble + toRemove.marble;
                playerScore.put(currentPlayer, score);

            } else {
                currentNode = currentNode.next;

                Node newNode = new Node();
                newNode.marble = marble;
                newNode.prev = currentNode;
                newNode.next = currentNode.next;

                currentNode = newNode;
                currentNode.prev.next = currentNode;
                currentNode.next.prev = currentNode;
            }

//            print();

            currentPlayer = (currentPlayer % playersCount) + 1;
        }

        System.out.println("\n");
        List<Integer> idxs = new ArrayList<>(playerScore.keySet());
        idxs.sort((o1, o2) -> {
            long diff = playerScore.get(o2) - playerScore.get(o1);
            if (diff < 0) return -1;
            if (diff > 0) return  1;
            return 0;
        });
//        for (Integer i : idxs) {
//            int score = playerScore.get(i);
//            if (score > 0) {
//                System.out.println(i + ": " + score);
//            }
//        }
        System.out.println(idxs.get(0) + ": " + playerScore.get(idxs.get(0)));
    }

    private static void print() {
        System.out.print("[" + currentPlayer + "] ");
        Node myNode = currentNode;
        System.out.print("(" + myNode.marble + ")");
        myNode = myNode.next;
        while(myNode != currentNode) {
            System.out.print(" " + myNode.marble + " ");
            myNode = myNode.next;
        }
        System.out.println();
    }

    static class Node {
        int marble;
        Node prev, next;
    }
}
