package memory;

import extra.Helper;

public class MemorySystem {

    // Values for debugging purposes
    private static int CACHE_L1_CAPACITY = 4;
    private static int CACHE_L2_CAPACITY = 6;

    private Cache cacheL1;
    private Cache cacheL2;
    private MainMemory mainMemory;

    public MemorySystem() {
        mainMemory = new MainMemory();
        cacheL2 = new Cache("L2", CACHE_L2_CAPACITY, mainMemory, false);
        cacheL1 = new Cache("L1", CACHE_L1_CAPACITY, cacheL2, true);
    }

    public String read(String address) {
        Helper.log(this, "Reading address " + address);
        String tag = getTag(address);
        int offset = getOffset(address);
        Block block = cacheL1.read(tag);
        Helper.log(this, "Reading address " + address + " finished");
        return block.getContentAt(offset);
    }

    public void write(String address, String value) {
        Helper.log(this, "Writing on address " + address + " with the value " + value);
        String tag = getTag(address);
        int offset = getOffset(address);
        Block block = cacheL1.read(tag);
        block.setContentAt(offset, value);
        block.setDirty(true);
        Helper.log(this, "Writing on address "+ address + " finished");
    }

    private String getTag(String address){
        Long i = Long.parseLong(address,16);
        i >>= 6;
        return Long.toHexString(i);
    }

    private int getOffset(String address){
        Long i = Long.parseLong(address,16);
        i &= 0x0000003F;
        return (int) (long) i;
    }

    @Override
    public String toString() {
        return cacheL2 + ", " + cacheL1;
    }
}
