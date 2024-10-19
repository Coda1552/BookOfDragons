package coolrex.bookofdragons.client.geo;

import coolrex.bookofdragons.BookOfDragons;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.model.GeoModel;

public class GenericGeoModel<E extends LivingEntity & GeoEntity> extends GeoModel<E> {
    private final String model;
    private final String texture;
    private final String anim;

    public GenericGeoModel(String name) {
        this(name, name, name);
    }

    public GenericGeoModel(String model, String texture, String anim) {
        this.model = model;
        this.texture = texture;
        this.anim = anim;
    }

    @Override
    public ResourceLocation getModelResource(E object) {
        return new ResourceLocation(BookOfDragons.MOD_ID, "geo/entity/" + model + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(E object) {
        return new ResourceLocation(BookOfDragons.MOD_ID, "textures/entity/" + texture + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(E object) {
        return new ResourceLocation(BookOfDragons.MOD_ID, "animations/entity/" + anim + ".animation.json");
    }
}