package antip2w.tools.mixin.network.rawpacket;

import antip2w.tools.network.ByteBufUpgrader;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(RegistryByteBuf.class)
public abstract class RegistryByteBufMixin {

    @Inject(method = "makeFactory", at = @At("HEAD"))
    private static void onMakeFactory(DynamicRegistryManager registryManager, CallbackInfoReturnable<Function<ByteBuf, RegistryByteBuf>> cir) {
        ByteBufUpgrader.lastRegistryManager = registryManager;
    }

}
