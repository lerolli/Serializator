public interface ISerialize {

    public String setTypeSerialize();
    public <T> byte[] Serialize(T o);
    public <T extends Object> T Deserialize (byte[] raw);
}
