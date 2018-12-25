import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Main2 {

    public static void main(String[] args) {

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main2.class.getResourceAsStream("/input.txt")))) {
            List<Rule> rules = new LinkedList<>();
            boolean[] plants;
            int size, zeroIdx;
            long generations = 50000000000L;

            String line = reader.readLine();

            String initialState = line.split(":")[1].substring(1);

            size = initialState.length() * 50000;
            zeroIdx = initialState.length() * 20;
            plants = new boolean[size];

            for (int i = 0; i < size; i++) {
                if (i >= zeroIdx && i < zeroIdx + initialState.length()) {
                    plants[i] = initialState.charAt(i - zeroIdx) == '#';
                } else {
                    plants[i] = false;
                }
            }

            reader.readLine();

            while(reader.ready()) {
                line = reader.readLine();
                String[] s = line.split(" => ");

                Rule r = new Rule();
                for (int i = 0; i < 5; i++) {
                    r.rule[i] = s[0].charAt(i) == '#';
                }
                r.result = s[1].charAt(0) == '#';

                rules.add(r);
            }

//            print(plants, size);

            for (long i = 0; i < generations; i++) {
                boolean[] newArr = new boolean[size];
                boolean[] finalPlants = plants;

                newArr[0] = newArr[1] = newArr[size - 2] = newArr[size - 1] = false;
                for (int j = 2; j < size - 2; j++) {
                    int finalJ = j;

                    newArr[j] = rules.stream()
                            .map(rule -> rule.process(finalPlants, finalJ))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .findFirst()
                            .orElse(false);
                }
                plants = newArr;

                if (i % 100 == 0) {
//                    print(plants, size);
                }
            }

            print(plants, size);

            int score = 0;
            for (int i = 0; i < size; i++) {
                if (plants[i]) {
                    score += i - zeroIdx;
                }
            }

            System.out.println(score);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void print(boolean[] plants, int size) {
        for (int i = 0; i < size; i++) {
            System.out.print(plants[i] ? '#' : '.');
        }
        System.out.println();
    }

    static class Rule {
        boolean[] rule = new boolean[5];
        boolean result;

        Optional<Boolean> process(boolean[] arr, int i) {
            boolean r = true;
            for (int j = 0; j < 5; j++) {
                r = r && (arr[i - 2 + j] == rule[j]);
            }

            if (r) {
                return Optional.of(result);
            }
            return Optional.empty();
        }
    }

}
