package coda.bookofdragons.common.entities;

import coda.bookofdragons.common.entities.util.AbstractRideableDragonEntity;
import coda.bookofdragons.common.entities.util.goal.FlyingDragonWanderGoal;
import coda.bookofdragons.init.BODEntities;
import coda.bookofdragons.init.BODItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class DeadlyNadderEntity extends AbstractRideableDragonEntity implements FlyingAnimal, IAnimatable, IAnimationTickable {
    private final AnimationFactory factory = new AnimationFactory(this);

    public DeadlyNadderEntity(EntityType<? extends DeadlyNadderEntity> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 40.0D).add(Attributes.MOVEMENT_SPEED, 0.285F).add(Attributes.FLYING_SPEED, 0.5F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new FlyingDragonWanderGoal(this, 150));
    }

    @Override
    public Ingredient getIngredient() {
        return Ingredient.of(ItemTags.FISHES);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.FISHES);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob ageableMob) {
        return BODEntities.DEADLY_NADDER.get().create(world);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(BODItems.DEADLY_NADDER_SPAWN_EGG.get());
    }

    @Override
    protected float getStandingEyeHeight(Pose p_21131_, EntityDimensions p_21132_) {
        return isBaby() ? 1.0F : 1.5F;
    }

    @Override
    public void positionRider(Entity passenger) {
        Vec3 pos = getYawVec(yBodyRot, 0.0F, -0.35F).add(getX(), getY() + 1.5F, getZ());
        passenger.setPos(pos.x, pos.y, pos.z);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (!isOnGround() && event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("fly", true));
            return PlayState.CONTINUE;
        }
        else if (!isOnGround() && !event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("fly_idle", true));
            return PlayState.CONTINUE;
        }
        else if (!isFlying() && event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
            return PlayState.CONTINUE;
        }
        else if (!isFlying() && !event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("land_idle", true));
            return PlayState.CONTINUE;
        }
        else {
            return PlayState.STOP;
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }
}
