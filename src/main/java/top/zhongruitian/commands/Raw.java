package top.zhongruitian.commands;

public class Raw implements Rawable {
    private byte[] raw;

    public Raw(byte[] raw) {
        this.raw = raw;
    }

    @Override
    public byte[] getRaw() {
        return this.raw;
    }
}
