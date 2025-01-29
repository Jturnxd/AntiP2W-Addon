package antip2w.tools.util;

import antip2w.tools.network.SimpleRawPacket;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.CommonPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class MCUtil {

    public static final MinecraftClient MC = MinecraftClient.getInstance();

    public static ClientPlayNetworkHandler getPlayNetHandler() {
        return MC.getNetworkHandler();
    }

    public static void sendPacket(Packet<?> packet) {
        getPlayNetHandler().sendPacket(packet);
    }

    public static void sendCustomPayload(Identifier channel, Consumer<ByteArrayDataOutput> writer) {
        ByteArrayDataOutput container = ByteStreams.newDataOutput();
        writer.accept(container);
        sendCustomPayload(channel, container.toByteArray());
    }

    public static void sendCustomPayload(Identifier channel, byte[] data) {
        sendPacket(SimpleRawPacket.of(CommonPackets.CUSTOM_PAYLOAD_C2S, buf -> {
            buf.writeIdentifier(channel);
            buf.writeBytes(data);
        }));
    }

    public static boolean notCreative() {
        return !MC.player.isCreative();
    }

    public static boolean isCommandRegistered(String commandName) {
        return getPlayNetHandler().getCommandDispatcher().getRoot().getChild(commandName) != null;
    }

    public static void sendCommand(String command) {
        getPlayNetHandler().sendCommand(command);
    }

    public static ItemStack getStackInSlot(int slot) {
        return MC.player.getInventory().getStack(slot);
    }

    public static int getSelectedSlot() {
        return MC.player.getInventory().selectedSlot;
    }

    public static ItemStack getStackInSelectedSlot() {
        return getStackInSlot(getSelectedSlot());
    }

    public static void forceMainThread(Runnable runnable) {
        MC.execute(runnable);
    }

}
