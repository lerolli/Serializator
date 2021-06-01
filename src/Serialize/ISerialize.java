package Serialize;

public interface ISerialize {

    public String setTypeSerialize();
    public <T> byte[] Serialize(Object o);
    public <T extends Object> T Deserialize (byte[] raw);
}
