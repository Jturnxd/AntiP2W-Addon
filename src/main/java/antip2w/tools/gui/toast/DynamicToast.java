package antip2w.tools.gui.toast;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class DynamicToast extends BetterToast {

    private static final Identifier TEXTURE = Identifier.of("antip2w", "toast/simple_simple");

    public int progress = 0;
    public int maxProgress = Integer.MAX_VALUE;
    private boolean completed = false;

    public DynamicToast(Text title, Item item, int maxProgress) {
        super(title, item.getDefaultStack());
        this.maxProgress = maxProgress;
    }

    public DynamicToast(Text title, Item item) {
        super(title, item.getDefaultStack());
    }

    public DynamicToast(String title, Item item, int maxProgress) {
        this(Text.of(title), item, maxProgress);
    }

    public DynamicToast(String title, Item item) {
        this(Text.of(title), item);
    }

    public void addProgress() {
        this.progress++;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setCompleted() {
        this.completed = true;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    @Override
    public Visibility getVisibility() {
        return completed ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public int getWidth() {
        return 200;
    }

    @Override
    public void update(ToastManager manager, long time) {
    }

    @Override
    public void draw(DrawContext ctx, TextRenderer textRenderer, long startTime) {
        ctx.drawGuiTexture(RenderLayer::getGuiTextured, TEXTURE, 200, 32, 0, 0, 0, 0, 200, 32);

        ctx.drawItem(icon, 4, getHeight() / 2 - 9);
        ctx.drawText(textRenderer, this.title, 24, (getHeight() - 8) / 2, Colors.YELLOW, false);

        ctx.fill(3, getHeight() - 3, MathHelper.floor(3 + (getWidth() - 6) * MathHelper.clamp((float) progress / maxProgress, 0.0f, 1.0f)), getHeight() - 2, 0xFF00FF00);
    }

}
