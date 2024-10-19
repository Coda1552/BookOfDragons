package coolrex.bookofdragons.common.entities;

import coolrex.bookofdragons.common.entities.misc.BODAnimations;
import coolrex.bookofdragons.common.entities.misc.base.BaseDragonEntity;
import coolrex.bookofdragons.registry.BODEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;

// TODO for the nadder - fix blinking, fix which animations play and when, figure out why its lowering tps, and probably more idfk lol
public class DeadlyNadder extends BaseDragonEntity implements GeoEntity {
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(DeadlyNadder.class, EntityDataSerializers.INT);
    public Vec3 targetPosition;
    public BlockPos circlingCenter = BlockPos.ZERO;

    public DeadlyNadder(EntityType<? extends DeadlyNadder> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes().add(Attributes.MAX_HEALTH, 40.0D).add(Attributes.MOVEMENT_SPEED, 0.285F).add(Attributes.ARMOR, 5.0F).add(Attributes.ATTACK_DAMAGE, 7.0F).add(Attributes.ATTACK_KNOCKBACK, 7.0F).add(Attributes.FOLLOW_RANGE, 128.0F).add(Attributes.FLYING_SPEED, 0.8F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new DeadlyNadder.StartAttackGoal());
        this.goalSelector.addGoal(4, new DeadlyNadder.SwoopMovementGoal());
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob ageableMob) {
        return BODEntities.DEADLY_NADDER.get().create(world);
    }

    @Override
    protected float getStandingEyeHeight(Pose p_21131_, EntityDimensions p_21132_) {
        return isBaby() ? 1.0F : 1.5F;
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction pCallback) {
        Vec3 pos = getYawVec(yBodyRot, 0.0F, -0.35F).add(getX(), getY() + 1.5F, getZ());
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

    public DeadlyNadder.Variant getVariant() {
        return DeadlyNadder.Variant.BY_ID[this.entityData.get(DATA_VARIANT)];
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<GeoEntity>(this, "controller", 2, this::predicate));
        controllerRegistrar.add(new AnimationController<GeoEntity>(this, "controllerSnap", 2, this::predicateSnap));
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

    private <E extends GeoEntity> PlayState predicateSnap(AnimationState<E> event) {
        if (isAggressive()) {
            event.setAnimation(BODAnimations.BITE);
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

    class StartAttackGoal extends Goal {

        private StartAttackGoal() {
        }

        public boolean canUse() {
            return !DeadlyNadder.this.isInWater() && DeadlyNadder.this.getTarget() != null && DeadlyNadder.this.canAttack(DeadlyNadder.this.getTarget(), TargetingConditions.DEFAULT);
        }

        public void start() {
            this.startSwoop();
            DeadlyNadder.this.push(0, 1, 0);
            DeadlyNadder.this.setFlying(true);
        }

        public void stop() {
            DeadlyNadder.this.circlingCenter = DeadlyNadder.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, DeadlyNadder.this.circlingCenter).above(10 + DeadlyNadder.this.random.nextInt(20));
            DeadlyNadder.this.setFlying(false);
        }

        private void startSwoop() {
            DeadlyNadder.this.circlingCenter = DeadlyNadder.this.getTarget().blockPosition().above(20 + DeadlyNadder.this.random.nextInt(20));
            if (DeadlyNadder.this.circlingCenter.getY() < DeadlyNadder.this.level().getSeaLevel()) {
                DeadlyNadder.this.circlingCenter = new BlockPos(DeadlyNadder.this.circlingCenter.getX(), DeadlyNadder.this.level().getSeaLevel() + 1, DeadlyNadder.this.circlingCenter.getZ());
            }
        }
    }

    class SwoopMovementGoal extends Goal {
        private SwoopMovementGoal() {
            super();
        }

        public boolean canUse() {
            return DeadlyNadder.this.isFlying() && DeadlyNadder.this.canFly() && DeadlyNadder.this.getTarget() != null;
        }

        public boolean canContinueToUse() {
            LivingEntity livingEntity = DeadlyNadder.this.getTarget();
            if (livingEntity == null) {
                return false;
            } else if (!livingEntity.isAlive()) {
                return false;
            } else if (livingEntity instanceof Player && (livingEntity.isSpectator() || ((Player) livingEntity).isCreative())) {
                return false;
            } else if (!this.canUse()) {
                return false;
            } else {
                return DeadlyNadder.this.isFlying();
            }
        }

        public void tick() {
            LivingEntity livingEntity = DeadlyNadder.this.getTarget();
            DeadlyNadder.this.targetPosition = new Vec3(livingEntity.getX(), livingEntity.getY(0.5D), livingEntity.getZ());
            if (DeadlyNadder.this.getBoundingBox().inflate(0.20000000298023224D).intersects(livingEntity.getBoundingBox())) {
                DeadlyNadder.this.canAttack(livingEntity);
                if (!DeadlyNadder.this.isSilent()) {
                    level().playSound(null, DeadlyNadder.this, SoundEvents.RAVAGER_ATTACK, DeadlyNadder.this.getSoundSource(), DeadlyNadder.this.getSoundVolume(), DeadlyNadder.this.getVoicePitch());
                }
            }
        }
    }

    public enum Variant {
        HERO(0),
        COPPER(1),
        IMPERIAL(2),
        TEAL(3);

        public static final DeadlyNadder.Variant[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(DeadlyNadder.Variant::getId)).toArray(Variant[]::new);

        private final int id;

        public int getId() {
            return this.id;
        }

        private Variant(int p_149239_) {
            this.id = p_149239_;
        }
    }
}
