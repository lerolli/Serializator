import Point.*;
import Serialize.*;
import packets.*;

import java.lang.reflect.InvocationTargetException;

public class Program {
    public static void main(String[] args) {

        var serialization = new Serialization();
        checkEqualsSimpleClass(serialization);
        checkEqualsClassInClass(serialization);
        checkEqualsPointClass(serialization);
        checkEqualsArrayInClass(serialization);
    }

    private static void checkEqualsArrayInClass(Serialization serialization) {
        var packet = new PacketArray();
        packet.intArray = new int[2];
        packet.intArray[0]= 1;
        packet.intArray[1]= 2;
        Point newPacket = null;
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
        packet.s = "Str";
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
