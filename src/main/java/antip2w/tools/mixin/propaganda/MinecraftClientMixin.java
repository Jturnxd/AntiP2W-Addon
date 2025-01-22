package antip2w.tools.mixin.propaganda;

import antip2w.tools.AntiP2WTools;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Inject(method = "getWindowTitle", at = @At("HEAD"), cancellable = true)
    private void changeTitle(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("AntiP2W Tools (" + (AntiP2WTools.IS_DEV_ENV ? "running in IDE" : AntiP2WTools.METADATA.getVersion().getFriendlyString()) + ')');
    }

}
