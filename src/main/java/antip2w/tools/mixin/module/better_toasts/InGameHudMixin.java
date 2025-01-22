package antip2w.tools.mixin.module.better_toasts;

import antip2w.tools.module.BetterToasts;
import antip2w.tools.util.MCUtil;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Redirect(method = "clear", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/toast/ToastManager;clear()V"))
    private void disableToastClearing(ToastManager instance) {
        if (MCUtil.isActive(BetterToasts.class) &&
            Modules.get().get(BetterToasts.class).clearOnDisconnect.get() ||
            !MCUtil.isActive(BetterToasts.class)
        ) {
            instance.clear();
        }
    }

}
