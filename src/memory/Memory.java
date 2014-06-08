package memory;

public interface Memory {

    public Block read(String tag);
    public void store(Block block);

}
