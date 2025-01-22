package antip2w.tools.mixin.network.rawpacket;

import antip2w.tools.network.RawPacket;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.EncoderException;
import net.minecraft.network.handler.PacketCodecDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PacketCodecDispatcher.class)
public abstract class PacketCodecDispatcherMixin {

    @Inject(method = "encode(Lio/netty/buffer/ByteBuf;Ljava/lang/Object;)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/network/encoding/VarInts;write(Lio/netty/buffer/ByteBuf;I)Lio/netty/buffer/ByteBuf;", ordinal = 0), cancellable = true)
    private void processRawPacket(ByteBuf buf, Object object, CallbackInfo ci) {
        if (!(object instanceof RawPacket packet)) {
            return;
        }

        ci.cancel();

        try {
            packet.getEncoder().accept(buf);
        } catch (Exception exception) {
            throw new EncoderException("Failed to encode raw packet", exception);
        }
    }

}
