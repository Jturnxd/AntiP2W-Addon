package antip2w.tools.util;

import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public interface MCUtilWrapper {

    MinecraftClient MC = MCUtil.MC;

    default ClientPlayNetworkHandler getPlayNetHandler() {
        return MCUtil.getPlayNetHandler();
    }

    default void sendPacket(Packet<?> packet) {
        MCUtil.sendPacket(packet);
    }

    default void sendCustomPayload(Identifier channel, Consumer<ByteArrayDataOutput> writer) {
        MCUtil.sendCustomPayload(channel, writer);
    }

    default boolean notCreative() {
        return MCUtil.notCreative();
    }

    default boolean isCommandRegistered(String commandName) {
        return MCUtil.isCommandRegistered(commandName);
    }

    default void sendCommand(String command) {
        MCUtil.sendCommand(command);
    }

    default ItemStack getStackInSlot(int slot) {
        return MCUtil.getStackInSlot(slot);
    }

    default int getSelectedSlot() {
        return MCUtil.getSelectedSlot();
    }

    default ItemStack getStackInSelectedSlot() {
        return MCUtil.getStackInSelectedSlot();
    }

    default void forceMainThread(Runnable runnable) {
        MCUtil.forceMainThread(runnable);
    }

}
