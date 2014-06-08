package extra;

import memory.MemorySystem;

public class Test {

    public static void main(String[] args) {
        MemorySystem mem = new MemorySystem();

        // Reading
        System.out.println("Testing reading same address");
        mem.read("0041f7a0");
        mem.read("0041f7a0");
        mem.read("0041f7a2");
        mem.read("004758a0");
        System.out.println("Situation: " + mem);
        System.out.println("Should be: " + "Cache L1 : 0041f7 : 004758");
        System.out.println();

        System.out.println("Testing reading multiple address and checking if the oldest block is removed");
        mem.read("0041f7a0");
        mem.read("1041f7a0");
        mem.read("2041f7a0");
        mem.read("3041f7a0");
        System.out.println("Situation: " + mem);
        System.out.println("Should be: " + "Cache L1 : 004758 : 1041f7 : 2041f7 : 3041f7");
        System.out.println();

        // Writing
        System.out.println("Testing write hit");
        mem.write("1041f7a0", "00000000");
        mem.write("2041f7a0", "00000000");
        mem.write("2041f7a0", "00000001");
        System.out.println("Situation: " + mem);
        System.out.println("Should be: " + "Cache L1 : 004758 : 1041f7(*) : 2041f7(*) : 3041f7");
        System.out.println();

        System.out.println("Testing write miss");
        mem.write("AA00f7a0", "00000000");
        mem.write("AA00f7a0", "00000001");
        System.out.println("Situation: " + mem);
        System.out.println("Should be: " + "Cache L1 : 1041f7(*) : 2041f7(*) : 3041f7 : AA00f7(*)");
        System.out.println();

        System.out.println("Testing if dirty blocks are written on main memory");
        Helper.LOGGING = true;
        mem.write("AA10f7a0", "00000000");
        System.out.println();

        mem.read("AA20f7a0");
        System.out.println();
        Helper.LOGGING = false;
        System.out.println("Situation: " + mem);
        System.out.println("Should be: " + "Cache L1 : 3041f7 : AA00f7(*) : AA10f7(*) : AA20f7");
        System.out.println();
    }
}
