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
    public Block read(String tag) {
        Helper.log(this, "Reading block with tag " + tag);
        Block block = parallelSearch(tag);
        if (block == null) {
            Helper.log(this, "Cache miss, proceeding to read at next level");
            block = backingMemory.read(tag);
            store(block);
        }
        return block;
    }

    @Override
    public void store(Block block) {
        Helper.log(this, "Storing block to cache");
        if (cachedBlocks.containsValue(block)) {
            Helper.errorLog(this, "Trying to store already cached block on cache");
        }
        if (cachedBlocks.size() == capacity) {
            removeOldestBlock();
        }
        cachedBlocks.put(block.tag(), block);
        cachedBlocksQueue.add(block);
    }

    private Block parallelSearch(String tag) {
        return cachedBlocks.get(tag);
    }

    private void removeOldestBlock() {
        Helper.log(this, "Removing oldest block from cache");
        Block oldestBlock = cachedBlocksQueue.remove();
        cachedBlocks.remove(oldestBlock.tag());
        if (oldestBlock.isDirty()) {
            backingMemory.store(oldestBlock);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cache L1");
        for (Block block : cachedBlocksQueue) {
            sb.append(" : ").append(block.toString());
        }
        return sb.toString();
    }
}
