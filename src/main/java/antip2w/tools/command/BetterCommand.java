package antip2w.tools.command;

import antip2w.tools.util.MCUtilWrapper;
import antip2w.tools.util.functional_interface.TriConsumer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import meteordevelopment.meteorclient.MeteorClient;
import net.minecraft.command.CommandSource;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public abstract class BetterCommand extends meteordevelopment.meteorclient.commands.Command implements MCUtilWrapper {

    public BetterCommand(String name, String description, String... aliases) {
        super(name, description, aliases);

        // Event bus support
        MeteorClient.EVENT_BUS.subscribe(this);
    }

    protected static Command<CommandSource> wrapWithSuccess(Consumer<CommandContext<CommandSource>> consumer) {
        return context -> {
            consumer.accept(context); return Command.SINGLE_SUCCESS;
        };
    }

    protected static Command<CommandSource> wrapWithSuccess(Runnable runnable) {
        return context -> {
            runnable.run(); return Command.SINGLE_SUCCESS;
        };
    }

    protected static <A> RequiredArgumentBuilder<CommandSource, A> argument(
        String name, ArgumentType<A> type, Consumer<A> executes
    ) {
        return argument(name, type).executes(wrapWithSuccess(ctx -> executes.accept(
            ctx.getArgument(name, (Class<A>) Object.class)
        )));
    }

    protected static <A, B> RequiredArgumentBuilder<CommandSource, A> arguments(
        String name1, ArgumentType<A> type1,
        String name2, ArgumentType<B> type2, BiConsumer<A, B> executes
    ) {
        return argument(name1, type1).then(
            argument(name2, type2).executes(wrapWithSuccess(ctx -> executes.accept(
                ctx.getArgument(name1, (Class<A>) Object.class),
                ctx.getArgument(name2, (Class<B>) Object.class)
            )))
        );
    }

    protected static <A, B, C> RequiredArgumentBuilder<CommandSource, A> arguments(
        String name1, ArgumentType<A> type1,
        String name2, ArgumentType<B> type2,
        String name3, ArgumentType<C> type3, TriConsumer<A, B, C> executes
    ) {
        return argument(name1, type1).then(
            argument(name2, type2).then(
                argument(name3, type3).executes(wrapWithSuccess(ctx -> executes.accept(
                    ctx.getArgument(name1, (Class<A>) Object.class),
                    ctx.getArgument(name2, (Class<B>) Object.class),
                    ctx.getArgument(name3, (Class<C>) Object.class)
                )))
            )
        );
    }

    protected static <T> RequiredArgumentBuilder<CommandSource, T> argument(String name, ArgumentType<T> type, BiConsumer<CommandContext<CommandSource>, T> executes) {
        return argument(name, type).executes(wrapWithSuccess(ctx -> executes.accept(ctx, ctx.getArgument(name, (Class<T>) Object.class))));
    }

    protected static <A> LiteralArgumentBuilder<CommandSource> arguments(
        String literal,
        String name, ArgumentType<A> type, Consumer<A> executes
    ) {
        return literal(literal).then(
            argument(name, type).executes(wrapWithSuccess(ctx -> executes.accept(
                ctx.getArgument(name, (Class<A>) Object.class)
            )))
        );
    }

    protected static <A, B> LiteralArgumentBuilder<CommandSource> arguments(
        String literal,
        String name1, ArgumentType<A> type1,
        String name2, ArgumentType<B> type2, BiConsumer<A, B> executes
    ) {
        return literal(literal).then(
            argument(name1, type1).then(
                argument(name2, type2).executes(wrapWithSuccess(ctx -> executes.accept(
                    ctx.getArgument(name1, (Class<A>) Object.class),
                    ctx.getArgument(name2, (Class<B>) Object.class)
                )
            )))
        );
    }

    protected static LiteralArgumentBuilder<CommandSource> literal(String name, Consumer<CommandContext<CommandSource>> executes) {
        return literal(name).executes(wrapWithSuccess(executes));
    }

    protected static LiteralArgumentBuilder<CommandSource> literal(String name, Runnable executes) {
        return literal(name).executes(wrapWithSuccess(executes));
    }
}
