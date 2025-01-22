package antip2w.tools.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.systems.accounts.types.CrackedAccount;
import meteordevelopment.meteorclient.utils.Utils;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.mojang.brigadier.arguments.StringArgumentType.word;

public class Reconnect extends BetterCommand {

    public Reconnect() {
        super("reconnect", "Reconnects to the server you are playing on, optionally with another name", "rejoin");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(wrapWithSuccess(() -> execute(null)));
        builder.then(argument("new_name", word(), this::execute));
    }

    private void execute(@Nullable String newName) {
        if (!Utils.canUpdate()) {
            return;
        }

        if (MC.isInSingleplayer()) {
            warning("You are in Singleplayer!");
            return;
        }

        if (newName != null) {
            new CrackedAccount(newName).login();
        }

        ServerInfo server = MC.getCurrentServerEntry();

        MC.world.disconnect();
        MC.disconnect();

        // TODO impl async bugfix properly
        CompletableFuture.delayedExecutor(100, TimeUnit.MILLISECONDS).execute(() -> forceMainThread(() ->
            ConnectScreen.connect(new MultiplayerScreen(new TitleScreen()),
                MC,
                ServerAddress.parse(server.address),
                server,
                false,
                null
            )
        ));
    }

}
