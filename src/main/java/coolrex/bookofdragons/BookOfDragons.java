package coolrex.bookofdragons;

import coolrex.bookofdragons.client.ClientProxy;
import coolrex.bookofdragons.common.CommonProxy;
import coolrex.bookofdragons.common.entities.*;
import coolrex.bookofdragons.registry.BODBlocks;
import coolrex.bookofdragons.registry.BODEntities;
import coolrex.bookofdragons.registry.BODItems;
import coolrex.bookofdragons.registry.BODTabs;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.GeckoLib;

@Mod(BookOfDragons.MOD_ID)
public class BookOfDragons {
    public static final String MOD_ID = "bookofdragons";
    public static final Logger LOGGER = LogManager.getLogger();
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new); // Proxy code from Alex's Caves

    public BookOfDragons() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BODItems.ITEMS.register(bus);
        BODEntities.ENTITIES.register(bus);
        BODBlocks.BLOCKS.register(bus);
        BODTabs.CREATIVE_TABS.register(bus);

        bus.addListener(this::registerAttributes);

        PROXY.commonInit();
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(BODEntities.EEL.get(), Eel.createAttributes().build());
        event.put(BODEntities.TERRIBLE_TERROR.get(), TerribleTerror.createAttributes().build());
        event.put(BODEntities.GRONCKLE.get(), Gronckle.createAttributes().build());
        event.put(BODEntities.DEADLY_NADDER.get(), DeadlyNadder.createAttributes().build());
        event.put(BODEntities.NIGHT_FURY.get(), NightFury.createAttributes().build());
    }


    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> PROXY.clientInit());
    }
}
