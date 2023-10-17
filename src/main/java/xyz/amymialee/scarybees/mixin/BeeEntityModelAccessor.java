package xyz.amymialee.scarybees.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BeeEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BeeEntityModel.class)
public interface BeeEntityModelAccessor {
    @Accessor("bone")
    ModelPart getBone();
}