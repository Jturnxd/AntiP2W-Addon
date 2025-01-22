package antip2w.tools.util;

import com.google.common.io.ByteArrayDataOutput;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.toast.Toast;
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

    default void sendCustomPayload(Identifier channel, byte[] data) {
        MCUtil.sendCustomPayload(channel, data);
    }

    default boolean notCreative() {
        return MCUtil.notCreative();
    }

    default boolean isSpectator() {
        return MCUtil.isSpectator();
    }

    default boolean notOp() {
        return MCUtil.notOp();
    }

    default boolean isCommandRegistered(String commandName) {
        return MCUtil.isCommandRegistered(commandName);
    }

    default void sendCommand(String command) {
        MCUtil.sendCommand(command);
    }

    default void tryAddToast(Toast toast) {
        MCUtil.tryAddToast(toast);
    }

    default boolean isActive(Class<? extends Module> clazz) {
        return MCUtil.isActive(clazz);
    }

    default void swingHand(boolean clientSide) {
        MCUtil.swingMainHand(clientSide);
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
