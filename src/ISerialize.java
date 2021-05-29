public interface ISerialize {
    String type = null;
    public <T> byte[] Serialize(T o);
    public <T extends Object> T Deserialize (byte[] raw);
}
