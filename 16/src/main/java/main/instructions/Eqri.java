package main.instructions;

import main.Instruction;
import main.Register;

import java.util.List;

public class Eqri extends Instruction {
    @Override
    public void process(int a, int b, int c, List<Register> registers) {
        registers.get(c).setValue(b == registers.get(a).getValue() ? 1 : 0);
    }
}
