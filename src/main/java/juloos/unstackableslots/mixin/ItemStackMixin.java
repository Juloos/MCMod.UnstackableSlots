package juloos.unstackableslots.mixin;

import com.mojang.serialization.DataResult;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    /**
     * @author Juloos
     * @reason Set the default max stack size to 1
     */
    @Overwrite
    public int getMaxStackSize() {
        return 1;
    }

    /**
     * @author Juloos
     * @reason Do not error with stack size larger than max stack size
     */
    @Overwrite
    private static DataResult<ItemStack> validateStrict(ItemStack itemStack) {
        DataResult<Unit> dataResult = ItemStack.validateComponents(itemStack.getComponents());
        if (dataResult.isError())
            return dataResult.map(unit -> itemStack);
        else
            return DataResult.success(itemStack);
    }
}
