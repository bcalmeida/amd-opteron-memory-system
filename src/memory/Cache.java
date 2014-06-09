package memory;

import extra.Helper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Cache implements Memory {

    private String name;
    private int capacity;
    private Memory backingMemory;
    private boolean topCache;
    private HashMap<String, Block> cachedBlocks;
    private Queue<Block> cachedBlocksQueue;

    public Cache(String name, int capacity, Memory backingMemory, boolean topCache) {
        this.name = name;
        this.backingMemory = backingMemory;
        this.capacity = capacity;
        this.topCache = topCache;
        cachedBlocks = new HashMap<String, Block>();
        cachedBlocksQueue = new LinkedList<Block>();
    }

    @Override
    public Block read(String tag) {
        log("Reading block with tag " + tag);
        Block block = parallelSearch(tag);
        if (block == null) {
            log("Cache miss, proceeding to read next level");
            block = backingMemory.read(tag);
            if (topCache) {
                store(block);
            }
        } else if (!topCache) {
            log("Cache hit out of top cache, proceeding to remove block");
            removeBlock(block);
        }
        return block;
    }

    @Override
    public void store(Block block) {
        log("Storing block to cache");
        if (cachedBlocks.containsValue(block)) {
            Helper.errorLog(this, "Trying to store already cached block on cache");
        }
        if (cachedBlocks.size() == capacity) {
            dropOldestBlock();
        }
        cachedBlocks.put(block.tag(), block);
        cachedBlocksQueue.add(block);
    }

    private Block parallelSearch(String tag) {
        return cachedBlocks.get(tag);
    }

    private void dropOldestBlock() {
        log("Dropping oldest block to next level");
        Block oldestBlock = cachedBlocksQueue.remove();
        cachedBlocks.remove(oldestBlock.tag());
        backingMemory.store(oldestBlock);
    }

    private void removeBlock(Block block) {
        log("Removing block " + block);
        cachedBlocksQueue.remove(block);
        cachedBlocks.remove(block.tag());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cache ").append(name);
        for (Block block : cachedBlocksQueue) {
            sb.append(" : ").append(block.toString());
        }
        return sb.toString();
    }

    private void log(String text) {
        Helper.log(this, name + ": " + text);
    }
}
