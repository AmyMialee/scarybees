package xyz.amymialee.scarybees.mixin;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.scarybees.ScaryBees;
import xyz.amymialee.scarybees.cca.BeeMaskComponent;

@Mixin(BeeEntity.class)
public class BeeEntityMixin {
    @Inject(method = "createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/passive/BeeEntity;", at = @At("RETURN"))
    private void scaryBees$spawn(ServerWorld serverWorld, PassiveEntity passiveEntity, @NotNull CallbackInfoReturnable<BeeEntity> cir) {
        ScaryBees.BEE_MASK.maybeGet(cir.getReturnValue()).ifPresent(BeeMaskComponent::initialize);
    }
}