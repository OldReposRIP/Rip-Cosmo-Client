package cope.cosmos.loader.asm.mixins;

import cope.cosmos.client.events.BlockBreakEvent;
import cope.cosmos.client.events.BlockResetEvent;
import cope.cosmos.client.events.ReachEvent;
import cope.cosmos.util.Wrapper;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ PlayerControllerMP.class})
public class MixinPlayerControllerMP implements Wrapper {

    @Inject(
        method = { "onPlayerDestroyBlock"},
        at = {             @At("RETURN")}
    )
    private void destroyBlock(BlockPos pos, CallbackInfoReturnable info) {
        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(pos);

        MinecraftForge.EVENT_BUS.post(blockBreakEvent);
        if (blockBreakEvent.isCanceled()) {
            info.cancel();
            info.setReturnValue(Boolean.valueOf(false));
        }

    }

    @Inject(
        method = { "resetBlockRemoving"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    private void resetBlock(CallbackInfo info) {
        BlockResetEvent blockResetEvent = new BlockResetEvent();

        MinecraftForge.EVENT_BUS.post(blockResetEvent);
        if (blockResetEvent.isCanceled()) {
            info.cancel();
        }

    }

    @Inject(
        method = { "getBlockReachDistance"},
        at = {             @At("RETURN")},
        cancellable = true
    )
    private void getReachDistanceHook(CallbackInfoReturnable info) {
        ReachEvent reachEvent = new ReachEvent();

        MinecraftForge.EVENT_BUS.post(reachEvent);
        if (reachEvent.isCanceled()) {
            info.cancel();
            info.setReturnValue(Float.valueOf(reachEvent.getReach()));
        }

    }
}
