package juloos.unstackableslots.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
    @Shadow @Final public NonNullList<Slot> slots;

    /**
     * @author Juloos
     * @reason Remove the break statement that breaks multistacks quick crafting
     */
    @Overwrite
    public boolean moveItemStackTo(ItemStack itemStack, int i, int j, boolean bl) {
        boolean bl2 = false;
        int k = i;
        if (bl) {
            k = j - 1;
        }

        if (itemStack.isStackable()) {
            while (!itemStack.isEmpty() && (bl ? k >= i : k < j)) {
                Slot slot = this.slots.get(k);
                ItemStack itemStack2 = slot.getItem();
                if (!itemStack2.isEmpty() && ItemStack.isSameItemSameComponents(itemStack, itemStack2)) {
                    int l = itemStack2.getCount() + itemStack.getCount();
                    int m = slot.getMaxStackSize(itemStack2);
                    if (l <= m) {
                        itemStack.setCount(0);
                        itemStack2.setCount(l);
                        slot.setChanged();
                        bl2 = true;
                    } else if (itemStack2.getCount() < m) {
                        itemStack.shrink(m - itemStack2.getCount());
                        itemStack2.setCount(m);
                        slot.setChanged();
                        bl2 = true;
                    }
                }

                if (bl) {
                    k--;
                } else {
                    k++;
                }
            }
        }

        if (!itemStack.isEmpty()) {
            if (bl) {
                k = j - 1;
            } else {
                k = i;
            }

            while (bl ? k >= i : k < j) {
                Slot slotx = this.slots.get(k);
                ItemStack itemStack2x = slotx.getItem();
                if (itemStack2x.isEmpty() && slotx.mayPlace(itemStack)) {
                    int l = slotx.getMaxStackSize(itemStack);
                    slotx.setByPlayer(itemStack.split(Math.min(itemStack.getCount(), l)));
                    slotx.setChanged();
                    bl2 = true;
                }

                if (bl) {
                    k--;
                } else {
                    k++;
                }
            }
        }

        return bl2;
    }
}
