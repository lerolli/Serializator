package packets;

import java.util.Arrays;

public class PacketArray {
    public int[] intArray;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacketArray that = (PacketArray) o;
        return Arrays.equals(intArray, that.intArray);
    }
}
