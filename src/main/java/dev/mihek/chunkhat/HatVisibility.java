package dev.mihek.chunkhat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkStatus;

public final class HatVisibility {
    private HatVisibility() {}

    public record Status(boolean showHat, String reason, int viewDistance, long chunkDistance) {}

    public static Status inspect(MinecraftClient client, PlayerListEntry entry) {
        if (client.world == null || client.player == null) {
            return new Status(false, "Dünya hazır değil", 0, -1);
        }
        int view = Math.min(client.options.getViewDistance().getValue(),
                client.options.getClampedViewDistance());
        PlayerEntity player = client.world.getPlayerByUuid(entry.getProfile().id());
        if (player == null || player.isRemoved()) {
            return new Status(false, "Sunucu bu oyuncunun konumunu göndermiyor", view, -1);
        }
        ChunkPos center = client.player.getChunkPos();
        ChunkPos target = player.getChunkPos();
        long distance = Math.max(Math.abs((long) target.x - center.x), Math.abs((long) target.z - center.z));
        if (!ActiveChunkRange.contains(center.x, center.z, target.x, target.z, view)) {
            return new Status(false, "Aktif mesafenin dışında", view, distance);
        }
        if (client.world.getChunkManager().getChunk(target.x, target.z, ChunkStatus.FULL, false) == null) {
            return new Status(false, "Oyuncunun chunk'ı yüklü değil", view, distance);
        }
        return new Status(true, "Aktif chunk içinde", view, distance);
    }
}
