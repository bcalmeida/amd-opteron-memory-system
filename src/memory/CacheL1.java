package memory;

import extra.Helper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class CacheL1 implements Memory {

    private HashMap<String, Block> cachedBlocks;
    private Queue<Block> cachedBlocksQueue;
    private Memory backingMemory;
    private int capacity;

    public CacheL1(Memory backingMemory, int capacity) {
        cachedBlocks = new HashMap<String, Block>();
        cachedBlocksQueue = new LinkedList<Block>();
        this.backingMemory = backingMemory;
        this.capacity = capacity;
    }

    @Override
    public Block read(String address) {
        Helper.log(this, "Reading address " + address);
        String tag = MemorySystem.getTag(address);
        Block block = parallelSearch(tag);
        if (block == null) {
            Helper.log(this, "Cache miss, proceeding to read at next level");
            block = backingMemory.read(address);
            addBlock(block);
        }
        return block;
    }

    // TODO: Implement write operation
    @Override
    public void write(Block block) { }

    private Block parallelSearch(String tag) {
        return cachedBlocks.get(tag);
    }

    private void addBlock(Block block){
        if (cachedBlocks.containsValue(block)) {
            Helper.errorLog(this, "Trying to add already cached block on cache");
        }
        if (cachedBlocks.size() == capacity) {
            removeOldestBlock();
        }
        cachedBlocks.put(block.tag(), block);
        cachedBlocksQueue.add(block);
        Helper.log(this, "Added block to cache");
    }

    private void removeOldestBlock() {
        Block oldestBlock = cachedBlocksQueue.remove();
        cachedBlocks.remove(oldestBlock.tag());
        if (oldestBlock.isDirty()) {
            backingMemory.write(oldestBlock);
        }
        Helper.log(this, "Removed oldest block from cache");
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Cache L1");
        for (Block block : cachedBlocksQueue) {
            sb.append(" : " + block.tag());
        }
        return sb.toString();
    }
}
