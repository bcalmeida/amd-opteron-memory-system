package memory;

import extra.Helper;

import java.util.Arrays;

public class MainMemory implements Memory {

    @Override
    public Block read(String tag) {
        Helper.log(this, "Reading block from main memory");
        String[] content = new String[64];
        Arrays.fill(content, "00000000");
        return new Block(tag, content, false);
    }

    @Override
    public Block store(Block block) {
        Helper.log(this, "Storing block " + block + " on Main Memory");
        block.setDirty(false);
        return null;
    }
}
