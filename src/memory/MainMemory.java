package memory;

import extra.Helper;

import java.util.Arrays;

public class MainMemory implements Memory {

    // TODO: Refactor this method. It is hardcoded.
    @Override
    public Block read(String address) {
        Helper.log(this, "Reading address " + address);
        String tag = MemorySystem.getTag(address);
        String[] content = new String[256];
        Arrays.fill(content, "00000000");
        Helper.log(this, "Reading address " + address + " finished");
        return new Block(tag, content, false);
    }

    @Override
    public void store(Block block) {
        Helper.log(this, "Storing block " + block + " on Main Memory");
        block.setDirty(false);
        Helper.log(this, "Block " + block + " stored on Main Memory");
    }
}
