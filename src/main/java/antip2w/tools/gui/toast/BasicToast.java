package antip2w.tools.gui.toast;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BasicToast extends BetterToast {

    private static final Identifier SIMPLE_TEXTURE = Identifier.of("antip2w", "toast/simple_simple");
    private static final Identifier BOTTOM_TEXTURE = Identifier.of("antip2w", "toast/simple_bottom");
    private static final Identifier MIDDLE_TEXTURE = Identifier.of("antip2w", "toast/simple_middle");
    private static final Identifier TOP_TEXTURE = Identifier.of("antip2w", "toast/simple_top");
    private static final Identifier SIMPLE_TEXTURE_RED = Identifier.of("antip2w", "toast/simple_red_simple");
    private static final Identifier BOTTOM_TEXTURE_RED = Identifier.of("antip2w", "toast/simple_red_bottom");
    private static final Identifier MIDDLE_TEXTURE_RED = Identifier.of("antip2w", "toast/simple_red_middle");
    private static final Identifier TOP_TEXTURE_RED = Identifier.of("antip2w", "toast/simple_red_top");
    private static final MinecraftClient MC = MinecraftClient.getInstance();

    private final int heightCache;
    private final int maxTimeCache;
    private final int requiredSpaceCountCache;

    @Nullable
    private final List<OrderedText> description;
    private final boolean isImportant;
    private int remainingDings = 2;
    private long nextDingTime;
    private Visibility visibility;

    public BasicToast(Text title, @Nullable Text description, Item icon, boolean isImportant) {
        super(title, icon.getDefaultStack());
        this.description = description == null ? null : MC.textRenderer.wrapLines(description, 200 - 14);
        this.heightCache = (int) Math.ceil((12 + (this.description == null ? 1 : Math.max(this.description.size(), 1)) * 10) / 32.) * 32;
        this.requiredSpaceCountCache = MathHelper.ceilDiv(heightCache, 32);
        this.maxTimeCache = 2000 + (this.description == null ? 0 : this.description.size()) * 2000;
        this.isImportant = isImportant;
    }

    public BasicToast(String title, @Nullable String description, Item icon, boolean isImportant) {
        this(Text.of(title), description == null ? null : Text.of(description), icon, isImportant);
    }

    public BasicToast(String title, @Nullable String description) {
        this(Text.of(title), description == null ? null : Text.of(description), Items.COMMAND_BLOCK, false);
    }

    public BasicToast(String title) {
        this(Text.of(title), null, Items.COMMAND_BLOCK, false);
    }

    @Override
    public Toast.Visibility getVisibility() {
        return visibility;
    }

    @Override
    public int getWidth() {
        return 200;
    }

    @Override
    public int getHeight() {
        return heightCache;
    }

    @Override
    public int getRequiredSpaceCount() {
        return requiredSpaceCountCache;
    }

    @Override
    public void update(ToastManager manager, long time) {
        this.visibility = (double)time >= maxTimeCache * manager.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public void draw(DrawContext ctx, TextRenderer textRenderer, long startTime) {
        double toastTimeMul = MC.getToastManager().getNotificationDisplayTimeMultiplier();

        // background rendering
        boolean useRedTex = isImportant && startTime < 2000 && startTime % 1000 < 500;
        if (requiredSpaceCountCache == 1) {
            drawBackgroundPart(ctx, 0, useRedTex ? SIMPLE_TEXTURE_RED : SIMPLE_TEXTURE);
        } else if (requiredSpaceCountCache == 2) {
            drawBackgroundPart(ctx, 0, useRedTex ? TOP_TEXTURE_RED : TOP_TEXTURE);
            drawBackgroundPart(ctx, 32, useRedTex ? BOTTOM_TEXTURE_RED : BOTTOM_TEXTURE);
        } else {
            drawBackgroundPart(ctx, 0, useRedTex ? TOP_TEXTURE_RED : TOP_TEXTURE);

            int midSections = requiredSpaceCountCache - 2;
            for (int midSection = 0; midSection < midSections; midSection++) {
                drawBackgroundPart(ctx, 32 * (midSection + 1), useRedTex ? MIDDLE_TEXTURE_RED : MIDDLE_TEXTURE);
            }

            drawBackgroundPart(ctx, heightCache - 32, useRedTex ? BOTTOM_TEXTURE_RED : BOTTOM_TEXTURE);
        }

        if (startTime < 1500 * toastTimeMul || description == null) {
            // item and title rendering
            ctx.drawItem(icon, 4, this.getHeight() / 2 - 9);
            ctx.drawText(textRenderer, this.title, 24, (this.getHeight() - 8) / 2, Colors.YELLOW, false);
        } else {
            // description rendering
            int textHeight = this.description.size() * 10;
            for (int k = 0; k < this.description.size(); k++) {
                ctx.drawText(textRenderer, this.description.get(k), 7, (this.getHeight() - textHeight) / 2 + k * 10 + 1, -1, false);
            }
        }

        // progress bar rendering
        ctx.fill(3, heightCache - 3, getProgressBarEnd(startTime, (int) (maxTimeCache * toastTimeMul)), heightCache - 2, 0xFF00FF00);

        if (nextDingTime < startTime && remainingDings > 0 && isImportant) {
            MC.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE.value(), 1f, 1f));
            remainingDings--;
            nextDingTime = startTime + 1000L;
        }
    }

    private void drawBackgroundPart(DrawContext ctx, int y, Identifier sprite) {
        ctx.drawGuiTexture(RenderLayer::getGuiTextured, sprite, 200, 32, 0, 0, 0, y, 200, 32);
    }

    public int getProgressBarEnd(long startTime, long maxTime) {
        float delta = MathHelper.clamp((float) startTime / maxTime, 0f, 1f);
        return (int) ((getWidth() - 6) * delta) + 3;
    }
}
