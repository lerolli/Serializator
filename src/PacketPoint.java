
public class PacketPoint {
    Point z;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacketPoint that = (PacketPoint) o;
        return z.equals(that.z);
    }
}
