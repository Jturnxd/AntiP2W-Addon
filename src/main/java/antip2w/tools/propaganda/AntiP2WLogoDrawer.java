package antip2w.tools.propaganda;

import antip2w.tools.AntiP2WTools;
import antip2w.tools.util.MCUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LogoDrawer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class AntiP2WLogoDrawer extends LogoDrawer {

    public static final Identifier LOGO_TEXTURE = Identifier.of("antip2w", "textures/gui/title/logo.png");

    public AntiP2WLogoDrawer(boolean ignoreAlpha) {
        super(ignoreAlpha);
    }

    @Override
    public void draw(DrawContext context, int screenWidth, float alpha) {
        this.draw(context, screenWidth, alpha, 20);
    }

    @Override
    public void draw(DrawContext context, int screenWidth, float alpha, int y) {
        float f = this.ignoreAlpha ? 1.0f : alpha;
        int color = ColorHelper.getWhite(f);
        context.drawTexture(RenderLayer::getGuiTextured, LOGO_TEXTURE, screenWidth / 2 - 128, y, 0, 0, 256, 64, 256, 64, color);

        String text = AntiP2WTools.IS_DEV_ENV ? "Running in IDE" : "Build " + AntiP2WTools.METADATA.getVersion().getFriendlyString();
        int textWidth = MCUtil.MC.textRenderer.getWidth(text);
        int textX = screenWidth / 2 - textWidth / 2;
        context.drawText(MCUtil.MC.textRenderer, text, textX, y + 64, color, true);
    }
}
