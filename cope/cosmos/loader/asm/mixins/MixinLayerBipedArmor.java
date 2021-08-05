package cope.cosmos.loader.asm.mixins;

import cope.cosmos.client.events.LayerArmorEvent;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LayerBipedArmor.class})
public class MixinLayerBipedArmor {

    @Inject(
        method = { "setModelSlotVisible"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    protected void setModelSlotVisible(ModelBiped model, EntityEquipmentSlot slotIn, CallbackInfo info) {
        LayerArmorEvent layerArmorEvent = new LayerArmorEvent(model, slotIn);

        MinecraftForge.EVENT_BUS.post(layerArmorEvent);
        if (layerArmorEvent.isCanceled()) {
            info.cancel();
        }

    }
}
