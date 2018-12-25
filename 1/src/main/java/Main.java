import java.io.*;

public class Main {

    public static void main(String[] args) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/input.txt")))) {
            long f = 0;
            while(reader.ready()) {
                String line = reader.readLine();
                f += Long.valueOf(line);
            }
            System.out.println(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
