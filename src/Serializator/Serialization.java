package Serializator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Serialization implements ISerialize {

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
                var fieldTypeNameToByte = Converter.convertNameTypeToByte(fieldTypeName);

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
                if (valueField != null){
                    result.write(Converter.convertValueToByte(fieldTypeName, valueField));
                }

                result.write('|');

            }

            result.write(new byte[] {(byte)')'});

        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result.toByteArray();
    }

    public <T extends Object> T Deserialize (byte[] raw) {

        // Делаем проверку, что наш пакет пришел целым
        if (raw.length == 2 || (raw[0] != 40 && raw[raw.length - 1] != 41))
            return null;

        var classBytes = Arrays.copyOfRange(raw, 1, raw.length - 1);

        var nameArray = new ArrayList<Byte>();
        var index = 1;
        byte tempBytes = classBytes[index];

        // Получаем название класса из массива байт
        while (tempBytes != 124) {
            nameArray.add(tempBytes);
            index++;
            tempBytes = classBytes[index];
        }

        String className = Converter.byteToString(nameArray);
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
            index++;
            var tempArray = new ArrayList<Byte>();
            tempBytes = classBytes[index];
            while (tempBytes != 124) {
                tempArray.add(tempBytes);
                index++;
                tempBytes = classBytes[index];
            }
            String fieldName = Converter.getNameTypeFromByte(tempArray.get(tempArray.size() - 1));
            if (fieldName == null)
                continue;

            if (!fieldName.equals(field.getType().getName()))
                return null;

            // Получаем имя переменной
            index++;
            tempArray = new ArrayList<Byte>();
            tempBytes = classBytes[index];
            while (tempBytes != 124) {
                tempArray.add(tempBytes);
                index++;
                tempBytes = classBytes[index];
            }

            var name = Converter.byteToString(tempArray);

            index++;
            tempArray = new ArrayList<Byte>();
            tempBytes = classBytes[index];
            while (tempBytes != 124) {
                tempArray.add(tempBytes);
                index++;
                tempBytes = classBytes[index];
            }


            var valueOf = Converter.ByteToType(fieldName, tempArray);


            if (valueOf != null) {
                try {
                    try {
                    classObj.getField(name).set(classObj, valueOf);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                // Пытаемся записать в поле значение

        }

        return (T) classObj;
    }

    public void AddCustom(Serialization a){

    }
}
