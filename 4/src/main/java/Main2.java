import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main2 {

    public static void main(String[] args) throws ParseException {
        List<String> lines = new LinkedList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main2.class.getResourceAsStream("/input.txt")))) {
            while(reader.ready()) {
                String line = reader.readLine();

                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        lines.sort((o1, o2) -> {
            String d1 = o1.split("]")[0].substring(1);
            String d2 = o2.split("]")[0].substring(1);

            try {
                long diff = sdf.parse(d1).getTime() - sdf.parse(d2).getTime();
                if (diff < 0) {
                    return -1;
                } else if (diff > 0) {
                    return 1;
                }
            } catch (ParseException e) {

            }
            return 0;
        });

//        for (String l : lines) {
//            System.out.println(l);
//        }

        Map<String, Guard> guards = new HashMap<>();

        String id = null;
        for (String l : lines) {
            String[] split = l.split("]");

            if (split[1].startsWith(" Guard")) {
                id = split[1].split("#")[1].split(" ")[0];
                if (!guards.containsKey(id)) {
                    Guard g = new Guard();
                    g.id = id;
                    guards.put(id, g);
                }
            } else if (split[1].equals(" falls asleep") && id != null) {
                Guard g = guards.get(id);
                Sleep s = new Sleep();
                s.from = sdf.parse(split[0].substring(1)).getMinutes();
                g.sleeps.add(s);
            } else if (split[1].equals(" wakes up") && id != null) {
                Guard g = guards.get(id);
                Sleep s = g.sleeps.get(g.sleeps.size() - 1);
                s.to = sdf.parse(split[0].substring(1)).getMinutes();
            }
        }

        List<Guard> guardsList = new LinkedList<>(guards.values());

        for (Guard g : guardsList) {
            for (int i = 0; i < 60; i++) {
                g.minutes.put(i, 0);
                for (Sleep s : g.sleeps) {
                    if (i >= s.from && i < s.to) {
                        g.minutes.put(i, g.minutes.get(i) + 1);
                    }
                }
            }

            int maxCount = -1;
            int maxMinute = -1;

            for (Integer k : g.minutes.keySet()) {
                int count = g.minutes.get(k);
                if (count > maxCount) {
                    maxCount = count;
                    maxMinute = k;
                }
            }

            g.maxCount = maxCount;
            g.maxMinute = maxMinute;
        }

        guardsList.sort((o1, o2) -> o2.maxCount - o1.maxCount);

        Guard candidate = guardsList.get(0);

        System.out.println(Integer.valueOf(candidate.id) * candidate.maxMinute);
    }

    private static class Guard {
        String id;
        Map<Integer, Integer> minutes = new HashMap<>();
        int maxCount, maxMinute;
        List<Sleep> sleeps = new LinkedList<>();
    }

    private static class Sleep {
        Integer from, to;
    }
}
