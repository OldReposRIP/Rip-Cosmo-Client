package cope.cosmos.loader.asm.mixins;

import cope.cosmos.client.events.RenderWitherSkullEvent;
import net.minecraft.client.renderer.entity.RenderWitherSkull;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ RenderWitherSkull.class})
public class MixinRenderWitherSkull {

    @Inject(
        method = { "doRender"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void doRender(EntityWitherSkull entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        RenderWitherSkullEvent renderWitherSkullEvent = new RenderWitherSkullEvent();

        MinecraftForge.EVENT_BUS.post(renderWitherSkullEvent);
        if (renderWitherSkullEvent.isCanceled()) {
            info.cancel();
        }

    }
}
