package cope.cosmos.loader.asm.mixins;

import cope.cosmos.client.events.EntityCollisionEvent;
import cope.cosmos.client.events.TravelEvent;
import cope.cosmos.client.events.WaterCollisionEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ EntityPlayer.class})
public abstract class MixinEntityPlayer extends EntityLivingBase {

    public MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    @Inject(
        method = { "travel"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void travel(float strafe, float vertical, float forward, CallbackInfo info) {
        TravelEvent travelEvent = new TravelEvent(strafe, vertical, forward);

        MinecraftForge.EVENT_BUS.post(travelEvent);
        if (travelEvent.isCanceled()) {
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            info.cancel();
        }

    }

    @Inject(
        method = { "applyEntityCollision"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void applyEntityCollision(Entity entity, CallbackInfo info) {
        EntityCollisionEvent entityCollisionEvent = new EntityCollisionEvent();

        MinecraftForge.EVENT_BUS.post(entityCollisionEvent);
        if (entityCollisionEvent.isCanceled()) {
            info.cancel();
        }

    }

    @Inject(
        method = { "isPushedByWater()Z"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void isPushedByWater(CallbackInfoReturnable info) {
        WaterCollisionEvent waterCollisionEvent = new WaterCollisionEvent();

        MinecraftForge.EVENT_BUS.post(waterCollisionEvent);
        if (waterCollisionEvent.isCanceled()) {
            info.cancel();
            info.setReturnValue(Boolean.valueOf(false));
        }

    }
}
