package antip2w.tools.module;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class AutoAuth extends BetterModule {

    private final SettingGroup defaultGroup = settings.getDefaultGroup();

    private final Setting<Boolean> loginAfterRegister = defaultGroup.add(new BoolSetting.Builder()
        .name("login-after-register")
        .description("Whether to run /login directly after /register or wait until the server asks you to login.")
        .defaultValue(true)
        .build()
    );

    private final Setting<String> password = defaultGroup.add(new StringSetting.Builder()
        .name("password")
        .description("The password to login with")
        .defaultValue("12345678")
        .build()
    );

    private boolean loggedIn = false;

    public AutoAuth() {
        super("auto-auth", "Automatically authenticates you on server join where supported.");
    }


    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event) {
        if (loggedIn || !(event.packet instanceof GameMessageS2CPacket packet)) {
            return;
        }

        String message = packet.content().getString();

        if (message.contains("/reg")) {
            if (!isCommandRegistered("register")) {
                loggedIn = true;
                return;
            }

            info("Attempting to register");
            sendCommand("register " + password.get() + " " + password.get());
            if (loginAfterRegister.get()) {
                sendCommand("login " + password.get());
                loggedIn = true;
            }
        } else if (message.contains("/login")) {
            if (!isCommandRegistered("login")) {
                loggedIn = true;
                return;
            }

            info("Attempting to log in");
            sendCommand("login " + password.get());
            loggedIn = true;
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        loggedIn = false;
    }
}
