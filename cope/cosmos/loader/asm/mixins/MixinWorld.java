package cope.cosmos.loader.asm.mixins;

import cope.cosmos.client.events.EntityWorldEvent;
import cope.cosmos.client.events.RenderSkylightEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ World.class})
public class MixinWorld {

    @Inject(
        method = { "checkLightFor"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void checkLightFor(EnumSkyBlock lightType, BlockPos pos, CallbackInfoReturnable info) {
        RenderSkylightEvent renderSkylightEvent = new RenderSkylightEvent();

        MinecraftForge.EVENT_BUS.post(renderSkylightEvent);
        if (renderSkylightEvent.isCanceled()) {
            info.cancel();
            info.setReturnValue(Boolean.valueOf(true));
        }

    }

    @Inject(
        method = { "spawnEntity"},
        at = {             @At("RETURN")}
    )
    public void spawnEntity(Entity entityIn, CallbackInfoReturnable info) {
        EntityWorldEvent.EntitySpawnEvent entitySpawnEvent = new EntityWorldEvent.EntitySpawnEvent(entityIn);

        MinecraftForge.EVENT_BUS.post(entitySpawnEvent);
    }

    @Inject(
        method = { "removeEntity"},
        at = {             @At("HEAD")}
    )
    public void removeEntity(Entity entityIn, CallbackInfo info) {
        EntityWorldEvent.EntityRemoveEvent entityRemoveEvent = new EntityWorldEvent.EntityRemoveEvent(entityIn);

        MinecraftForge.EVENT_BUS.post(entityRemoveEvent);
    }
}
