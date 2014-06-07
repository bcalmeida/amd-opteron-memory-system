package memory;

import extra.Helper;

public class MemorySystem {

    // TODO: Implement later with BLOCK_OFFSET_SIZE = 6
    private static int INPUT_LENGTH = 8;
    private static int BLOCK_OFFSET_SIZE = 8;
    private static int CACHE_L1_CAPACITY = 4; // Debugging

    private CacheL1 cacheL1;
    private MainMemory mainMemory;

    public MemorySystem() {
        mainMemory = new MainMemory();
        cacheL1 = new CacheL1(mainMemory, CACHE_L1_CAPACITY);
    }

    public String read(String address) {
        if (address.length() != INPUT_LENGTH) {
            Helper.errorLog(this, "Invalid address");
            return null;
        }
        Block block = cacheL1.read(address);
        int offset = getOffset(address);
        return block.getContentAt(offset);
    }

    // TODO: Refactor this method, it is tied with BLOCK_OFFSET_SIZE
    // Tied with
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
