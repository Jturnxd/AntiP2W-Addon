package antip2w.tools.mixin.module.better_toasts;

import antip2w.tools.module.BetterToasts;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.client.toast.ToastManager$Entry")
public abstract class ToastManager$EntryMixin {

    @Final
    @Shadow
    private Toast instance;

    @Redirect(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"))
    void redirectTranslate(MatrixStack matrices, float x, float y, float z) {
        BetterToasts.translateToast(matrices, instance, x, y, z);
    }

}
