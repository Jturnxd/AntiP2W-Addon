package antip2w.tools.mixin.util.meteor_string_identifiable_support;

import net.minecraft.util.StringIdentifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "meteordevelopment.meteorclient.gui.themes.meteor.widgets.input.WMeteorDropdown$WValue", remap = false)
public abstract class WMeteorDropdown$WValueMixin {

    @Redirect(method = "onCalculateSize", at = @At(value = "INVOKE", target = "Ljava/lang/Object;toString()Ljava/lang/String;"))
    private String redirect1(Object instance) {
        return instance instanceof StringIdentifiable si ? si.asString() : instance.toString();
    }

    @Redirect(method = "onRender", at = @At(value = "INVOKE", target = "Ljava/lang/Object;toString()Ljava/lang/String;"))
    private String redirect2(Object instance) {
        return instance instanceof StringIdentifiable si ? si.asString() : instance.toString();
    }

}
