package antip2w.tools.command.argument;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class TimeArgumentType implements ArgumentType<Time> {

    public static final SimpleCommandExceptionType INVALID_TIME_EXCEPTION = new SimpleCommandExceptionType(new LiteralMessage("Expected time"));;
    private static final Collection<String> EXAMPLES = Arrays.asList("100s", "100t", "100ms");

    public static RandomDelayArgumentType randomDelay() {
        return new RandomDelayArgumentType();
    }

    public static Time getTime(final CommandContext<?> context, final String name) {
        return context.getArgument(name, Time.class);
    }

    public static Time parseTime(final StringReader reader) throws CommandSyntaxException {
        int start = reader.getCursor();

        if (reader.peek() == '-') {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooLow().createWithContext(reader, "negative value", 0);
        }

        StringBuilder string = new StringBuilder();
        while (reader.canRead() && Character.isDigit(reader.peek())) {
            string.append(reader.read());
        }

        int time;
        try {
            time = Integer.parseInt(string.toString());
        } catch (NumberFormatException e) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(reader, string);
        }

        start = reader.getCursor();
        string = new StringBuilder();

        while (reader.canRead() && Character.isLetter(reader.peek())) {
            string.append(reader.read());
        }

        Time.TimeUnit unit;
        try {
            unit = Time.TimeUnit.fromString(string.toString());
        } catch (Time.InvalidTimeUnitException e) {
            reader.setCursor(start);
            throw INVALID_TIME_EXCEPTION.createWithContext(reader);
        }

        return new Time(unit, time);
    }

    @Override
    public Time parse(final StringReader reader) throws CommandSyntaxException {
        return parseTime(reader);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        return o instanceof TimeArgumentType;
    }

    @Override
    public String toString() {
        return "time()";
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
