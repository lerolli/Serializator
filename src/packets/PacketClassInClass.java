package packets;

import java.util.Objects;

public class PacketClassInClass {

    public PacketClassInClass packet;

    @Override
    public boolean equals(Object o) {
        var packet = (PacketClassInClass) o;
        if (o == null)
            return false;
        else
            return packet.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(packet);
    }
}
