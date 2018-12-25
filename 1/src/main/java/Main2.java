import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Main2 {

    public static void main(String[] args) {
        List<Long> list = new LinkedList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main2.class.getResourceAsStream("/input.txt")))) {
            while(reader.ready()) {
                String line = reader.readLine();
                list.add(Long.valueOf(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<Long> known = new HashSet<>();
        known.add(0L);

        long f = 0;
        outerLoop: while(true) {
            System.out.println("O");
            for (Long c : list) {
                f += c;
                if (f == 464) {
                    System.out.println("X");
                }
                if (known.contains(f)) {
                    System.out.println(f);
                    break outerLoop;
                }
                known.add(f);
            }
        }
    }
}
