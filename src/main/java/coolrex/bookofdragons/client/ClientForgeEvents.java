package coolrex.bookofdragons.client;

import coolrex.bookofdragons.BookOfDragons;
import coolrex.bookofdragons.common.entities.misc.base.BaseDragonEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BookOfDragons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {

    @SubscribeEvent
    public static void cameraOverlay(ViewportEvent.ComputeCameraAngles e) {
        Minecraft mc = Minecraft.getInstance();
        var player = mc.player;
        float factor;

        if (player != null && player.isPassenger() && player.getVehicle() instanceof BaseDragonEntity hippogryph) {
            Camera camera = e.getCamera();

            if (mc.options.getCameraType().isFirstPerson()) {
                factor = 0.25F;

                camera.move(0.0D, 0.0D, (Mth.lerp(e.getPartialTick(), hippogryph.prevTilt, hippogryph.tilt)) * 0.025D);
            }
            else {
                factor = 0.1F;

                camera.move(-0.75D, 0.0D, 0.0D);
            }

            float pitch = e.getPitch() + (float) (Mth.lerp(e.getPartialTick(), hippogryph.xRotO, hippogryph.xRot) * factor);
            float roll = e.getRoll() - (float) (Mth.lerp(e.getPartialTick(), hippogryph.prevTilt, hippogryph.tilt));

            e.setPitch(pitch);
            e.setRoll(roll);
        }
    }

    @SubscribeEvent
    public static void preRenderLiving(RenderLivingEvent.Pre<Player, HumanoidModel<Player>> event) {
        if (ClientProxy.blockedEntityRenders.contains(event.getEntity().getUUID())) {
            if (!isFirstPersonPlayer(event.getEntity())) {
                MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post<>(event.getEntity(), event.getRenderer(), event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight()));
                event.setCanceled(true);
            }
            ClientProxy.blockedEntityRenders.remove(event.getEntity().getUUID());
        }
    }

    public static boolean isFirstPersonPlayer(LivingEntity entity) {
        return entity.equals(Minecraft.getInstance().cameraEntity) && Minecraft.getInstance().options.getCameraType().isFirstPerson();
    }

}
