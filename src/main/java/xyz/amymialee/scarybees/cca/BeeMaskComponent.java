package xyz.amymialee.scarybees.cca;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.scarybees.ScaryBees;

public class BeeMaskComponent implements AutoSyncedComponent {
    private final BeeEntity bee;
    private @NotNull ItemStack mask = ItemStack.EMPTY;

    public BeeMaskComponent(BeeEntity bee) {
        this.bee = bee;
    }

    public @NotNull ItemStack getMask() {
        return this.mask;
    }

    public void setMask(ItemStack mask) {
        this.mask = mask;
        if (this.mask == null) {
            this.mask = ItemStack.EMPTY;
        }
        ScaryBees.BEE_MASK.sync(this.bee);
    }

    public void initialize() {
        if (this.bee.getRandom().nextFloat() > 0.6f) {
            var entryList = Registries.ITEM.getEntryList(ScaryBees.SPAWNABLE_BEE_MASKS);
            if (entryList.isPresent()) {
                var list = entryList.get();
                if (list.size() > 0) {
                    this.setMask(list.get(this.bee.getRandom().nextInt(list.size())).value().getDefaultStack());
                }
            }
        }
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        if (tag.contains("Mask")) {
            this.mask = ItemStack.fromNbt(tag.getCompound("Mask"));
        }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        tag.put("Mask", this.mask.writeNbt(new NbtCompound()));
    }
}