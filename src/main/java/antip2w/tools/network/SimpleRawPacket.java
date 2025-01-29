package antip2w.tools.network;

import antip2w.tools.util.MCUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.PacketType;

import java.util.function.Consumer;

public final class SimpleRawPacket implements RawPacket {

    public final PacketType<?> type;
    public final Consumer<ByteBuf> writer;

    private SimpleRawPacket(PacketType<?> type, Consumer<ByteBuf> writer) {
        this.type = type;
        this.writer = writer;
    }

    public static SimpleRawPacket of(PacketType<?> type, Consumer<PacketByteBuf> writer) {
        return new SimpleRawPacket(type, buf -> writer.accept(ByteBufUpgrader.PACKET.apply(buf))); // buf instanceof PacketByteBuf => false
    }

    public static SimpleRawPacket ofPlay(PacketType<?> type, Consumer<RegistryByteBuf> writer) {
        return new SimpleRawPacket(type, buf -> {
            if (MCUtil.getPlayNetHandler() == null) {
                return;
            }

            writer.accept(ByteBufUpgrader.REGISTRY.apply(buf));
        });
    }

    @Override
    public PacketType<?> getPacketType() {
        return type;
    }

    @Override
    public Consumer<ByteBuf> getEncoder() {
        return writer;
    }
}
