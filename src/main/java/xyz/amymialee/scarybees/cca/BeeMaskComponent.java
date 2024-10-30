package xyz.amymialee.scarybees.cca;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import xyz.amymialee.scarybees.ScaryBees;

public class BeeMaskComponent implements AutoSyncedComponent {
    public static final ComponentKey<BeeMaskComponent> KEY = ComponentRegistry.getOrCreate(ScaryBees.id("mask"), BeeMaskComponent.class);
    private final BeeEntity bee;
    private @NotNull ItemStack mask = ItemStack.EMPTY;

    public BeeMaskComponent(BeeEntity bee) {
        this.bee = bee;
    }

    public void initialize() {
        if (!(this.bee.getRandom().nextFloat() > 0.8f)) return;
        var entryList = Registries.ITEM.getEntryList(ScaryBees.SPAWNABLE_BEE_MASKS);
        if (entryList.isEmpty()) return;
        var list = entryList.get();
        if (list.size() <= 0) return;
        this.setMask(list.get(this.bee.getRandom().nextInt(list.size())).value().getDefaultStack());
    }

    public @NotNull ItemStack getMask() {
        return this.mask;
    }

    public void setMask(ItemStack mask) {
        this.mask = mask;
        if (this.mask == null) this.mask = ItemStack.EMPTY;
        KEY.sync(this.bee);
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (!this.mask.isEmpty()) tag.put("Mask", this.mask.encode(registryLookup));
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (tag.contains("Mask")) this.mask = ItemStack.fromNbtOrEmpty(registryLookup, tag.getCompound("Mask"));
    }
}