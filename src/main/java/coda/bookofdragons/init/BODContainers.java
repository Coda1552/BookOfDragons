package coda.bookofdragons.init;

import coda.bookofdragons.BookOfDragons;
import coda.bookofdragons.common.menu.DragonInventoryMenu;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BODContainers {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, BookOfDragons.MOD_ID);

    public static final RegistryObject<MenuType<DragonInventoryMenu>> DRAGON_INV = CONTAINERS.register("dragon_inv", () -> IForgeContainerType.create((windowId, inv, data) -> {
        int id = data.readInt();
        // TODO - fix the npe here
        return new DragonInventoryMenu(windowId, inv, null, id);
    }));
}