package packets;

import java.util.Objects;

public class PacketSimple {
    public String s;
    public int i;
    public double d;

    public PacketSimple(){
    }


    @Override
    public boolean equals(Object o) {
        PacketSimple packet = (PacketSimple) o;
        return s.equals(packet.s) && i == packet.i && d == packet.d;
    }

    @Override
    public int hashCode() {
        return Objects.hash(s, i, d);
    }
}