package antip2w.tools.mixin.module.better_toasts;

import antip2w.tools.module.BetterToasts;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.BitSet;

@Mixin(ToastManager.class)
public abstract class ToastManagerMixin {

    @Mutable
    @Final
    @Shadow
    private BitSet occupiedSpaces;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void afterInit(MinecraftClient client, CallbackInfo ci) {
        this.occupiedSpaces = new BitSet(100);
    }

    @ModifyConstant(method = "getTopIndex", constant = @Constant(intValue = 5))
    private int modifyConstant1(int original) {
        BetterToasts module = Modules.get().get(BetterToasts.class);
        return module.isActive() ? module.toastPlaces.get() : original;
    }

    @ModifyConstant(method = "getEmptySpaceCount", constant = @Constant(intValue = 5))
    private int modifyConstant2(int original) {
        BetterToasts module = Modules.get().get(BetterToasts.class);
        return module.isActive() ? module.toastPlaces.get() : original;
    }

}
