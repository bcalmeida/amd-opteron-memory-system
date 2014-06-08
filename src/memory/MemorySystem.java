package memory;

import extra.Helper;

public class MemorySystem {

    // TODO: Implement later with BLOCK_OFFSET_SIZE = 6
    private static int BLOCK_OFFSET_SIZE = 8;
    private static int CACHE_L1_CAPACITY = 4; // Debugging

    private CacheL1 cacheL1;
    private MainMemory mainMemory;

    public MemorySystem() {
        mainMemory = new MainMemory();
        cacheL1 = new CacheL1(mainMemory, CACHE_L1_CAPACITY);
    }

    public String read(String address) {
        Helper.log(this, "Reading address " + address);
        Block block = cacheL1.read(address);
        int offset = getOffset(address);
        Helper.log(this, "Reading address " + address + " finished");
        return block.getContentAt(offset);
    }

    public void write(String address, String value) {
        Helper.log(this, "Writing on address " + address + " with the value " + value);
        Block block = cacheL1.read(address);
        int offset = MemorySystem.getOffset(address);
        block.setContentAt(offset, value);
        block.setDirty(true);
        Helper.log(this, "Writing on address "+ address + " finished");
    }

    // TODO: Refactor this method, it is tied with BLOCK_OFFSET_SIZE
    public static String getTag(String address) {
        // Excludes last two digits
        return address.substring(0,6);
    }

    // TODO: Refactor this method, it is tied with BLOCK_OFFSET_SIZE
    public static int getOffset(String address) {
        // Calculates last two digits
        return Integer.parseInt(address.substring(6), 16);
    }

    @Override
    public String toString() {
        return cacheL1.toString();
    }
}
