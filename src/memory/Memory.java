package memory;

public interface Memory {

    public Block read(String address);
    public void write(Block block);

}
