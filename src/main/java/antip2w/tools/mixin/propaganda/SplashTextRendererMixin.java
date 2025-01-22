package antip2w.tools.mixin.propaganda;

import net.minecraft.client.gui.screen.SplashTextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashTextRenderer.class)
public abstract class SplashTextRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    void dontRender(CallbackInfo ci) {
        ci.cancel();
    }

}
