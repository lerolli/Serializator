package Serializator;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Converter {

    private static final HashMap<String, Integer> codeOfTypeVariable = new HashMap<>(){{
        put("int", 1);
        put("byte", 2);
        put("short", 3);
        put("long", 4);
        put("double", 5);
        put("char", 6);
        put("bool", 7);
        put("java.lang.String", 8);
        put("float", 9);
    }};

    private static final HashMap<Integer, String> codeOfVariableType = new HashMap<>(){{
        put(1, "int");
        put(2, "byte");
        put(3, "short");
        put(4, "long");
        put(5, "double");
        put(6, "char");
        put(7, "bool");
        put(8, "java.lang.String");
        put(9, "float");
    }};

    public static byte[] convertNameTypeVariableToByte(String type) {
        if (codeOfTypeVariable.containsKey(type)) {
            return switch (codeOfTypeVariable.get(type)) {
                case 1 -> ByteBuffer.allocate(4).putInt(1).array();
                case 2 -> ByteBuffer.allocate(4).putInt(2).array();
                case 3 -> ByteBuffer.allocate(4).putInt(3).array();
                case 4 -> ByteBuffer.allocate(4).putInt(4).array();
                case 5 -> ByteBuffer.allocate(4).putInt(5).array();
                case 6 -> ByteBuffer.allocate(4).putInt(6).array();
                case 7 -> ByteBuffer.allocate(4).putInt(7).array();
                case 8 -> ByteBuffer.allocate(4).putInt(8).array();
                case 9 -> ByteBuffer.allocate(4).putInt(9).array();
                default -> null;
            };
        }
        return null;
    }

    public static byte[] convertValueToByte (String name, Object value){

        return switch (codeOfTypeVariable.get(name)){
            case 1 -> ByteBuffer.allocate(4).putInt((Integer) value).array();
            case 2 -> new byte[] {(byte) value};
            case 3 -> ByteBuffer.allocate(2).putShort((Short) value).array();
            case 4 -> ByteBuffer.allocate(8).putLong((Long) value).array();
            case 5 -> ByteBuffer.allocate(8).putDouble((Double) value).array();
            case 6 -> new byte[] {(byte) ((char) value)};
            case 7 -> ByteBuffer.allocate(4).putInt (((boolean) value) ? 1 : 0).array();
            case 8 -> value.toString().getBytes();
            case 9 -> ByteBuffer.allocate(4).putFloat((Float) value).array();
            default -> null;
        };
    }

    public static String getNameTypeFromByte(ArrayList<Byte> number){
        if (number.size() == 0)
            return null;
        else
            return codeOfVariableType.get((int) number.get(number.size() - 1));
    }

    public static String byteToString (ArrayList<Byte> arrayList){
        String str;
        var bytes = new byte[arrayList.size()];
        for (int i = 0; i < bytes.length; i++){
            bytes[i] = arrayList.get(i);
        }

        str = new String(bytes, StandardCharsets.UTF_8);
        return str;
    }

    public static Object ByteToType(String fieldName, ArrayList<Byte> tempBytes) {
        if (tempBytes.size() == 0)
            return "";
        byte[] bytes = new byte[tempBytes.size()];
        for (var i = 0; i < tempBytes.size(); i++){
            bytes[i] = tempBytes.get(i);
        }

        return switch (fieldName) {
            case "int" -> ByteBuffer.wrap(bytes).getInt();
            case "byte" -> bytes[0];
            case "short" -> ByteBuffer.wrap(bytes).getShort();
            case "long" -> ByteBuffer.wrap(bytes).getLong();
            case "double" -> ByteBuffer.wrap(bytes).getDouble();
            case "char" -> (char) bytes[0];
            case "bool" -> ByteBuffer.wrap(bytes).getInt() == 1;
            case "java.lang.String" -> byteToString(tempBytes);
            case "float" -> ByteBuffer.wrap(bytes).getFloat();
            default -> null;
        };
    }
}
