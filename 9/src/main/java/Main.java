import java.util.*;

public class Main {

    private static int playersCount = 493, lastMarble = 7186300;
//    private static int playersCount = 9, lastMarble = 25;
//    private static int playersCount = 10, lastMarble = 1618;
//    private static int playersCount = 30, lastMarble = 5807;

    static int currentMarbleIdx = 0;
    static int currentPlayer = 1;
    static ArrayList<Integer> field = new ArrayList<>();

    public static void main(String[] args) {
        Map<Integer, Integer> playerScore = new HashMap<>();
        for (int i = 1; i <= playersCount; i++) {
            playerScore.put(i, 0);
        }

        field.add(0);

        for (int marble = 1; marble <= lastMarble; marble++) {
            if (marble % 10000 == 0) {
                System.out.println(marble);
            }
            if (marble % 23 == 0) {
                int removeIdx = (currentMarbleIdx - 7) % field.size();
                if (removeIdx < 0) {
                    removeIdx += field.size();
                }
                int score = playerScore.get(currentPlayer) + marble + field.get(removeIdx);
                field.remove(removeIdx);
                playerScore.put(currentPlayer, score);

                currentMarbleIdx = removeIdx % field.size();

            } else {
                int insertIdx = (currentMarbleIdx + 2) % (field.size() + 1);
                if (insertIdx == 0) {
                    insertIdx++;
                }
                field.add(insertIdx, marble);

                currentMarbleIdx = insertIdx;
            }

//            print();

            currentPlayer = (currentPlayer % playersCount) + 1;
        }

        System.out.println("\n");
        List<Integer> idxs = new ArrayList<>(playerScore.keySet());
        idxs.sort((o1, o2) -> playerScore.get(o2) - playerScore.get(o1));
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
        for (int i = 0; i < field.size(); i++) {
            System.out.print(i == currentMarbleIdx ? ("(" + field.get(i) + ")") : (" " + field.get(i) + " "));
        }
        System.out.println();
    }
}
