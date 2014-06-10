package memory;

import extra.Helper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Cache implements Memory {

    private String name;
    private int capacity;
    private HashMap<String, Block> cachedBlocks;
    private Queue<Block>[] cachedBlocksQueues;
    private int amountSets;
    private int associative;

    public Cache(String name, int capacity, int associative) {
        this.name = name;
        this.capacity = capacity;
        this.associative = associative;
        this.amountSets = capacity/associative;
        cachedBlocks = new HashMap<String, Block>();
        cachedBlocksQueues = new Queue[amountSets];
        for (int i = 0; i < amountSets; i++) {
            cachedBlocksQueues[i] = new LinkedList<Block>();
        }
    }

    @Override
    public Block read(String tag) {
        log("Reading block from cache");
        return cachedBlocks.get(tag);
    }

    @Override
    public Block store(Block block) {
        log("Storing block to cache");
        Block discardedBlock = null;
        int setNum = getSetNumber(block);
        if (cachedBlocksQueues[setNum].size() == associative) {
            discardedBlock = removeOldestBlock(setNum);
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
        cachedBlocksQueues[getSetNumber(block)].remove(block);
        return removedBlock;
    }

    private void addBlock(Block block) {
        cachedBlocks.put(block.tag(), block);
        cachedBlocksQueues[getSetNumber(block)].add(block);
    }

    private Block removeOldestBlock(int setNum) {
        log("Removing oldest block from cache");
        Block oldestBlock = cachedBlocksQueues[setNum].remove();
        cachedBlocks.remove(oldestBlock.tag());
        return oldestBlock;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cache ").append(name);
        for (Queue<Block> cachedBlocksQueue : cachedBlocksQueues){
            for (Block block : cachedBlocksQueue) {
                sb.append(" : ").append(block.toString());
            }
        }
        return sb.toString();
    }

    private int getSetNumber(Block block){
        Long l = Long.parseLong(block.tag(),16);
        return (int) (long) l % amountSets;
    }

    private void log(String text) {
        Helper.log(this, name + ": " + text);
    }
}
