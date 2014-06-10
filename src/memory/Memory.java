package memory;

public interface Memory {

    public Block read(String tag);
    public Block store(Block block);

}
