import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String polymer = null;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/input.txt")))) {
            polymer = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < polymer.length(); i++) {
            int c = (int) polymer.charAt(i);
            if (c < 65 || c > 122 || c > 90 && c < 97) {
                System.out.println(polymer.charAt(i));
            }
        }

        if (polymer != null) {
            boolean shouldStop = false;

            while(!shouldStop) {
                shouldStop = true;
                for (int i = 65; i <= 90; i++) {
                    char large = (char) i;
                    char small = (char) (i + 32);

                    String g1 = "" + small + large;
                    String g2 = "" + large + small;

                    while (polymer.contains(g1) || polymer.contains(g2)) {
                        shouldStop = false;
                        polymer = polymer.replace(g1, "");
                        polymer = polymer.replace(g2, "");
                    }
                }
            }

            System.out.println(polymer.length());

            for (int i = 0; i < polymer.length() - 1; i++) {
                int c1 = polymer.charAt(i);
                int c2 = polymer.charAt(i + 1);
                if (Math.abs(c1 - c2) == 32) {
                    polymer = polymer.substring(0, i) + polymer.substring(i + 2);
                    i -= 2;
                }
            }

            System.out.println(polymer.length());
        }


    }
}
