package antip2w.tools.mixin.module.book_colors;

import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;
import java.util.Optional;

@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin {

    @ModifyArgs(method = "finalizeBook", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/c2s/play/BookUpdateC2SPacket;<init>(ILjava/util/List;Ljava/util/Optional;)V"))
    private void modifyArgs(Args args) {
        List<String> pages = args.get(1);
        Optional<String> title = args.get(2);

        args.set(1, pages.stream().map(s -> s.replace("\\&", "§")));
        args.set(2, title.map(s -> s.replace("\\&", "§")));
    }

}
