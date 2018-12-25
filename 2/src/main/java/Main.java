import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        long twice = 0;
        long thrice = 0;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/input.txt")))) {
            while(reader.ready()) {
                String line = reader.readLine();

                Map<Character, Long> current = new HashMap<>();
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (current.containsKey(c)) {
                        current.put(c, current.get(c) + 1);
                    } else {
                        current.put(c, 1L);
                    }
                }

                boolean twiceAdded = false;
                boolean thriceAdded = false;

                for (Character c : current.keySet()) {
                    long count = current.get(c);
                    if (count == 2 && !twiceAdded) {
                        twice++;
                        twiceAdded = true;
                    } else if (count == 3 && !thriceAdded) {
                        thrice++;
                        thriceAdded = true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(twice);
        System.out.println(thrice);
        System.out.println(twice * thrice);
    }
}
