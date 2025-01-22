package antip2w.tools.util;

import antip2w.tools.network.SimpleRawPacket;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.toast.Toast;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.CommonPackets;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
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

    public static boolean isSpectator() {
        return MC.player.isSpectator();
    }

    public static boolean notOp() {
        return !MC.player.hasPermissionLevel(4);
    }

    public static boolean isCommandRegistered(String commandName) {
        return getPlayNetHandler().getCommandDispatcher().getRoot().getChild(commandName) != null;
    }

    public static void sendCommand(String command) {
        getPlayNetHandler().sendCommand(command);
    }

    public static void tryAddToast(Toast toast) {
        if (MC.getToastManager().getEmptySpaceCount() < toast.getRequiredSpaceCount()) {
            return;
        }

        MC.getToastManager().add(toast);
        MC.getToastManager().update();
    }

    public static boolean isActive(Class<? extends Module> clazz) {
        return Modules.get().isActive(clazz);
    }

    public static void swingMainHand(boolean clientSide) {
        if (clientSide) {
            MC.player.swingHand(Hand.MAIN_HAND);
        } else {
            sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
        }
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
