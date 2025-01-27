package antip2w.tools.command;

import antip2w.tools.command.argument.RandomDelayArgument;
import antip2w.tools.command.argument.Time;
import antip2w.tools.util.MCUtil;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static antip2w.tools.command.argument.RandomDelayArgumentType.randomDelay;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;

public class Foreach extends BetterCommand {

    public Foreach() {
        super("for", "Foreach command.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder
            .then(literal("players").then(arguments("delay", randomDelay(), "command", StringArgumentType.greedyString(), this::executePlayers)))
            .then(arguments("amount", integer(1), "delay", randomDelay(), "command", StringArgumentType.greedyString(), this::execute));
    }

    private static void runCommand(String command) {
        if (command.startsWith(".")) MCUtil.getPlayNetHandler().sendChatMessage(command);
        MCUtil.getPlayNetHandler().sendCommand(command);
    }

    private void execute(int amount, RandomDelayArgument delay, String command) {
        int lastDelay = 0;
        for (int i = 0; i < amount; i++) {
            Executor executor = CompletableFuture.delayedExecutor(lastDelay, TimeUnit.MILLISECONDS);
            CompletableFuture.runAsync(() -> forceMainThread(() -> runCommand(command)), executor);
            lastDelay += delay.generateNewValue(Time.TimeUnit.MILLISECONDS).amount();
        }
    }

    private void executePlayers(RandomDelayArgument delay, String command) {
        final int[] lastDelay = {0};
        getPlayNetHandler().getPlayerList().stream()
            .map(playerListEntry -> playerListEntry.getProfile().getName())
            .forEach(name -> {
                    Executor executor = CompletableFuture.delayedExecutor(lastDelay[0], TimeUnit.MILLISECONDS);
                    CompletableFuture.runAsync(() -> forceMainThread(() -> runCommand(command.replace("%player%", name))), executor);
                    lastDelay[0] += delay.generateNewValue(Time.TimeUnit.MILLISECONDS).amount();
            });
    }

}
