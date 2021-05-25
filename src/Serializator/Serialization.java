package Serializator;

import Serializator.packets.Packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Serialization {
    private final HashMap<String, ISerialize> customSerialize = new HashMap<>();
    private int indexDeserialize = 0;

    public <T> byte[] Serialize(T o){

        var result = new ByteArrayOutputStream();
        try {
            if (o == null)
                return new byte[0];

            // Записываем длину имени класса и имя класса
            var oClass = o.getClass();
            result.write('|');
            result.write(oClass.getName().getBytes());
            result.write('|');

            // Проходимся по полям класса
            for (var field : oClass.getDeclaredFields()){
                field.setAccessible(true);

                // Записываем длину имени типа переменной и тип переменной
                var fieldTypeName = field.getType().getName();
                var fieldTypeNameToByte = Converter.convertNameTypeVariableToByte(fieldTypeName);

                if (customSerialize.containsKey(fieldTypeNameToByte)){
                    result.write(customSerialize.get(fieldTypeName).Serialize(field));
                }
                else {

                    // Если null, то это какой-то пользовательский класс
                    if (fieldTypeNameToByte != null) {
                        result.write(fieldTypeNameToByte);
                        var fieldNameBytes = field.getName().getBytes(StandardCharsets.UTF_8);
                        result.write(fieldNameBytes);
                        result.write('|');

                        var valueField = field.get(o);
                        if (valueField != null) {
                            result.write(Converter.convertValueToByte(fieldTypeName, valueField));
                        }
                        result.write('|');
                    }
                    else
                        result.write(Serialize(field.get(o)));
                    result.write('|');

                    // Записываем длину имени переменной и имя переменной

                }
            }

        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result.toByteArray();
    }

    public <T extends Object> T Deserialize (byte[] classBytes) {

        // Получаем название класса из массива байт
        var className = Converter.byteToString(getFromByteArray(classBytes));

        // Получаем сам класс
        Class<Object> classObj = null;
        try {
            classObj = (Class<Object>) Class.forName(className);
        } catch (ClassNotFoundException ignored) { }


        if (classObj == null)
            return null;

        // Получаем известные поля класса и проходимся по ним
        for (var field : classObj.getDeclaredFields()) {
            String name = "";
            Object valueOf = null;

            // Получаем имя типа поля ("int", "byte" и так далее)
            String fieldName = Converter.getNameTypeFromByte(getFromByteArray(classBytes));
            if (customSerialize.containsKey(fieldName)){
                var customSerializeArray = Arrays.copyOfRange(classBytes, indexDeserialize, classBytes.length - 1);
                Field f = customSerialize.get(fieldName).Deserialize(customSerializeArray);
                name = f.getName();
                try {
                    valueOf = f.get(f);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            else {
                if (fieldName == null) {
                    Field tempField = Deserialize(classBytes);
                    if (tempField == null)
                        continue;
                    field = tempField;
                }

                if (!fieldName.equals(field.getType().getName()))
                    return null;

                // Получаем имя и значение переменной
                name = Converter.byteToString(getFromByteArray(classBytes));
                valueOf = Converter.ByteToType(fieldName, getFromByteArray(classBytes));
            }
            // Пытаемся записать в поле значение
            if (valueOf != null) {
                try {
                    var f = classObj.newInstance();
                    field.set(f, valueOf);
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        return (T) classObj;
    }

    private ArrayList<Byte> getFromByteArray(byte[] classBytes){
        indexDeserialize++;
        var tempArray = new ArrayList<Byte>();
        var tempBytes = classBytes[indexDeserialize];
        while (tempBytes != 124) {
            tempArray.add(tempBytes);
            indexDeserialize++;
            tempBytes = classBytes[indexDeserialize];
        }
        return tempArray;
    }

    public void AddCustom(ISerialize a){
        customSerialize.put(a.type, a);
    }
}
