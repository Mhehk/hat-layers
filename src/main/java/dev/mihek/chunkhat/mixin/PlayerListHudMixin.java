package dev.mihek.chunkhat.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.mihek.chunkhat.HatVisibility;
import dev.mihek.chunkhat.TabHatMarker;
import dev.mihek.chunkhat.ChunkHatDiagnostics;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {
    @Shadow @Final private MinecraftClient client;

    // Vanilla hides all TAB heads on unencrypted (offline-mode) connections.
    // This changes only that local drawing decision, never the connection itself.
    @ModifyExpressionValue(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;isEncrypted()Z"),
            require = 1,
            allow = 1
    )
    private boolean chunkHat$drawHeadsOnAllServers(boolean original) {
        return true;
    }

    @WrapOperation(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/PlayerSkinDrawer;draw(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;IIIZZI)V"),
            require = 1,
            allow = 1
    )
    private void chunkHat$drawHead(DrawContext context, Identifier texture, int x, int y, int size,
                                  boolean originalHat, boolean upsideDown, int color,
                                  Operation<Void> original, @Local PlayerListEntry entry) {
        boolean showHat = HatVisibility.inspect(client, entry).showHat();
        // Apply the decision to the actual draw argument, not the upstream skin preference.
        original.call(context, texture, x, y, size, showHat, upsideDown, color);
        if (showHat) {
            TabHatMarker.draw(context, x, y, size, color);
        }
        ChunkHatDiagnostics.recordDraw(client.world, entry.getProfile().id(), showHat);
    }
}
