package coolrex.bookofdragons.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import coolrex.bookofdragons.BookOfDragons;
import coolrex.bookofdragons.common.entities.misc.base.BaseDragonEntity;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class PassengerLayer<T extends BaseDragonEntity> extends GeoRenderLayer<T> {

    public PassengerLayer(GeoRenderer<T> render) {
        super(render);
    }

    @Override
    public void renderForBone(PoseStack poseStack, T entity, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer pBuffer, float partialTicks, int pPackedLight, int packedOverlay) {
        if (bone.getName().equals("saddle") && entity.isVehicle()) {
            for (Entity passenger : entity.getPassengers()) {
                if (passenger == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                    continue;
                }
                float bodyYaw = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks;

                BookOfDragons.PROXY.releaseRenderingEntity(passenger.getUUID());

                poseStack.pushPose();

                poseStack.translate(0.0D, passenger.getBbHeight() - 0.65F, 0.65F);
                poseStack.mulPose(Axis.YP.rotationDegrees(180F));
                poseStack.mulPose(Axis.YN.rotationDegrees(360F - bodyYaw));
                poseStack.translate(bone.getRotX(), bone.getRotY(), bone.getRotZ());

                renderPassenger(passenger, 0, partialTicks, poseStack, bufferSource, pPackedLight);
                poseStack.popPose();

                BookOfDragons.PROXY.blockRenderingEntity(passenger.getUUID());
            }
        }
    }

    public static <E extends Entity> void renderPassenger(E entityIn, float yaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLight) {
        EntityRenderer<? super E> render = null;
        EntityRenderDispatcher manager = Minecraft.getInstance().getEntityRenderDispatcher();
        try {
            render = manager.getRenderer(entityIn);

            if (render != null) {
                try {
                    render.render(entityIn, yaw, partialTicks, matrixStack, bufferIn, packedLight);
                } catch (Throwable throwable1) {
                    throw new ReportedException(CrashReport.forThrowable(throwable1, "Rendering entity in world"));
                }
            }
        } catch (Throwable throwable3) {
            CrashReport crashreport = CrashReport.forThrowable(throwable3, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Entity being rendered");
            entityIn.fillCrashReportCategory(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.addCategory("Renderer details");
            crashreportcategory1.setDetail("Assigned renderer", render);
            crashreportcategory1.setDetail("Rotation", Float.valueOf(yaw));
            crashreportcategory1.setDetail("Delta", Float.valueOf(partialTicks));
            throw new ReportedException(crashreport);
        }
    }

}