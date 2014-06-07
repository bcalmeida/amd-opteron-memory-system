package memory;

public class Block {
    private String tag;
    private String[] content;
    private boolean dirty;

    public Block(String tag, String[] content, boolean dirty) {
        this.tag = tag;
        this.content = content.clone();
        this.dirty = dirty;
    }

    public String tag() {
        return tag;
    }

    public boolean isDirty() {
        return dirty;
    }

    public String getContentAt(int offset) {
        return content[offset];
    }
}
