package coolrex.bookofdragons.client;

import coolrex.bookofdragons.BookOfDragons;
import coolrex.bookofdragons.client.geo.GenericGeoModel;
import coolrex.bookofdragons.client.geo.GenericGeoRenderer;
import coolrex.bookofdragons.registry.BODEntities;
import coolrex.bookofdragons.registry.BODKeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BookOfDragons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    private static void make(EntityType type, String name){
        EntityRenderers.register(type, (ctx) -> new GenericGeoRenderer<>(ctx, () -> new GenericGeoModel<>(name)));
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers e) {
        EntityType<?>[] simpleEntities = new EntityType[]{
                BODEntities.EEL.get(), BODEntities.DEADLY_NADDER.get(), BODEntities.GRONCKLE.get(), BODEntities.TERRIBLE_TERROR.get(),
                BODEntities.NIGHT_FURY.get()
        };
        for (EntityType<?> type : simpleEntities) {
            make(type, type.getDescriptionId().substring("entity.bookofdragons.".length()));
        }
    }

    public static Minecraft getClient() {
        return Minecraft.getInstance();
    }

    public static double getFlightDelta() {
        return getClient().options.keyJump.isDown() ? 0.4 : BODKeyBindings.DRAGON_DESCEND.isDown() ? -0.5 : 0;
    }
}