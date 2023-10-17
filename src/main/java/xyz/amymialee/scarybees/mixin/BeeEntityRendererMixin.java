package xyz.amymialee.scarybees.mixin;

import net.minecraft.client.render.entity.BeeEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.BeeEntityModel;
import net.minecraft.entity.passive.BeeEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.scarybees.client.BeeHeadFeatureRenderer;

@Mixin(BeeEntityRenderer.class)
public abstract class BeeEntityRendererMixin extends MobEntityRenderer<BeeEntity, BeeEntityModel<BeeEntity>> {
    public BeeEntityRendererMixin(EntityRendererFactory.Context context, BeeEntityModel<BeeEntity> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void scaryBees$maskRenderer(@NotNull EntityRendererFactory.Context context, CallbackInfo ci) {
        this.addFeature(new BeeHeadFeatureRenderer(this, context.getModelLoader(), context.getHeldItemRenderer()));
    }
}