package coolrex.bookofdragons.common.entities;

import coolrex.bookofdragons.common.entities.misc.BODAnimations;
import coolrex.bookofdragons.common.entities.misc.base.BaseDragonEntity;
import coolrex.bookofdragons.registry.BODEntities;
import coolrex.bookofdragons.registry.BODItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.Comparator;

public class NightFury extends BaseDragonEntity implements GeoEntity {
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(NightFury.class, EntityDataSerializers.INT);

    public NightFury(EntityType<? extends NightFury> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 35.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.FLYING_SPEED, 0.5F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob ageableMob) {
        return BODEntities.NIGHT_FURY.get().create(world);
    }

    @Override
    protected float getStandingEyeHeight(Pose p_21131_, EntityDimensions p_21132_) {
        return 1.2F;
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction pCallback) {
        Vec3 pos = getYawVec(yBodyRot, 0.0F, -0.35F).add(getX(), getY() + 1.25F, getZ());
        passenger.setPos(pos.x, pos.y, pos.z);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getVariant().getId());
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(DATA_VARIANT, tag.getInt("Variant"));
    }

    public NightFury.Variant getVariant() {
        return NightFury.Variant.BY_ID[this.entityData.get(DATA_VARIANT)];
    }

    private void setVariant(NightFury.Variant p_149118_) {
        this.entityData.set(DATA_VARIANT, p_149118_.getId());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoEntity>(this, "controller", 2, this::predicate));
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private <E extends GeoEntity> PlayState predicate(software.bernie.geckolib.core.animation.AnimationState<E> event) {
        boolean flyingFlag = !onGround() && (isVehicle() || (isFlying() || isLanding()));

        if (event.isMoving()) {
            if (flyingFlag) {
                event.setAnimation(BODAnimations.FLY);
            }
            else {
                event.setAnimation(BODAnimations.WALK);
            }
        }
        else {
            if (flyingFlag) {
                event.setAnimation(BODAnimations.FLY_IDLE);
            }
            else {
                event.setAnimation(BODAnimations.IDLE);
            }
        }
        return PlayState.CONTINUE;
    }

    public enum Variant {
        HERO(0),
        BLUE(1),
        PURPLE(2);

        public static final NightFury.Variant[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(NightFury.Variant::getId)).toArray(Variant[]::new);

        private final int id;

        public int getId() {
            return this.id;
        }

        private Variant(int p_149239_) {
            this.id = p_149239_;
        }
    }
}
