package coolrex.bookofdragons.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import coolrex.bookofdragons.common.entities.misc.base.BaseDragonEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class GeoSaddleLayer<T extends BaseDragonEntity> extends GeoRenderLayer<T> {
    private final ResourceLocation textureLocation;

    public GeoSaddleLayer(GeoRenderer<T> render, ResourceLocation textureLocation) {
        super(render);
        this.textureLocation = textureLocation;
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType saddle = RenderType.entityCutoutNoCull(textureLocation);
        if (animatable.isSaddled()) {
            super.render(poseStack, animatable, bakedModel, saddle, bufferSource, buffer, partialTick, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}