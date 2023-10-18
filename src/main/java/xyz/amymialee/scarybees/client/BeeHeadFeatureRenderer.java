package xyz.amymialee.scarybees.client;

import com.mojang.authlib.GameProfile;
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
import net.minecraft.entity.LimbAnimator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.scarybees.ScaryBees;
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

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, @NotNull BeeEntity bee, float f, float g, float h, float j, float k, float l) {
        var component = ScaryBees.BEE_MASK.get(bee);
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

                GameProfile gameProfile = null;
                var nbtCompound = mask.getNbt();
                if (nbtCompound != null) {
                    if (nbtCompound.contains("SkullOwner", 10)) {
                        gameProfile = NbtHelper.toGameProfile(nbtCompound.getCompound("SkullOwner"));
                    }
                }

                matrixStack.translate(-0.5, -0.025, -0.5);
                var skullType = skullBlock.getSkullType();
                var skullBlockEntityModel = (SkullBlockEntityModel) this.headModels.get(skullType);
                var renderLayer = SkullBlockEntityRenderer.getRenderLayer(skullType, gameProfile);
                var var22 = bee.getVehicle();
                LimbAnimator limbAnimator;
                if (var22 instanceof LivingEntity livingEntity2) {
                    limbAnimator = livingEntity2.limbAnimator;
                } else {
                    limbAnimator = bee.limbAnimator;
                }

                var o = limbAnimator.getPos(h);
                SkullBlockEntityRenderer.renderSkull(null, 180.0F, o, matrixStack, vertexConsumerProvider, i, skullBlockEntityModel, renderLayer);
            } else {
                translate(matrixStack);
                this.heldItemRenderer.renderItem(bee, mask, ModelTransformationMode.HEAD, false, matrixStack, vertexConsumerProvider, i);
            }
            matrixStack.pop();
        }
    }

    public static void translate(@NotNull MatrixStack matrices) {
        matrices.translate(0.0F, -0.25F, 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
        matrices.scale(0.625F, -0.625F, -0.625F);
    }
}