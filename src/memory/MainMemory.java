package memory;

import extra.Helper;

import java.util.Arrays;

public class MainMemory implements Memory {

    // TODO: Refactor this method. It is hardcoded.
    @Override
    public Block read(String tag) {
        Helper.log(this, "Reading block with tag " + tag);
        String[] content = new String[64];
        Arrays.fill(content, "00000000");
        Helper.log(this, "Reading block with tag " + tag + " finished");
        return new Block(tag, content, false);
    }

    @Override
    public void store(Block block) {
        if (block.isDirty()) {
            Helper.log(this, "Storing block " + block + " on Main Memory");
            block.setDirty(false);
            Helper.log(this, "Block " + block + " stored on Main Memory");
        }
    }
}
