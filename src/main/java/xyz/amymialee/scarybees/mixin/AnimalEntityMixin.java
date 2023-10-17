package xyz.amymialee.scarybees.mixin;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.scarybees.ScaryBees;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin {
    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void scaryBees$trade(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        var entity = (AnimalEntity) (Object) this;
        if (entity instanceof BeeEntity bee) {
            var component = ScaryBees.BEE_MASK.get(bee);
            var itemStack = player.getStackInHand(hand);
            var mask = component.getMask();
            component.setMask(itemStack);
            player.setStackInHand(hand, mask);
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}