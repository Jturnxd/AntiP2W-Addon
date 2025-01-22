package antip2w.tools.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;

import java.util.function.Consumer;

@SuppressWarnings("rawtypes")
public interface RawPacket extends Packet {

    PacketType<?> getPacketType();
    
    Consumer<ByteBuf> getEncoder();

    @Override
    default void apply(PacketListener listener) {
        throw new AssertionError();
    }

}
