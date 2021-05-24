package Serializator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Serialization {
    private final HashMap<String, ISerialize> customSerialize = new HashMap<>();
    private int indexDeserialize = 0;
    public <T> byte[] Serialize(T o){

        if (o == null)
            return new byte[]{(byte) '(', (byte) ')'};

        var result = new ByteArrayOutputStream();

        try {
            // ( - начало массива байт, ) - конец массива байт
            result.write((byte)'(');

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
                    customSerialize.get(fieldTypeName).Serialize(field);
                }
                else {

                    // Если null, то это какой-то пользовательский класс
                    if (fieldTypeNameToByte != null)
                        result.write(fieldTypeNameToByte);
                    else
                        result.write(Serialize(field.get(o)));
                    result.write('|');

                    // Записываем длину имени переменной и имя переменной
                    var fieldNameBytes = field.getName().getBytes(StandardCharsets.UTF_8);
                    result.write(fieldNameBytes);
                    result.write('|');

                    var valueField = field.get(o);
                    if (valueField != null) {
                        result.write(Converter.convertValueToByte(fieldTypeName, valueField));
                    }
                    result.write('|');
                }
            }

            result.write(new byte[] {(byte)')'});

        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result.toByteArray();
    }

    public <T> T Deserialize (byte[] raw) {

        // Делаем проверку, что наш пакет пришел целым
        if (raw.length == 2 || (raw[0] != 40 && raw[raw.length - 1] != 41))
            return null;

        var classBytes = Arrays.copyOfRange(raw, 1, raw.length - 1);

        // Получаем название класса из массива байт
        var className = Converter.byteToString(getFromByteArray(classBytes));

        // Получаем сам класс
        Class classObj = null;
        try {
            classObj = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (classObj == null)
            return null;

        // Получаем известные поля класса и проходимся по ним
        for (var field : classObj.getDeclaredFields()) {

            // Получаем имя типа поля ("int", "byte" и так далее)
            String fieldName = Converter.getNameTypeFromByte(getFromByteArray(classBytes));
            if (fieldName == null)
                continue;

            if (!fieldName.equals(field.getType().getName()))
                return null;

            // Получаем имя и значение переменной
            var name = Converter.byteToString(getFromByteArray(classBytes));
            var valueOf = Converter.ByteToType(fieldName, getFromByteArray(classBytes));

            // Пытаемся записать в поле значение
            if (valueOf != null) {
                try {
                    field.set(classObj, valueOf);
                } catch (IllegalAccessException e) {
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
