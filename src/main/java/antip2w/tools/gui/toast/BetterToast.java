package antip2w.tools.gui.toast;

import net.minecraft.client.toast.Toast;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public abstract class BetterToast implements Toast {

    protected Text title;
    protected final ItemStack icon;

    protected BetterToast(Text title, ItemStack icon) {
        this.title = title;
        this.icon = icon;
    }

    public void setTitle(Text title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = Text.of(title);
    }

}
