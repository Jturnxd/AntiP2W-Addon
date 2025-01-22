package antip2w.tools.command;

import antip2w.tools.AntiP2WTools;
import antip2w.tools.util.CreativeUtil;
import antip2w.tools.util.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class Hologram extends BetterCommand {

    private CompletableFuture<BufferedImage> imageSupplier = null;
    private String lastImagePath = null;

    public Hologram() {
        super("hologram", "Loads an image into the world. (requires creative mode)", "holo");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(wrapWithSuccess(() -> execute(false)));
        builder.then(literal("last", () -> execute(true)));
    }

    private void execute(boolean getLast) {
        if (notCreative()) {
            warning("You're not in creative mode");
            return;
        }

        if (getLast && lastImagePath == null) {
            warning("No last image is present");
            return;
        }

        if (imageSupplier != null && !imageSupplier.isDone()) {
            warning("An image is already loading or a dialog is already open");
            return;
        }

        imageSupplier = CompletableFuture.supplyAsync(() -> tryGetImage(getLast ? lastImagePath : askForImagePath()));
    }

    @EventHandler
    private void postTick(TickEvent.Post event) {
        if (!Utils.canUpdate() || notCreative() || imageSupplier == null || !imageSupplier.isDone()) {
            return;
        }

        BufferedImage image = imageSupplier.getNow(null);
        if (image == null) {
            return;
        }

        int width = image.getWidth();
        int height = image.getHeight();

        CreativeUtil.saveHeldStack();

        Vec3d playerPos = MC.player.getPos();

        for (int y = 0; y < height; y++) {
            int[] colorRow = new int[width];

            for (int x = 0; x < width; x++) {
                colorRow[x] = image.getRGB(x, y);
            }

            Vec3d pos = playerPos.offset(Direction.UP, (height - y) * 0.23);
            CreativeUtil.setSelectedSlot(getSpawnEgg(pos, colorRow));
            CreativeUtil.interactWBlockAtEyes();
        }

        CreativeUtil.restoreHeldStack();
        imageSupplier = null;
    }

    private static ItemStack getSpawnEgg(Vec3d pos, int[] colorRow) {
        ItemStack spawnEgg = Items.COD_SPAWN_EGG.getDefaultStack();
        NbtCompound nbt = new NbtCompound();

        nbt.putString("id", "armor_stand");
        NbtList pos2 = new NbtList();
        pos2.add(NbtDouble.of(pos.x));
        pos2.add(NbtDouble.of(pos.y));
        pos2.add(NbtDouble.of(pos.z));
        nbt.put("Pos", pos2);
        nbt.putBoolean("CustomNameVisible", true);
        nbt.putBoolean("Marker", true);
        nbt.putBoolean("Invisible", true);
        nbt.putString("CustomName", getCustomName(colorRow));

        spawnEgg.set(DataComponentTypes.ENTITY_DATA, NbtComponent.of(nbt));
        return spawnEgg;
    }

    private static String getCustomName(int[] colorRow) {
        JsonArray customName = new JsonArray();

        for (int color : colorRow) {
            JsonObject object = new JsonObject();
            object.addProperty("color", getHexFormat(color));
            object.addProperty("text", "█");
            customName.add(object);
        }

        return customName.toString();
    }

    private static String getHexFormat(int color) {
        return "#" +
            Util.leftZeroPad(Integer.toHexString(color >> 16 & 0xFF), 2) +
            Util.leftZeroPad(Integer.toHexString(color >> 8 & 0xFF), 2) +
            Util.leftZeroPad(Integer.toHexString(color & 0xFF), 2);
    }

    private static final PointerBuffer imageFilter = BufferUtils.createPointerBuffer(4)
        .put(MemoryUtil.memASCII("*.jpg"))
        .put(MemoryUtil.memASCII("*.jpeg"))
        .put(MemoryUtil.memASCII("*.png"))
        .put(MemoryUtil.memASCII("*.gif"))
        .rewind();

    @Nullable
    private String askForImagePath() {
        lastImagePath = TinyFileDialogs.tinyfd_openFileDialog("Select Image", null, imageFilter, "Image Files", false);

        return lastImagePath;
    }

    @Nullable
    private BufferedImage tryGetImage(String path) {
        if (path == null) {
            return null;
        }

        File file = new File(path);
        BufferedImage image;

        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            warning("Exception while reading image, see logs for more info");
            AntiP2WTools.LOGGER.warn("Exception while reading image: ", e);
            return null;
        }

        if (image.getWidth() * image.getHeight() > 128 * 128) {
            info("Scaling image: %dx%d -> 128x128", image.getWidth(), image.getHeight());
            image = scaleImage(image);
        }

        return image;
    }

    private static BufferedImage scaleImage(BufferedImage image) {
        BufferedImage resized = new BufferedImage(128, 128, image.getType());
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(image, 0, 0, 128, 128, 0, 0, image.getWidth(), image.getHeight(), null);
        g.dispose();
        return resized;
    }

}
