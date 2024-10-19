package coolrex.bookofdragons.common.entities.misc;


import software.bernie.geckolib.core.animation.RawAnimation;

public class BODAnimations {
    public static final RawAnimation FLY = RawAnimation.begin().thenLoop("fly");
    public static final RawAnimation FLY_IDLE = RawAnimation.begin().thenLoop("fly_idle");
    public static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation SWIM = RawAnimation.begin().thenLoop("swim");
    public static final RawAnimation SWIM_IDLE = RawAnimation.begin().thenLoop("swim_idle");
    public static final RawAnimation BITE = RawAnimation.begin().thenLoop("bite");
    public static final RawAnimation FIRE = RawAnimation.begin().thenLoop("fire");
    public static final RawAnimation GLIDE = RawAnimation.begin().thenLoop("glide");
    public static final RawAnimation SIT = RawAnimation.begin().thenLoop("sit");
    public static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("sleep");
    public static final RawAnimation SPECIAL_ATTACK = RawAnimation.begin().thenLoop("special_attack"); // threaten, spinshot, etc.
}
