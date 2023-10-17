package xyz.amymialee.scarybees.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class BeeMaskComponent implements AutoSyncedComponent {
    private ItemStack mask = ItemStack.EMPTY;

    public ItemStack getMask() {
        return this.mask;
    }

    public void setMask(ItemStack mask) {
        this.mask = mask;
        if (this.mask == null) {
            this.mask = ItemStack.EMPTY;
        }
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        if (tag.contains("Mask")) {
            this.mask = ItemStack.fromNbt(tag.getCompound("Mask"));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (this.mask != null) {
            tag.put("Mask", this.mask.writeNbt(new NbtCompound()));
        }
    }
}