package xyz.amymialee.scarybees.client;

import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BeeEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.scarybees.cca.BeeMaskComponent;
import xyz.amymialee.scarybees.mixin.BeeEntityModelAccessor;

import java.util.Map;

public class BeeHeadFeatureRenderer extends FeatureRenderer<BeeEntity, BeeEntityModel<BeeEntity>> {
    private final float scaleX;
    private final float scaleY;
    private final float scaleZ;
    private final Map<SkullBlock.SkullType, SkullBlockEntityModel> headModels;
    private final HeldItemRenderer heldItemRenderer;

    public BeeHeadFeatureRenderer(FeatureRendererContext<BeeEntity, BeeEntityModel<BeeEntity>> context, EntityModelLoader loader, HeldItemRenderer heldItemRenderer) {
        this(context, loader, 1.0F, 1.0F, 1.0F, heldItemRenderer);
    }

    public BeeHeadFeatureRenderer(FeatureRendererContext<BeeEntity, BeeEntityModel<BeeEntity>> context, EntityModelLoader loader, float scaleX, float scaleY, float scaleZ, HeldItemRenderer heldItemRenderer) {
        super(context);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.headModels = SkullBlockEntityRenderer.getModels(loader);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, @NotNull BeeEntity bee, float f, float g, float h, float j, float k, float l) {
        var component = BeeMaskComponent.KEY.get(bee);
        var mask = component.getMask();
        if (!mask.isEmpty()) {
            var item = mask.getItem();
            matrixStack.push();
            matrixStack.scale(this.scaleX, this.scaleY, this.scaleZ);
            if (bee.isBaby()) {
                matrixStack.translate(0.0F, 0.7525F, 0.0F);
                matrixStack.scale(0.5F, 0.5F, 0.5F);
            }
            ((BeeEntityModelAccessor) this.getContextModel()).getBone().rotate(matrixStack);
            matrixStack.scale(0.8f, 0.8f, 0.8f);
            matrixStack.translate(0f, 0.2125f, -0.125f);
            if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractSkullBlock skullBlock) {
                matrixStack.scale(1.16F, -1.16F, -1.16F);
                matrixStack.translate(-0.5, -0.025, -0.5);
                var limbAnimator = bee.getVehicle() instanceof LivingEntity entity ? entity.limbAnimator : bee.limbAnimator;
                var skullType = skullBlock.getSkullType();
                SkullBlockEntityRenderer.renderSkull(null, 180.0F, limbAnimator.getPos(h), matrixStack, vertexConsumerProvider, i, this.headModels.get(skullType), SkullBlockEntityRenderer.getRenderLayer(skullType, mask.get(DataComponentTypes.PROFILE)));
            } else {
                matrixStack.translate(0.0F, -0.25F, 0.0F);
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
                matrixStack.scale(0.625F, -0.625F, -0.625F);
                this.heldItemRenderer.renderItem(bee, mask, ModelTransformationMode.HEAD, false, matrixStack, vertexConsumerProvider, i);
            }
            matrixStack.pop();
        }
    }
}