package Serializator;

import Serializator.packets.Packet;

public class Program {
    public static void main(String[] args) {
        var serialization = new Serialization();
        var packet = new Packet();
        packet.packet = new Packet();
        var serializeResult = serialization.Serialize(packet);
        Packet deserializeResult = serialization.<Packet>Deserialize(serializeResult);
        System.out.println(serialization.equals(deserializeResult));
    }
}
