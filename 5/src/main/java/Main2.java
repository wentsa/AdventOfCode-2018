import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main2 {

    public static void main(String[] args) {
        String polymer = null;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main2.class.getResourceAsStream("/input.txt")))) {
            polymer = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (polymer != null) {

            int min = Integer.MAX_VALUE;

            for (int k = 0; k < 26; k++) {
                String copy = polymer.replaceAll("[" + (char) (k + 'a') + (char) (k + 'A') + "]", "");
                for (int i = 0; i < copy.length() - 1; i++) {
                    if (i < 0) {
                        continue;
                    }
                    int c1 = copy.charAt(i);
                    int c2 = copy.charAt(i + 1);
                    if (Math.abs(c1 - c2) == 32) {
                        copy = copy.substring(0, i) + copy.substring(i + 2);
                        i -= 2;
                    }
                }
                min = Math.min(min, copy.length());
            }

            System.out.println(min);
        }


    }
}
