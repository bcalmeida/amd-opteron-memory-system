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
        cacheL2 = new Cache("L2", CACHE_L2_CAPACITY);
        cacheL1 = new Cache("L1", CACHE_L1_CAPACITY);
    }

    public String read(String address) {
        Helper.log(this, "Reading address " + address);
        String tag = getTag(address);
        int offset = getOffset(address);

        Block block = getBlock(tag);
        return block.getContentAt(offset);
    }

    public void write(String address, String value) {
        Helper.log(this, "Writing on address " + address + " with the value " + value);
        String tag = getTag(address);
        int offset = getOffset(address);

        Block block = getBlock(tag);
        block.setContentAt(offset, value);
        block.setDirty(true);
    }

    // TODO: Refactor
    private Block getBlock(String tag) {
        Block block = null;
        if ((block = cacheL1.read(tag)) != null) {
            Helper.log(this, "Cache L1 hit");
            return block;
        }
        Helper.log(this, "Cache L1 miss");
        if ((block = cacheL2.read(tag)) != null) {
            Helper.log(this, "Cache L2 hit");
            cacheL2.removeBlock(block);
            storeAtFirstLevel(block);
            return block;
        }
        Helper.log(this, "Cache L2 miss");
        if ((block = mainMemory.read(tag)) != null) {
            Helper.log(this, "Main memory hit");
            storeAtFirstLevel(block);
            return block;
        }
        Helper.log(this, "Main memory miss!!");
        return null;
    }

    // TODO: Refactor
    private void storeAtFirstLevel(Block block) {
        Block droppingBlock;
        droppingBlock = cacheL1.store(block);
        if (droppingBlock != null) {
            droppingBlock = cacheL2.store(droppingBlock);
        }
        if (droppingBlock != null) {
            droppingBlock = mainMemory.store(droppingBlock);
        }
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
