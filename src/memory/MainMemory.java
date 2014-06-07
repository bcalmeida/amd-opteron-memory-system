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
        return new Block(tag, content, false);
    }

    // TODO: Implement write operation
    @Override
    public void write(Block block) { }
}
