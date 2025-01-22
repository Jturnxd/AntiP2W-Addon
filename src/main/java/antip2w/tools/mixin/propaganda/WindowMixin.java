package antip2w.tools.mixin.propaganda;

import antip2w.tools.AntiP2WTools;
import net.minecraft.client.util.Icons;
import net.minecraft.client.util.Window;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Mixin(Window.class)
public abstract class WindowMixin {

    @Redirect(method = "setIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Icons;getIcons(Lnet/minecraft/resource/ResourcePack;)Ljava/util/List;"))
    private List<InputSupplier<InputStream>> redirectSetIcon(Icons instance, ResourcePack resourcePack) {
        return getIcons();
    }

    @Unique
    private static final int[] iconResolutions = {8, 16, 24, 32, 48, 64, 128, 256};

    @Unique
    private static List<InputSupplier<InputStream>> getIcons() {
        List<InputSupplier<InputStream>> icons = new ArrayList<>(iconResolutions.length);

        for (int r : iconResolutions) {
            String name = "/assets/antip2w/icon/" + r + "x" + r + ".png";
            icons.add(() -> AntiP2WTools.class.getResourceAsStream(name));
        }

        return icons;
    }

}
