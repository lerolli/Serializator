package packets;


import Point.Point;


public class PacketPoint {
    public Point z;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacketPoint that = (PacketPoint) o;
        return z.equals(that.z);
    }
}
