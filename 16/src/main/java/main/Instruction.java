package main;

import java.util.List;

public abstract class Instruction {
    private Integer code;

    public abstract void process(int a, int b, int c, List<Register> registers);

    @Override
    public String toString() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
