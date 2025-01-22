package antip2w.tools.module;

import antip2w.tools.util.MCUtil;
import net.minecraft.util.StringIdentifiable;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.util.math.MatrixStack;

public class BetterToasts extends BetterModule {

    private final SettingGroup defaultGroup = settings.getDefaultGroup();

    private final Setting<ToastLocation> location = defaultGroup.add(new EnumSetting.Builder<ToastLocation>()
        .name("location")
        .description("Where to show the toasts.")
        .defaultValue(ToastLocation.BOTTOM_RIGHT)
        .build()
    );

    public final Setting<Integer> toastPlaces = defaultGroup.add(new IntSetting.Builder()
        .name("toast-places")
        .description("The number of free toast places")
        .defaultValue(5)
        .range(5, 50)
        .sliderRange(5, 100)
        .build()
    );

    public final Setting<Boolean> clearOnDisconnect = defaultGroup.add(new BoolSetting.Builder()
        .name("clear-on-disconnect")
        .description("Whether to clear the toasts on disconnect. Also clears the current toasts when toggled.")
        .defaultValue(true)
        .onChanged(value -> {
            if (value) {
                MC.getToastManager().clear();
            }
        })
        .build()
    );

    public BetterToasts() {
        super("better-toasts", "Makes toasts better.");
    }

    public static void translateToast(MatrixStack matrices, Toast toast, float x, float y, float z) {
        if (!MCUtil.isActive(BetterToasts.class)) {
            matrices.translate(x, y, z);
            return;
        }

        switch (Modules.get().get(BetterToasts.class).location.get()) {
            case TOP_LEFT -> matrices.translate(MC.getWindow().getScaledWidth() - x - toast.getWidth(), y, z);
            case TOP_RIGHT -> matrices.translate(x, y, z);
            case BOTTOM_RIGHT -> matrices.translate(x, MC.getWindow().getScaledHeight() - Math.abs(y) - (toast.getHeight() + 6), z);
        }
    }

    private enum ToastLocation implements StringIdentifiable {
        TOP_LEFT("Top left"),
        TOP_RIGHT("Top right"),
        BOTTOM_RIGHT("Bottom right");

        private final String label;

        ToastLocation(String label) {
            this.label = label;
        }

        @Override
        public String asString() {
            return label;
        }
    }

}
