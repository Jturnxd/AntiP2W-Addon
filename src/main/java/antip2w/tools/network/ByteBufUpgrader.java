package antip2w.tools.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.DynamicRegistryManager;

import java.util.function.Function;

public class ByteBufUpgrader {

    public static DynamicRegistryManager lastRegistryManager = null;

    public static final Function<ByteBuf, PacketByteBuf> PACKET = PacketByteBuf::new;

    public static final Function<ByteBuf, RegistryByteBuf> REGISTRY = buf -> new RegistryByteBuf(buf, lastRegistryManager);

}
