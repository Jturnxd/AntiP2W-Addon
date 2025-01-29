package antip2w.tools.mixin.module.better_toasts;

import antip2w.tools.module.BetterToasts;
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
        BetterToasts m = Modules.get().get(BetterToasts.class);

        if (m.isActive() && m.clearOnDisconnect.get() || !m.isActive()) {
            instance.clear();
        }
    }

}
