package coolrex.bookofdragons.common.entities;

import coolrex.bookofdragons.common.entities.misc.BODAnimations;
import coolrex.bookofdragons.common.entities.misc.base.BaseDragonEntity;
import coolrex.bookofdragons.common.entities.misc.goals.TerrorIntimidateGoal;
import coolrex.bookofdragons.registry.BODEntities;
import coolrex.bookofdragons.registry.BODItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class TerribleTerror extends BaseDragonEntity implements GeoEntity {
    private static final EntityDataAccessor<Boolean> SNAPPING = SynchedEntityData.defineId(TerribleTerror.class, EntityDataSerializers.BOOLEAN);
    private int snapTimer;

    public TerribleTerror(EntityType<? extends BaseDragonEntity> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 12.0D).add(Attributes.MOVEMENT_SPEED, 0.25F).add(Attributes.FLYING_SPEED, 0.8F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SNAPPING, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new TerrorIntimidateGoal(this));
    }

    public void setSnapping(boolean snapping){
        this.entityData.set(SNAPPING, snapping);
    }

    public boolean getSnapping(){
        return this.entityData.get(SNAPPING);
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 lookVec = this.getViewVector(1.0f);
        if (this.getSnapping()) {
            if (this.snapTimer < 20) {
                this.snapTimer++;
            }
            if (this.snapTimer > 8 && this.snapTimer < 12) {
                for(int i = 0; i < 4; i++) {
                    this.level().addParticle(ParticleTypes.FLAME, this.getX() + lookVec.x() * 0.9, this.getY() + 0.35f, this.getZ() +  lookVec.z() * 0.9, lookVec.x()/4 , lookVec.y() / 2, lookVec.z()/4);
                }
            }
        }
        else {
           this.snapTimer = 0;
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob ageableMob) {
        return BODEntities.TERRIBLE_TERROR.get().create(world);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoEntity>(this, "controller", 2, this::predicate));
        controllerRegistrar.add(new AnimationController<GeoEntity>(this, "controllerSnap", 2, this::predicateSnap));
    }

    private <E extends GeoEntity> PlayState predicate(AnimationState<E> event) {
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

    private <E extends GeoEntity> PlayState predicateSnap(AnimationState<E> event) {
        if (getSnapping()) {
            event.setAnimation(BODAnimations.SPECIAL_ATTACK);
            return PlayState.CONTINUE;
        }
        else {
            return PlayState.STOP;
        }
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
