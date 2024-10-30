package xyz.amymialee.scarybees.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.scarybees.ScaryBees;
import xyz.amymialee.scarybees.cca.BeeMaskComponent;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin {
    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void scarybees$trade(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (hand != Hand.MAIN_HAND) return;
        var self = (AnimalEntity) (Object) this;
        if (!(self instanceof BeeEntity bee)) return;
        var component = BeeMaskComponent.KEY.get(bee);
        var itemStack = player.getStackInHand(hand);
        var mask = component.getMask();
        var buy = itemStack.isOf(Items.HONEY_BOTTLE) && !mask.isEmpty();
        var trade = !buy && itemStack.isIn(ScaryBees.BEE_MASKS);
        if (!trade && !buy) return;
        var itemEntity = new ItemEntity(bee.getWorld(), bee.getX(), bee.getEyeY(), bee.getZ(), mask);
        itemEntity.setPickupDelay(0);
        itemEntity.setVelocity(new Vec3d(
                player.getX() - bee.getX(),
                player.getBodyY(0.5) - bee.getBodyY(0.5),
                player.getZ() - bee.getZ()
        ).normalize().multiply(Math.sqrt(Math.sqrt(bee.squaredDistanceTo(player))) * 0.5));
        bee.getWorld().spawnEntity(itemEntity);
        if (buy) {
            component.setMask(ItemStack.EMPTY);
        } else {
            component.setMask(itemStack.copyWithCount(1));
        }
        itemStack.decrement(1);
        cir.setReturnValue(ActionResult.SUCCESS);
    }
}