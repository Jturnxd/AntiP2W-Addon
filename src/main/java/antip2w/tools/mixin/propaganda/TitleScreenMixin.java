package antip2w.tools.mixin.propaganda;

import antip2w.tools.propaganda.AntiP2WLogoDrawer;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {

    @Mutable
    @Final
    @Shadow
    private LogoDrawer logoDrawer;

    @Inject(method = "<init>(ZLnet/minecraft/client/gui/LogoDrawer;)V", at = @At(value = "TAIL"))
    private void afterInit(CallbackInfo ci) {
        this.logoDrawer = new AntiP2WLogoDrawer(false);
    }

}
