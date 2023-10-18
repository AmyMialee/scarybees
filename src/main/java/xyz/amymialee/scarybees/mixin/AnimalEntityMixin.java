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

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin {
    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void scaryBees$trade(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (hand != Hand.MAIN_HAND) return;
        var entity = (AnimalEntity) (Object) this;
        if (entity instanceof BeeEntity bee) {
            var component = ScaryBees.BEE_MASK.get(bee);
            var itemStack = player.getStackInHand(hand);
            var mask = component.getMask();
            var buy = itemStack.isOf(Items.HONEY_BOTTLE) && !mask.isEmpty();
            var trade = false;
            if (!buy) {
                if (itemStack.isIn(ScaryBees.BEE_MASKS)) {
                    trade = true;
                }
            }
            if (trade || buy) {
                var itemEntity = new ItemEntity(bee.getWorld(), bee.getX(), bee.getEyeY(), bee.getZ(), mask);
                itemEntity.setPickupDelay(0);
                var xDif = player.getX() - bee.getX();
                var yDif = player.getBodyY(0.5) - bee.getBodyY(0.5);
                var zDif = player.getZ() - bee.getZ();
                var dist = Math.sqrt(Math.sqrt(bee.squaredDistanceTo(player))) * 0.5;
                var vec3d = new Vec3d(xDif, yDif, zDif).normalize().multiply(dist);
                itemEntity.setVelocity(vec3d);
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
    }
}