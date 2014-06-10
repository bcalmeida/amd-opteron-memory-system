package memory;

import extra.Helper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Cache implements Memory {

    private String name;
    private int capacity;
    private HashMap<String, Block> cachedBlocks;
    private Queue<Block> cachedBlocksQueue;

    public Cache(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        cachedBlocks = new HashMap<String, Block>();
        cachedBlocksQueue = new LinkedList<Block>();
    }

    @Override
    public Block read(String tag) {
        log("Reading block from cache");
        return cachedBlocks.get(tag);
    }

    @Override
    public Block store(Block block) {
        log("Storing block to cache");
        if (cachedBlocks.containsKey(block.tag())) {
            updateBlock(block);
            return null;
        }
        Block discardedBlock = null;
        if (cachedBlocks.size() == capacity) {
            discardedBlock = removeOldestBlock();
        }
        addBlock(block);
        return discardedBlock;
    }

    public Block removeBlock(Block block) {
        log("Removing block " + block);
        return removeSimilarBlock(block);
    }

    private Block removeSimilarBlock(Block block) {
        Block removedBlock = cachedBlocks.remove(block.tag());
        for (Block b : cachedBlocksQueue) {
            if (b.tag().equals(block.tag())) {
                cachedBlocksQueue.remove(b);
                break;
            }
        }
        return removedBlock;
    }

    private void addBlock(Block block) {
        cachedBlocks.put(block.tag(), block);
        cachedBlocksQueue.add(block);
    }

    private void updateBlock(Block block) {
        removeSimilarBlock(block);
        addBlock(block);
    }

    private Block removeOldestBlock() {
        log("Removing oldest block from cache");
        Block oldestBlock = cachedBlocksQueue.remove();
        cachedBlocks.remove(oldestBlock.tag());
        return oldestBlock;
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
