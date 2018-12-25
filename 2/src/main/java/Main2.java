import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main2 {

    public static void main(String[] args) {
        List<String> list = new LinkedList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main2.class.getResourceAsStream("/input.txt")))) {
            while(reader.ready()) {
                String line = reader.readLine();

                list.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list.size(); i++) {
            for (int k = i + 1; k < list.size(); k++) {
                int diff = 0;
                for (int cc = 0; cc < list.get(i).length(); cc++) {
                    if (list.get(i).charAt(cc) != list.get(k).charAt(cc)) {
//                        System.out.println(cc + " " + list.get(i).charAt(cc));
                        diff++;
                    }
                }
                if (diff <= 1) {
                    System.out.println(list.get(i) + " " + list.get(k));
                }
            }
        }
    }
}
