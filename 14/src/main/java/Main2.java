import java.util.ArrayList;
import java.util.List;

public class Main2 {
    public static void main(String[] args) {
        List<Integer> recipes = new ArrayList<>();

        recipes.add(3);
        recipes.add(7);

        int elf1 = 0;
        int elf2 = 1;

        int[] input = {0,4,7,8,0,1};
//        int[] input = {5,1,5,8,9};
//        int[] input = {0,1,2,4,5};
//        int[] input = {9,2,5,1,0}; nefunguje
//        int[] input = {5,9,4,1,4}; nefunguje

        while(true) {
            int newRecipe = recipes.get(elf1) + recipes.get(elf2);
            if (newRecipe > 9) {
                recipes.add(1);
            }
            recipes.add(newRecipe % 10);

            elf1 += 1 + recipes.get(elf1);
            elf2 += 1 + recipes.get(elf2);

            elf1 %= recipes.size();
            elf2 %= recipes.size();

            if (recipes.size() > 6) {
                if (newRecipe > 9) {
                    boolean match = true;
                    for (int i = 0; i < 6; i++) {
                        if (recipes.get(recipes.size() - 7 + i) != input[i]) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        System.out.print(recipes.size() - 7);
                        break;
                    }
                } else {
                    boolean match = true;
                    for (int i = 0; i < 6; i++) {
                        if (recipes.get(recipes.size() - 6 + i) != input[i]) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        System.out.print(recipes.size() - 6);
                        break;
                    }
                }
            }

        }
    }
}
