package extra;

import memory.MemorySystem;

public class Test {

    // Test memory with 1 cache level reading operation
    public static void main(String[] args) {
        MemorySystem mem = new MemorySystem();
        mem.read("0041f7a0");
        mem.read("0041f7a0");
        mem.read("0041f7a2");
        mem.read("004758a0");
        mem.read("0041f7a0");
        mem.read("1041f7a0");
        mem.read("2041f7a0");
        mem.read("3041f7a0");
        System.out.println(mem);
    }
}
