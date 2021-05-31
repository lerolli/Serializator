import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class PointSerialize implements ISerialize {


    @Override
    public String setTypeSerialize() {
        return "Point";
    }

    @Override
    public <T> byte[] Serialize(T o){

        if (o == null)
            return new byte[0];
        var result = new ByteArrayOutputStream();

        try {
            var namePoint = ((Field) o).getType().getName();
            Class<?> point = null;

            point = Class.forName(namePoint);
            var a = point.getDeclaredField("x");
            var v= a.get(o);
            // Записываем длину имени класса и имя класса
            result.write("Point".getBytes());
            result.write('|');
            result.write(Objects.requireNonNull(Converter.convertNameTypeVariableToByte("int")));
            result.write('|');
            result.write("x".getBytes(StandardCharsets.UTF_8));
            result.write('|');
            var xValue = point.getDeclaredField("x");
            if (xValue != null) {
                result.write(Converter.convertValueToByte("int", xValue));
            }

            result.write('|');
            result.write(Objects.requireNonNull(Converter.convertNameTypeVariableToByte("int")));
            result.write('|');
            result.write("x".getBytes(StandardCharsets.UTF_8));
            result.write('|');
            var yValue = point.getDeclaredField("y");
            if (yValue != null) {
                result.write(Converter.convertValueToByte("int", yValue));
            }

            result.write('|');

        }
        catch (IOException | ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result.toByteArray();
    }


    @Override
    public <T> T Deserialize(byte[] classBytes) {
        if (classBytes.length == 1)
            return null;

        int x = 0;
        int y = 0;
        var index = 0;



        var object = new Point(x,y);
        return (T) object;
    }
}
