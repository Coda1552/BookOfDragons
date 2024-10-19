package coolrex.bookofdragons.registry;

import coolrex.bookofdragons.BookOfDragons;
import coolrex.bookofdragons.common.items.BODItemTier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BODItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BookOfDragons.MOD_ID);

    public static final RegistryObject<Item> RAW_GRONCKLE_IRON = ITEMS.register("raw_gronckle_iron", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GRONCKLE_IRON_INGOT = ITEMS.register("gronckle_iron_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GRONCKLE_IRON_NUGGET_ = ITEMS.register("gronckle_iron_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GRONCKLE_IRON_SWORD = ITEMS.register("gronckle_iron_sword", () -> new SwordItem(BODItemTier.GRONKLE_IRON, 3, -2.1F, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GRONCKLE_IRON_PICKAXE = ITEMS.register("gronckle_iron_pickaxe", () -> new PickaxeItem(BODItemTier.GRONKLE_IRON, 1, -2.5F, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GRONCKLE_IRON_AXE = ITEMS.register("gronckle_iron_axe", () -> new AxeItem(BODItemTier.GRONKLE_IRON, 5, -2.85F, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GRONCKLE_IRON_SHOVEL = ITEMS.register("gronckle_iron_shovel", () -> new ShovelItem(BODItemTier.GRONKLE_IRON, 1.5F, -2.8F, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> GRONCKLE_IRON_HOE = ITEMS.register("gronckle_iron_hoe", () -> new HoeItem(BODItemTier.GRONKLE_IRON, 3, -0.0F, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> EEL = ITEMS.register("eel", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.1F).effect(() -> new MobEffectInstance(MobEffects.POISON, 200, 0), 0.5F).build())));
    public static final RegistryObject<Item> EEL_BUCKET = ITEMS.register("eel_bucket", () -> new MobBucketItem(BODEntities.EEL, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> EEL_SPAWN_EGG = ITEMS.register("eel_spawn_egg", () -> new ForgeSpawnEggItem(BODEntities.EEL, 0x222123, 0xc8bc15, new Item.Properties()));
    public static final RegistryObject<Item> TERRIBLE_TERROR_SPAWN_EGG = ITEMS.register("terrible_terror_spawn_egg", () -> new ForgeSpawnEggItem(BODEntities.TERRIBLE_TERROR, 0x6f7930, 0x843917, new Item.Properties()));
    public static final RegistryObject<Item> GRONCKLE_SPAWN_EGG = ITEMS.register("gronckle_spawn_egg", () -> new ForgeSpawnEggItem(BODEntities.GRONCKLE, 0x9e5b40, 0xb99575, new Item.Properties()));
    public static final RegistryObject<Item> DEADLY_NADDER_SPAWN_EGG = ITEMS.register("deadly_nadder_spawn_egg", () -> new ForgeSpawnEggItem(BODEntities.DEADLY_NADDER, 0x429ab2, 0xe3a923, new Item.Properties()));
    public static final RegistryObject<Item> NIGHT_FURY_SPAWN_EGG = ITEMS.register("night_fury_spawn_egg", () -> new ForgeSpawnEggItem(BODEntities.NIGHT_FURY, 0x0a0326, 0x29b34b, new Item.Properties()));
}
