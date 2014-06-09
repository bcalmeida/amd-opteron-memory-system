package extra;

import memory.MemorySystem;

public class Test {

    public static void main(String[] args) {
        MemorySystem mem = new MemorySystem();

        System.out.println("Filling caches");
        mem.read("01000000");
        mem.read("01000000");
        mem.read("02000000");
        mem.read("03000000");
        mem.read("04000000");
        // testOutput(mem, "Cache L2, Cache L1 : 010000 : 020000 : 030000 : 040000");
        testOutput(mem, "Cache L2, Cache L1 : 40000 : 80000 : c0000 : 100000");
        mem.read("05000000");
        mem.read("06000000");
        mem.read("07000000");
        mem.read("08000000");
        mem.read("09000000");
        // testOutput(mem, "Cache L2 : 010000 : 020000 : 030000 : 040000 : 050000, Cache L1 : 060000 : 070000 : 080000 : 090000");
        testOutput(mem, "Cache L2 : 40000 : 80000 : c0000 : 100000 : 140000, Cache L1 : 180000 : 1c0000 : 200000 : 240000");
        mem.read("10000000");
        mem.read("11000000");
        mem.read("12000000");
        mem.read("13000000");
        // testOutput(mem, "Cache L2 : 040000 : 050000 : 060000 : 070000 : 080000 : 090000, Cache L1 : 100000 : 110000 : 120000 : 130000");
        testOutput(mem, "Cache L2 : 100000 : 140000 : 180000 : 1c0000 : 200000 : 240000, Cache L1 : 400000 : 440000 : 480000 : 4c0000");

        System.out.println("Reading from cache L1");
        mem.read("11000000");
        // testOutput(mem, "Cache L2 : 040000 : 050000 : 060000 : 070000 : 080000 : 090000, Cache L1 : 100000 : 110000 : 120000 : 130000");
        testOutput(mem, "Cache L2 : 100000 : 140000 : 180000 : 1c0000 : 200000 : 240000, Cache L1 : 400000 : 440000 : 480000 : 4c0000");

        System.out.println("Reading from cache L2");
        mem.read("07000000");
        // testOutput(mem, "Cache L2 : 040000 : 050000 : 060000 : 080000 : 090000 : 100000, Cache L1 : 110000 : 120000 : 130000 : 070000");
        testOutput(mem, "Cache L2 : 100000 : 140000 : 180000 : 200000 : 240000 : 400000, Cache L1 : 440000 : 480000 : 4c0000 : 1c0000");
        mem.read("10000000");
        //testOutput(mem, "Cache L2 : 040000 : 050000 : 060000 : 080000 : 090000 : 110000, Cache L1 : 120000 : 130000 : 070000 : 100000");
        testOutput(mem, "Cache L2 : 100000 : 140000 : 180000 : 200000 : 240000 : 440000, Cache L1 : 480000 : 4c0000 : 1c0000 : 400000");

        System.out.println("Reading from main memory");
        mem.read("14000000");
        // testOutput(mem, "Cache L2 : 050000 : 060000 : 080000 : 090000 : 110000 : 120000, Cache L1 : 130000 : 070000 : 100000 : 140000");
        testOutput(mem, "Cache L2 : 140000 : 180000 : 200000 : 240000 : 440000 : 480000, Cache L1 : 4c0000 : 1c0000 : 400000 : 500000");

        System.out.println("Writing on cache L1");
        mem.write("14000000", "00000000");
        mem.write("07000000", "00000000");
        // testOutput(mem, "Cache L2 : 050000 : 060000 : 080000 : 090000 : 110000 : 120000, Cache L1 : 130000 : 070000(*) : 100000 : 140000(*)");
        testOutput(mem, "Cache L2 : 140000 : 180000 : 200000 : 240000 : 440000 : 480000, Cache L1 : 4c0000 : 1c0000(*) : 400000 : 500000(*)");

        System.out.println("Writing on cache L2");
        mem.write("06000000", "00000000");
        // testOutput(mem, "Cache L2 : 050000 : 080000 : 090000 : 110000 : 120000 : 130000, Cache L1 : 070000(*) : 100000 : 140000(*) : 060000(*)");
        testOutput(mem, "Cache L2 : 140000 : 200000 : 240000 : 440000 : 480000 : 4c0000, Cache L1 : 1c0000(*) : 400000 : 500000(*) : 180000(*)");

        System.out.println("Checking if dirty block is stored on memory");
        mem.read("20000000");
        mem.read("21000000");
        mem.read("22000000");
        mem.read("23000000");
        mem.read("24000000");
        mem.read("25000000");
        mem.read("26000000");
        // testOutput(mem, "Cache L2 : 100000 : 140000(*) : 060000(*) : 200000 : 210000 : 220000, Cache L1 : 230000 : 240000 : 250000 : 260000");
        testOutput(mem, "Cache L2 : 400000 : 500000(*) : 180000(*) : 800000 : 840000 : 880000, Cache L1 : 8c0000 : 900000 : 940000 : 980000");
    }

    private static void testOutput(MemorySystem mem, String expected) {
        if (mem.toString().equals(expected)) {
            System.out.println("PASS");
        } else System.out.println("FAILED");
    }
}
