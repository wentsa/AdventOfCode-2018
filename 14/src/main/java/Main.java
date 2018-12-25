import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> recipes = new ArrayList<>();

        recipes.add(3);
        recipes.add(7);

        int elf1 = 0;
        int elf2 = 1;

        int input = 47801;

        while(recipes.size() < input + 10) {
            int newRecipe = recipes.get(elf1) + recipes.get(elf2);
            if (newRecipe > 9) {
                recipes.add(1);
            }
            recipes.add(newRecipe % 10);

            elf1 += 1 + recipes.get(elf1);
            elf2 += 1 + recipes.get(elf2);

            elf1 %= recipes.size();
            elf2 %= recipes.size();
        }

        for (int i = 0; i < 10; i++) {
            System.out.print(recipes.get(input + i));
        }
        System.out.println();
    }
}
