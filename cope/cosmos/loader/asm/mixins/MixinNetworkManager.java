package cope.cosmos.loader.asm.mixins;

import cope.cosmos.client.events.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ NetworkManager.class})
public class MixinNetworkManager {

    @Inject(
        method = { "sendPacket(Lnet/minecraft/network/Packet;)V"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void onPacketSend(Packet packet, CallbackInfo info) {
        PacketEvent.PacketSendEvent packetSendEvent = new PacketEvent.PacketSendEvent(packet);

        MinecraftForge.EVENT_BUS.post(packetSendEvent);
        if (packetSendEvent.isCanceled()) {
            info.cancel();
        }

    }

    @Inject(
        method = { "channelRead0"},
        at = {             @At("HEAD")},
        cancellable = true
    )
    public void onPacketReceive(ChannelHandlerContext chc, Packet packet, CallbackInfo info) {
        PacketEvent.PacketReceiveEvent packetReceiveEvent = new PacketEvent.PacketReceiveEvent(packet);

        MinecraftForge.EVENT_BUS.post(packetReceiveEvent);
        if (packetReceiveEvent.isCanceled()) {
            info.cancel();
        }

    }
}
