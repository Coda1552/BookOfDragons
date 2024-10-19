package coolrex.bookofdragons.registry;

import coolrex.bookofdragons.BookOfDragons;
import coolrex.bookofdragons.common.entities.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BODEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BookOfDragons.MOD_ID);

    public static final RegistryObject<EntityType<Eel>> EEL = create("eel", EntityType.Builder.of(Eel::new, MobCategory.WATER_AMBIENT).sized(0.6f, 0.3f));
    public static final RegistryObject<EntityType<TerribleTerror>> TERRIBLE_TERROR = create("terrible_terror", EntityType.Builder.of(TerribleTerror::new, MobCategory.CREATURE).sized(1.0f, 0.75f));
    public static final RegistryObject<EntityType<Gronckle>> GRONCKLE = create("gronckle", EntityType.Builder.of(Gronckle::new, MobCategory.CREATURE).sized(2.0f, 2.0f));
    public static final RegistryObject<EntityType<DeadlyNadder>> DEADLY_NADDER = create("deadly_nadder", EntityType.Builder.of(DeadlyNadder::new, MobCategory.CREATURE).sized(2.0f, 2.3f));
    public static final RegistryObject<EntityType<NightFury>> NIGHT_FURY = create("night_fury", EntityType.Builder.of(NightFury::new, MobCategory.CREATURE).sized(2.0f, 2.0f));

    private static <T extends Entity> RegistryObject<EntityType<T>> create(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(BookOfDragons.MOD_ID + "." + name));
    }
}
