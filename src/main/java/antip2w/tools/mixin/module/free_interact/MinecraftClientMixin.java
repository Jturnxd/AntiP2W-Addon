package antip2w.tools.mixin.module.free_interact;

import antip2w.tools.module.FreeInteract;
import antip2w.tools.util.MCUtil;
import antip2w.tools.util.Util;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", ordinal = 0))
    private void removeLimit(CallbackInfo ci, @Local LocalBooleanRef bl3) {
        if (Util.isActiveAnd(FreeInteract.class, m -> m.useAndAttack.get())) {
            while (MCUtil.MC.options.attackKey.wasPressed()) {
                bl3.set(bl3.get() | MCUtil.MC.doAttack());
            }
        }
    }

    @Redirect(method = "handleBlockBreaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean redirectIsUsingItem(ClientPlayerEntity instance) {
        if (Util.isActiveAnd(FreeInteract.class, m -> m.useAndAttack.get())) {
            return false;
        }

        return instance.isUsingItem();
    }

}
