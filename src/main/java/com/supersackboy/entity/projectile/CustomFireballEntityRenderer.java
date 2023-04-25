package com.supersackboy.entity.projectile;

import com.mojang.blaze3d.systems.RenderSystem;
import com.supersackboy.MagicMod;
import com.supersackboy.util.functions;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.DragonFireballEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.RotationCalculator;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CustomFireballEntityRenderer extends EntityRenderer<CustomFireballEntity> {
    private static final Identifier TEXTURE = new Identifier(MagicMod.ModID,"textures/entity/fireball.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE);
    private float deg = 0;
    private float scale = 1;
    private ModelPart cube;
    private boolean latch;
    private float top = 5;
    private float bottom = 0;
    private float location = 0;
    public CustomFireballEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("cube", ModelPartBuilder.create().uv(0, 0).cuboid(-8, -4, -8, 16, 16, 16), ModelTransform.NONE);

        cube = modelPartData.getChild("cube").createPart(64,32);
    }


    @Override
    public void render(CustomFireballEntity customFireballEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        //matrixStack.multiply(this.dispatcher.getRotation());
        //matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
        this.scale = 1f;
        deg++;

        if(latch) {
            location = (float) functions.lerp(location,top,0.05);
        } else {
            location = (float) functions.lerp(location,bottom,0.05);
        }
        if(location >= top-1) latch = false;
        if(location <= bottom+1) latch = true;

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
        matrixStack.push();
        matrixStack.translate(0,location,0);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(deg));

        cube.render(matrixStack,vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
        System.out.println(location);
        //renderCube(matrixStack, vertexConsumerProvider, i);

        super.render(customFireballEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private void renderCube(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        renderFace(matrixStack,vertexConsumerProvider,i,deg);
        renderFace(matrixStack,vertexConsumerProvider,i,deg+90);
        renderFace(matrixStack,vertexConsumerProvider,i,deg+180);
        renderFace(matrixStack,vertexConsumerProvider,i,deg+270);


        matrixStack.push();
        matrixStack.scale(scale, scale, scale);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-deg));
        matrixStack.translate(0,-scale/(scale*4),-scale*0.75/scale);
        renderFaceWithStack(matrixStack,vertexConsumerProvider,i);
        matrixStack.pop();

        matrixStack.push();
        matrixStack.scale(scale, scale, scale);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-deg));
        matrixStack.translate(0,-scale/(scale*4),scale/(4*scale));
        renderFaceWithStack(matrixStack,vertexConsumerProvider,i);
        matrixStack.pop();
    }

    private void renderFaceWithStack(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
        CustomFireballEntityRenderer.produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 0, 0, 1);
        CustomFireballEntityRenderer.produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 0, 1, 1);
        CustomFireballEntityRenderer.produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 1, 1, 0);
        CustomFireballEntityRenderer.produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 1, 0, 0);
    }
    private void renderFace(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, float deg) {
        matrixStack.push();
        matrixStack.scale(scale, scale, scale);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(deg));
        matrixStack.translate(0,0,scale/(scale*2));

        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);

        CustomFireballEntityRenderer.produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 0, 0, 1);
        CustomFireballEntityRenderer.produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 0, 1, 1);
        CustomFireballEntityRenderer.produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0f, 1, 1, 0);
        CustomFireballEntityRenderer.produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0f, 1, 0, 0);
        matrixStack.pop();
    }

    private static void produceVertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, int light, float x, int y, int textureU, int textureV) {

        vertexConsumer.vertex(positionMatrix, x - 0.5f, (float)y - 0.25f, 0.0f).color(255, 255, 255, 255).texture(textureU, textureV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0f, 1.0f, 0.0f).next();
    }
    @Override
    public Identifier getTexture(CustomFireballEntity entity) {
        return TEXTURE;
    }
}
