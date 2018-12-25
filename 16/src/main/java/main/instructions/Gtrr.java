package main.instructions;

import main.Instruction;
import main.Register;

import java.util.List;

public class Gtrr extends Instruction {
    @Override
    public void process(int a, int b, int c, List<Register> registers) {
        registers.get(c).setValue(registers.get(a).getValue() > registers.get(b).getValue() ? 1 : 0);
    }
}
