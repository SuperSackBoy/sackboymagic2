package com.supersackboy.entity.projectile;

import com.supersackboy.MagicMod;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

public class CustomFireballEntityRenderer extends EntityRenderer<CustomFireballEntity> {
    private static final Identifier TEXTURE = new Identifier(MagicMod.ModID,"textures/entity/fireball.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE);
    private float deg = 0;
    private final ModelPart cube;
    public CustomFireballEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("cube", ModelPartBuilder.create().uv(0, 0).cuboid(-8, -4, -8, 16, 16, 16), ModelTransform.NONE);
        cube = modelPartData.getChild("cube").createPart(64,32);
    }


    @Override
    public void render(CustomFireballEntity customFireballEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
        float scale = 1f;
        deg++;

        //Blender: W,X,Y,Z - Y,Z,X,W
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(deg));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(deg));

        matrixStack.push();
        matrixStack.translate(0,-0.039,0);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45));
        cube.render(matrixStack,vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.translate(0.18,0.035,0);
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45));
        cube.render(matrixStack,vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.translate(0,0.035,-0.18);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45));
        cube.render(matrixStack,vertexConsumer,i,OverlayTexture.DEFAULT_UV);
        matrixStack.pop();

        matrixStack.pop();
        super.render(customFireballEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
    @Override
    public Identifier getTexture(CustomFireballEntity entity) {
        return TEXTURE;
    }
}
