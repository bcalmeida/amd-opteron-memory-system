package memory;

import extra.Helper;

public class MemorySystem {

    // Values for debugging purposes
//    private static int CACHE_L1_CAPACITY = 1024;
//    private static int CACHE_L2_CAPACITY = 16384;
//    private static int CACHE_L1_ASSOCIATIVE = 2;
//    private static int CACHE_L2_ASSOCIATIVE = 16;
    private int timeCacheL1;
    private int timeCacheL2;
    private int timeMainMemory;
    private int totalTime;
    private int L1Hit;
    private int L2Hit;
    private int MainMemHit;

    private Cache cacheL1;
    private Cache cacheL2;
    private MainMemory mainMemory;

    public MemorySystem(int CACHE_L1_CAPACITY, int CACHE_L1_ASSOCIATIVE, int timeCacheL1, int CACHE_L2_CAPACITY, int CACHE_L2_ASSOCIATIVE, int timeCacheL2, int timeMainMemory) {
        mainMemory = new MainMemory();
        cacheL2 = new Cache("L2", CACHE_L2_CAPACITY, CACHE_L2_ASSOCIATIVE);
        cacheL1 = new Cache("L1", CACHE_L1_CAPACITY, CACHE_L1_ASSOCIATIVE);
        this.timeCacheL1 = timeCacheL1;
        this.timeCacheL2 = timeCacheL2;
        this.timeMainMemory = timeMainMemory;
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
        totalTime += timeCacheL1;
        if ((block = cacheL1.read(tag)) != null) {
            L1Hit++;
//            cacheL1.removeBlock(block);
//            cacheL1.store(block);
            Helper.log(this, "Cache L1 hit");
            return block;
        }
        Helper.log(this, "Cache L1 miss");
        totalTime += timeCacheL2 - timeCacheL1;
        if ((block = cacheL2.read(tag)) != null) {
            L2Hit++;
            Helper.log(this, "Cache L2 hit");
            cacheL2.removeBlock(block);
            storeAtFirstLevel(block);
            return block;
        }
        Helper.log(this, "Cache L2 miss");
        totalTime += timeMainMemory;
        if ((block = mainMemory.read(tag)) != null) {
            MainMemHit++;
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
        totalTime += timeCacheL1;
        if (droppingBlock != null) {
            droppingBlock = cacheL2.store(droppingBlock);
            totalTime += timeCacheL2;
        }
        if (droppingBlock != null) {
            if(droppingBlock.isDirty()) {
                totalTime += timeMainMemory;
                droppingBlock = mainMemory.store(droppingBlock);
            }
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

    public int getL1Hit(){
        return L1Hit;
    }

    public int getL2Hit(){
        return L2Hit;
    }

    public int getMainMemHit(){
        return MainMemHit;
    }

    public int getTotalTime(){
        return totalTime;
    }

    @Override
    public String toString() {
        return cacheL2 + ", " + cacheL1;
    }
}
