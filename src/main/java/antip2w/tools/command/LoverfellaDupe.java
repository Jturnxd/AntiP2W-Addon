package antip2w.tools.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;

public class LoverfellaDupe extends BetterCommand {

    public LoverfellaDupe() {
        super("loverfella-dupe", "Does the Loverfella dupe.", "lf-dupe");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(wrapWithSuccess(this::execute));
    }

    private void execute() {
        sendCommand("ah sell 10000");
        sendPacket(PlayerInteractEntityC2SPacket.attack(MC.player, MC.player.isSneaking()));
    }

}
