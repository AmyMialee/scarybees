package xyz.amymialee.scarybees.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.amymialee.scarybees.ScaryBees;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {
    public CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @WrapOperation(method = "renderTabIcon", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemGroup;getIcon()Lnet/minecraft/item/ItemStack;"))
    private ItemStack scaryBees$renderTabIcons(ItemGroup instance, @NotNull Operation<ItemStack> original) {
        if (instance != ScaryBees.BEE_GROUP) return original.call(instance);
        var array = instance.getDisplayStacks().toArray(new ItemStack[0]);
        if (array.length > 0) return array[(int) (System.currentTimeMillis() / 1000) % array.length];
        return original.call(instance);
    }
}