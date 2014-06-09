package memory;

import extra.Helper;

// TODO: Implement later with 6 bits of offset, instead of 8
public class MemorySystem {

    // Value for debugging purposes
    private static int CACHE_L1_CAPACITY = 4;

    private CacheL1 cacheL1;
    private MainMemory mainMemory;

    public MemorySystem() {
        mainMemory = new MainMemory();
        cacheL1 = new CacheL1(mainMemory, CACHE_L1_CAPACITY);
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
        return cacheL1.toString();
    }
}
