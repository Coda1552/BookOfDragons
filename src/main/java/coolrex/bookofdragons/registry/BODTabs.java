package coolrex.bookofdragons.registry;

import coolrex.bookofdragons.BookOfDragons;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class BODTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BookOfDragons.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BOD_TAB = CREATIVE_TABS.register("bod_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + BookOfDragons.MOD_ID))
                    .icon(BODItems.EEL.get()::getDefaultInstance)
                    .displayItems((displayParams, output) -> {
                        for (var item : BODItems.ITEMS.getEntries()) {
                            output.accept(item.get());
                        }
                    })
                    .build()
    );
}
