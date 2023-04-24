package com.supersackboy.entity.projectile;

import com.mojang.blaze3d.systems.RenderSystem;
import com.supersackboy.MagicMod;
import net.minecraft.client.model.TexturedModelData;
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

public class CustomFireballEntityRenderer extends EntityRenderer<CustomFireballEntity> {
    private static final Identifier TEXTURE = new Identifier("textures/block/andesite.png");
    private static final RenderLayer LAYER = RenderLayer.getEntityCutoutNoCull(TEXTURE);
    private float deg = 0;
    private float scale = 1;
    public CustomFireballEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(CustomFireballEntity customFireballEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        //matrixStack.multiply(this.dispatcher.getRotation());
        //matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
        this.scale = 1f;
        deg++;


        renderCube(matrixStack, vertexConsumerProvider, i);


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
