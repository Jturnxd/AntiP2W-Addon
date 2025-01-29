package antip2w.tools.mixin.module.free_interact;

import antip2w.tools.module.FreeInteract;
import antip2w.tools.util.Util;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Inject(method = "isRiding", at = @At("HEAD"), cancellable = true)
    private void modifyIsRiding(CallbackInfoReturnable<Boolean> cir) {
        if (Util.isActiveAnd(FreeInteract.class, m -> m.interactInBoat.get())) {
            cir.setReturnValue(false);
        }
    }

}
