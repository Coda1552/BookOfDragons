package coolrex.bookofdragons.common.entities.misc.lookcontrol;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;

public class FlyingLookControl extends LookControl {
   private final int maxYRotFromCenter;

   public FlyingLookControl(Mob pMob, int pMaxYRotFromCenter) {
      super(pMob);
      this.maxYRotFromCenter = pMaxYRotFromCenter;
   }

   public void tick() {
      if (this.lookAtCooldown > 0) {
         --this.lookAtCooldown;
         this.getYRotD().ifPresent((wantedY) -> {
            this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, wantedY + 20.0F, this.yMaxRotSpeed);
         });
         this.getXRotD().ifPresent((wantedX) -> {
            this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), wantedX + 10.0F, this.xMaxRotAngle));
         });
      } else {
         if (this.mob.getNavigation().isDone() && !mob.isVehicle()) {
            this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), 0.0F, 5.0F));
         }

         this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, this.yMaxRotSpeed);
      }

      float f = Mth.wrapDegrees(this.mob.yHeadRot - this.mob.yBodyRot);
      if (f < (float)(-this.maxYRotFromCenter)) {
         this.mob.yBodyRot -= 4.0F;
      } else if (f > (float)this.maxYRotFromCenter) {
         this.mob.yBodyRot += 4.0F;
      }

   }
}