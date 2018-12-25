package main;

import org.reflections.Reflections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main2 {

    final static List<Instruction> instructions = new LinkedList<>();
    final static List<Register> registers = new ArrayList<>();

    static {
        Reflections reflections = new Reflections("main.instructions");

        Set<Class<? extends Instruction>> allClasses = reflections.getSubTypesOf(Instruction.class);
        allClasses.stream()
                .map((Function<Class<? extends Instruction>, Instruction>) aClass -> {
                    try {
                        return aClass.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).
                filter(Objects::nonNull)
                .forEach(instructions::add);

        registers.add(new Register());
        registers.add(new Register());
        registers.add(new Register());
        registers.add(new Register());
    }

    static List<Integer> parse(String in) {
        String[] numbers = in.split("\\[")[1].split("]")[0].split(",");
        return Arrays.stream(numbers)
                .map(String::trim)
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {


        List<String> lines = new LinkedList<>();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main2.class.getResourceAsStream("/input.txt")))) {
            while(reader.ready()) {
                String line = reader.readLine();
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Sample> samples = new LinkedList<>();

        Sample currentSample = new Sample();
        for (String l : lines) {
            if (l.startsWith("Before: ")) {
                currentSample = new Sample();
                currentSample.before = parse(l);
            } else if (l.startsWith("After:  ")) {
                currentSample.after = parse(l);
                samples.add(currentSample);
            } else if (l.length() > 0) {
                currentSample.input = Arrays.stream(l.split(" "))
                        .map(String::trim)
                        .map(Integer::valueOf)
                        .collect(Collectors.toList());
            }
        }

        List<Instruction> resolved = new LinkedList<>();

        Map<Sample, List<Instruction>> test = new HashMap<>();
        for (Sample s : samples) {
            if (!test.containsKey(s)) {
                test.put(s, new LinkedList<>());
            }

            for (Instruction i : instructions) {
                if (s.process(i)) {
                    test.get(s).add(i);
                }
            }
        }

        while (resolved.size() != instructions.size()) {
            for (Sample s : test.keySet()) {
                List<Instruction> unresolved = test.get(s).stream()
                        .filter(instruction -> !resolved.contains(instruction))
                        .collect(Collectors.toList());

                if (unresolved.size() == 1) {
                    Instruction i = unresolved.get(0);
                    int opcode = s.input.get(0);
                    i.setCode(opcode);
                    resolved.add(i);

                    System.out.println(opcode + ": " + i);
                }
            }
        }

        Instruction[] instructionsResolved = resolved.toArray(new Instruction[0]);
        Arrays.sort(instructionsResolved, Comparator.comparingInt(Instruction::getCode));

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Main2.class.getResourceAsStream("/program.txt")))) {
            while(reader.ready()) {
                String line = reader.readLine();

                List<Integer> input = Arrays.stream(line.split(" "))
                        .map(String::trim)
                        .map(Integer::valueOf)
                        .collect(Collectors.toList());

                instructionsResolved[input.get(0)].process(input.get(1), input.get(2), input.get(3), registers);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(registers.get(0).getValue());

    }

    static class Sample {
        List<Integer> before = new ArrayList<>();
        List<Integer> after = new ArrayList<>();
        List<Integer> input = new ArrayList<>();
        
        boolean process(Instruction instruction) {
            List<Register> myRegisters = new ArrayList<>();
            for (Integer i : before) {
                myRegisters.add(new Register(i));
            }

            instruction.process(input.get(1), input.get(2), input.get(3), myRegisters);

            for (int i = 0; i < myRegisters.size(); i++) {
                if (myRegisters.get(i).getValue() != after.get(i)) {
                    return false;
                }
            }

            return true;
        }
    }
    
    static List<Register> copyRegisters() {
        List<Register> res = new ArrayList<>();
        for (Register r : registers) {
            res.add(new Register(r.getValue()));
        }
        return res;
    }
}
