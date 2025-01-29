package antip2w.tools.command;

import antip2w.tools.command.argument.TickTimeRange;
import antip2w.tools.util.MCUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.function.BiConsumer;

import static antip2w.tools.command.argument.TimeRangeArgumentType.timeRange;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;

public class ForEach extends BetterCommand {

    private static final Deque<CommandContext> COMMANDS = new ArrayDeque<>();

    public ForEach() {
        super("foreach", "A complicated command documented in the README");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(arguments("players", "delayBetweenCommands", timeRange(), "command", greedyString(), this::executePlayers));
        builder.then(arguments("players", "command", greedyString(), this::executePlayers));
        builder.then(arguments("iterations", integer(1), "delayBetweenCommands", timeRange(), "command", greedyString(), this::execute));
        builder.then(arguments("iterations", integer(1), "command", greedyString(), this::execute));
    }

    @EventHandler
    private void postTick(TickEvent.Post event) {
        if (!Utils.canUpdate()) {
            if (!COMMANDS.isEmpty()) {
                COMMANDS.clear();
            }
            return;
        }

        long currentTime = MC.uptimeInTicks;

        for (Iterator<CommandContext> iterator = COMMANDS.iterator(); iterator.hasNext();) {
            CommandContext ctx = iterator.next();

            if (ctx.executeTime <= currentTime) {
                iterator.remove();
            }

            ctx.execute(this);
        }
    }

    private void execute(int iterations, String commandTemplate) {
        execute(iterations, TickTimeRange.NULL, commandTemplate);
    }

    private void execute(int iterations, TickTimeRange timeRange, String commandTemplate) {
        long lastDelayTicks = 0;

        for (int i = 0; i < iterations; i++) {
            String command = commandTemplate.replace("%iteration%", String.valueOf(i));
            COMMANDS.add(new CommandContext(command, MC.uptimeInTicks + lastDelayTicks));
            lastDelayTicks += timeRange.getRandomPoint();
        }
    }

    private void executePlayers(String commandTemplate) {
        executePlayers(TickTimeRange.NULL, commandTemplate);
    }

    private void executePlayers(TickTimeRange timeRange, String commandTemplate) {
        long lastDelayTicks = 0;

        for (PlayerListEntry entry : getPlayNetHandler().getPlayerList()) {
            String name = entry.getProfile().getName();

            String command = commandTemplate.replace("%player%", name);
            COMMANDS.add(new CommandContext(command, MC.uptimeInTicks + lastDelayTicks));
            lastDelayTicks += timeRange.getRandomPoint();
        }
    }

    private record CommandContext(String command, CommandType type, long executeTime) {

        private CommandContext(String command, long executeTime) {
            this(command, CommandType.fromCommand(command), executeTime);
        }

        private void execute(ForEach module) {
            type.execute(module, command);
        }

    }

    private enum CommandType {
        METEOR((m, message) -> {
            try {
                Commands.dispatch(message.substring(1));
            } catch (CommandSyntaxException e) {
                m.error(e.getMessage());
            }
        }),
        SERVER((m, command) -> MCUtil.sendCommand(command));

        private final BiConsumer<ForEach, String> dispatcher;

        CommandType(BiConsumer<ForEach, String> executor) {
            this.dispatcher = executor;
        }

        private void execute(ForEach module, String command) {
            dispatcher.accept(module, command);
        }

        private static CommandType fromCommand(String command) {
            return command.charAt(0) == '.' ? METEOR : SERVER;
        }

    }

}
