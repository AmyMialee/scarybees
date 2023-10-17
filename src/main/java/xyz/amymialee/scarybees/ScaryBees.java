package xyz.amymialee.scarybees;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.scarybees.cca.BeeMaskComponent;

public class ScaryBees implements ModInitializer, EntityComponentInitializer {
    public static String MODID = "scarybees";
    public static final ComponentKey<BeeMaskComponent> BEE_MASK = ComponentRegistry.getOrCreate(id("mask"), BeeMaskComponent.class);

    @Override
    public void onInitialize() {}
    // bees sometimes spawn with masks
    // trade bees a flower or something to get the mask off of them (no drop on death)
    // also trade a mask to swap them

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(BeeEntity.class, BEE_MASK).respawnStrategy(RespawnCopyStrategy.INVENTORY).end(entity -> new BeeMaskComponent());
    }

    public static @NotNull Identifier id(String name) {
        return new Identifier(MODID, name);
    }
}