package antip2w.tools.module;

import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;

public class FreeInteract extends BetterModule {

    private final SettingGroup defaultGroup = settings.getDefaultGroup();

    public final Setting<Boolean> useAndAttack = defaultGroup.add(new BoolSetting.Builder()
        .name("use-and-attack")
        .description("Lets you attack while using an item.")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> interactInBoat = defaultGroup.add(new BoolSetting.Builder()
        .name("interact-in-boat")
        .description("Lets you attack and use items in a boat while moving.")
        .defaultValue(true)
        .build()
    );

    public FreeInteract() {
        super("free-interact", "Removes interaction limits.");
    }

}
