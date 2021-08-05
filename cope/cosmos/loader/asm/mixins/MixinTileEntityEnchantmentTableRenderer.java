package cope.cosmos.loader.asm.mixins;

import cope.cosmos.client.events.RenderEnchantmentTableBookEvent;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ TileEntityEnchantmentTableRenderer.class})
public class MixinTileEntityEnchantmentTableRenderer {

    @Inject(
        method = { "render"},
        at = {             @At("INVOKE")},
        cancellable = true
    )
    private void renderEnchantingTableBook(TileEntityEnchantmentTable tileEntityEnchantmentTable, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo info) {
        RenderEnchantmentTableBookEvent renderEnchantmentTableBookEvent = new RenderEnchantmentTableBookEvent();

        MinecraftForge.EVENT_BUS.post(renderEnchantmentTableBookEvent);
        if (renderEnchantmentTableBookEvent.isCanceled()) {
            info.cancel();
        }

    }
}
