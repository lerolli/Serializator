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


    public static byte[] convertNameTypeToByte(String type){
        if (codeOfTypeVariable.containsKey(type)){
            switch (codeOfTypeVariable.get(type)){
                case 1:{
                    return  ByteBuffer.allocate(4).putInt(1).array();
                }

                case 2:{
                    return  ByteBuffer.allocate(4).putInt(2).array();
                }

                case 3:{
                    return  ByteBuffer.allocate(4).putInt(3).array();
                }

                case 4:{
                    return  ByteBuffer.allocate(4).putInt(4).array();
                }

                case 5:{
                    return  ByteBuffer.allocate(4).putInt(5).array();
                }

                case 6:{
                    return  ByteBuffer.allocate(4).putInt(6).array();
                }
                case 7:{
                    return ByteBuffer.allocate(4).putInt(7).array();
                }
                case 8:{
                    return ByteBuffer.allocate(4).putInt(8).array();
                }

                case 9:{
                    return ByteBuffer.allocate(4).putInt(9).array();
                }
                default:
                    return null;
            }
        }
        return null;
    }

    public static byte[] convertValueToByte (String name, Object value){

            switch (codeOfTypeVariable.get(name)){
                //int
                case 1:{
                    return  ByteBuffer.allocate(4).putInt((Integer) value).array();
                }

                //byte
                case 2:{
                    return  new byte[] {(byte) value};
                }

                //short
                case 3:{
                    return  ByteBuffer.allocate(2).putShort((Short) value).array();
                }

                //long
                case 4:{
                    return  ByteBuffer.allocate(8).putLong((Long) value).array();
                }

                //double
                case 5:{
                    return  ByteBuffer.allocate(8).putDouble((Double) value).array();
                }

                //char
                case 6:{
                    return  new byte[] {(byte) ((char) value)};
                }

                //bool
                case 7:{
                    int number = ((boolean) value) ? 1 : 0;
                    return ByteBuffer.allocate(4).putInt (number).array();
                }

                //java.lang.String
                case 8:{
                    return value.toString().getBytes();
                }

                //float
                case 9:{
                    return ByteBuffer.allocate(4).putFloat((Float) value).array();
                }

                default:
                    return null;
            }
    }


    public static String getNameTypeFromByte(byte number){
        return codeOfVariableType.get((int) number);
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
