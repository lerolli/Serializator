import packets.ClassInClassPacket;
import packets.SimplePacket;

import java.lang.reflect.InvocationTargetException;

public class Program {
    public static void main(String[] args) {
        var serialization = new Serialization();
        checkEqualsSimpleClass(serialization);
        checkEqualsArrayClass(serialization);
    }

    private static void checkEqualsArrayClass(Serialization serialization) {
        try {
            var packet = new ClassInClassPacket();
            packet.packet = new ClassInClassPacket();
            ClassInClassPacket newPacket = serialization.Deserialize(serialization.Serialize(packet));

            if (packet.equals(newPacket))
                System.out.println("Классы равны");
            else
                System.out.println("Классы не равны");

        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void checkEqualsSimpleClass(Serialization serialization) {
        var packet = new SimplePacket();
        packet.s = "sdfvnjdbnjdnjfdbjibfj";
        packet.i = 11;
        packet.d = 112;
        var serializeResult = serialization.Serialize(packet);
        SimplePacket deserializeResult = null;

        try {
            deserializeResult = serialization.Deserialize(serializeResult);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (packet.equals(deserializeResult))
            System.out.println("Классы равны");
        else
            System.out.println("Классы не равны");
    }
}
