package xyz.amymialee.scarybees.mixin;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.scarybees.ScaryBees;
import xyz.amymialee.scarybees.cca.BeeMaskComponent;

@Mixin(PassiveEntity.class)
public class PassiveEntityMixin {
    @Inject(method = "initialize", at = @At("HEAD"))
    private void scaryBees$spawn(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        ScaryBees.BEE_MASK.maybeGet(this).ifPresent(BeeMaskComponent::initialize);
    }
}