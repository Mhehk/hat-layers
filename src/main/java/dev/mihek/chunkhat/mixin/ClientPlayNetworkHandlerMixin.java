package dev.mihek.chunkhat.mixin;

import dev.mihek.chunkhat.ChunkHatDiagnostics;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(method = "sendChatCommand", at = @At("HEAD"), cancellable = true)
    private void chunkHat$localCommand(String command, CallbackInfo ci) {
        if (ChunkHatDiagnostics.handle(command)) {
            ci.cancel();
        }
    }
}
