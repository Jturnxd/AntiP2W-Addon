package antip2w.tools.command.argument;

import antip2w.tools.util.Util;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import java.util.Arrays;
import java.util.Collection;

public class RandomDelayArgumentType implements ArgumentType<RandomDelayArgument> {

    private static final Collection<String> EXAMPLES = Arrays.asList("3s-5", "60t-5s", "100s", "100t", "0");

    public static RandomDelayArgumentType randomDelay() {
        return new RandomDelayArgumentType();
    }

    public static RandomDelayArgument getRandom(final CommandContext<?> context, final String name) {
        return context.getArgument(name, RandomDelayArgument.class);
    }

    @Override
    public RandomDelayArgument parse(final StringReader reader) throws CommandSyntaxException {
        Time min = TimeArgumentType.parseTime(reader);

        if (reader.peek() != '-') return new RandomDelayArgument(min);

        reader.skip();

        Time max = TimeArgumentType.parseTime(reader);

        return new RandomDelayArgument(min, max);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        return o instanceof RandomDelayArgumentType;
    }

    @Override
    public String toString() {
        return "randomDelay()";
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
