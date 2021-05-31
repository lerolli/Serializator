import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

public class Program {
    public static void main(String[] args) {

        var serialization = new Serialization();
//        checkEqualsSimpleClass(serialization);
//        checkEqualsClassInClass(serialization);
//        checkEqualsPointClass(serialization);
        checkEqualsArrayClass(serialization);
    }

    private static void checkEqualsArrayClass(Serialization serialization) {
        var packetArray = new PacketArray();
        packetArray.intArray = new int[2];
        packetArray.intArray[0] = 1;
        packetArray.intArray[1] = 1;
        var serializeResult = serialization.Serialize(packetArray);
        PacketArray deserializeResult = null;

        try {
            deserializeResult = serialization.Deserialize(serializeResult);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (packetArray.equals(deserializeResult))
            System.out.println("Классы равны");
        else
            System.out.println("Классы не равны");


    }

    private static void checkEqualsPointClass(Serialization serialization) {
        var packet = new PacketPoint();
        packet.z = new Point(1, 2);
        serialization.AddCustom(new PointSerialize());
        PacketClassInClass newPacket = null;
        try {
            newPacket = serialization.Deserialize(serialization.Serialize(packet));
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        if (packet.equals(newPacket))
            System.out.println("Классы равны");
        else
            System.out.println("Классы не равны");
    }

    private static void checkEqualsClassInClass(Serialization serialization) {
        try {
            var packet = new PacketClassInClass();
            packet.packet = new PacketClassInClass();
            PacketClassInClass newPacket = serialization.Deserialize(serialization.Serialize(packet));

            if (packet.equals(newPacket))
                System.out.println("Классы равны");
            else
                System.out.println("Классы не равны");

        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void checkEqualsSimpleClass(Serialization serialization) {
        var packet = new PacketSimple();
        packet.s = "sdfvnjdbnjdnjfdbjibfj";
        packet.i = 11;
        packet.d = 112;
        var serializeResult = serialization.Serialize(packet);
        PacketSimple deserializeResult = null;

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
