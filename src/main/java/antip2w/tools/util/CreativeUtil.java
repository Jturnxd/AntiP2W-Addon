package antip2w.tools.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CreativeUtil {

    private static ItemStack savedStack = null;

    public static void saveHeldStack() {
        savedStack = MCUtil.getStackInSelectedSlot();
    }

    public static void restoreHeldStack() {
        setSelectedSlot(savedStack);
    }

    public static void setSelectedSlot(ItemStack stack) {
       setSlot(MCUtil.getSelectedSlot(), stack);
    }

    public static void setSlot(int slot, ItemStack stack) {
        MCUtil.MC.player.getInventory().setStack(slot, stack);
        MCUtil.MC.interactionManager.clickCreativeStack(stack, slot + 36);
    }

    public static void interactWBlockAtEyes() {
        Vec3d eyePos = MCUtil.MC.player.getPos();

        BlockPos blockPos = new BlockPos(
            MathHelper.floor(eyePos.x),
            MathHelper.floor(eyePos.y),
            MathHelper.floor(eyePos.z)
        );

        BlockHitResult bhr = new BlockHitResult(
            eyePos,
            Direction.UP,
            blockPos,
            false
        );

        MCUtil.MC.interactionManager.interactBlock(MCUtil.MC.player, Hand.MAIN_HAND, bhr);
    }

}
