import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Serialization {
    private final HashMap<String, ISerialize> customSerialize = new HashMap<>();
    private int indexDeserialize = -1;
    public <T> byte[] Serialize(T o){

        if (o == null)
            return new byte[0];

        var result = new ByteArrayOutputStream();

        try {

            // Записываем длину имени класса и имя класса
            var oClass = o.getClass();
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
                    if (fieldTypeNameToByte == null)
                        result.write(Serialize(field.get(o)));
                    else {
                        result.write(fieldTypeNameToByte);
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
            }

        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
        indexDeserialize = -1;
        return result.toByteArray();
    }

    public <T> T Deserialize (byte[] classBytes) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        // Делаем проверку, что наш пакет пришел целым
        if (classBytes.length == 1)
            return null;

        // Получаем название класса из массива байт
        var className = Converter.byteToString(getFromByteArray(classBytes));

        if (className.equals(""))
            return null;

        // Получаем сам класс
        Class classObj = Class.forName(className);
        Object object = classObj.getConstructor().newInstance();



        // Получаем известные поля класса и проходимся по ним
        for (var field : classObj.getDeclaredFields()) {
            String name = "";
            Object valueOf = null;
            Object f = null;
            // Получаем имя типа поля ("int", "byte" и так далее)
            var tempIndex = indexDeserialize;
            String fieldName = Converter.getNameTypeFromByte(getFromByteArray(classBytes));
            if (customSerialize.containsKey(fieldName)){
                f = customSerialize.get(fieldName).Deserialize(classBytes);

            }
            else {
                if (fieldName == null) {
                    indexDeserialize = tempIndex;
                    f = Deserialize(classBytes);
                }

                if (f != null) {
                    valueOf = f;
                } else {
                    // Получаем имя и значение переменной
                    name = Converter.byteToString(getFromByteArray(classBytes));
                    valueOf = Converter.ByteToType(fieldName, getFromByteArray(classBytes));
                }
            }
            // Пытаемся записать в поле значение
            if (valueOf != null && valueOf != "") {
                try {
                    field.setAccessible(true);
                    field.set(object, valueOf);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return (T) object;
    }

    private ArrayList<Byte> getFromByteArray(byte[] classBytes){
        indexDeserialize++;
        var tempArray = new ArrayList<Byte>();

        if (indexDeserialize >= classBytes.length)
            return tempArray;
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
