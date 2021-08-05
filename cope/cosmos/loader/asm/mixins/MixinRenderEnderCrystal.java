package cope.cosmos.loader.asm.mixins;

import cope.cosmos.client.events.CrystalTextureEvent;
import cope.cosmos.client.events.RenderCrystalEvent;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
    value = { RenderEnderCrystal.class},
    priority = 2147483646
)
public class MixinRenderEnderCrystal {

    @Final
    @Shadow
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;
    @Final
    @Shadow
    private ModelBase modelEnderCrystal;
    @Final
    @Shadow
    private ModelBase modelEnderCrystalNoBase;

    @Redirect(
        method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V"},
        at =             @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"
            )
    )
    private void doRender(ModelBase modelBase, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        RenderCrystalEvent.RenderCrystalPreEvent renderCrystalEvent = new RenderCrystalEvent.RenderCrystalPreEvent(modelBase, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        MinecraftForge.EVENT_BUS.post(renderCrystalEvent);
        if (!renderCrystalEvent.isCanceled()) {
            modelBase.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }

        CrystalTextureEvent crystalTextureEvent = new CrystalTextureEvent();

        MinecraftForge.EVENT_BUS.post(crystalTextureEvent);
    }

    @Inject(
        method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V"},
        at = {             @At("RETURN")},
        cancellable = true
    )
    public void doRender(EntityEnderCrystal entityEnderCrystal, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        RenderCrystalEvent.RenderCrystalPostEvent renderCrystalEvent = new RenderCrystalEvent.RenderCrystalPostEvent(this.modelEnderCrystal, this.modelEnderCrystalNoBase, entityEnderCrystal, x, y, z, entityYaw, partialTicks);

        MinecraftForge.EVENT_BUS.post(renderCrystalEvent);
        if (renderCrystalEvent.isCanceled()) {
            info.cancel();
        }

    }
}
