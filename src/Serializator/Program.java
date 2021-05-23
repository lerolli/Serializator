package Serializator;

import java.lang.reflect.InvocationTargetException;

public class Program {
    public static void main(String[] args) {
        var serialization = new Serialization();
        var packet = new Packet();
        packet.i = 1;
        var serializeResult = serialization.Serialize(packet);
        var deserializeResult = serialization.Deserialize(serializeResult);
        System.out.println(packet.equals(deserializeResult));
    }


}
