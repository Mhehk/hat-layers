package dev.mihek.chunkhat;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.lang.ref.WeakReference;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;

/** All output is local chat; no command or diagnostic is sent to the server. */
public final class ChunkHatDiagnostics {
    private static final Map<UUID, DrawState> LAST_DRAWS = new LinkedHashMap<>();
    private static WeakReference<ClientWorld> lastWorld = new WeakReference<>(null);

    private ChunkHatDiagnostics() {}

    private record DrawState(boolean showHat, long time) {}

    public static void recordDraw(ClientWorld world, UUID id, boolean showHat) {
        if (lastWorld.get() != world) {
            LAST_DRAWS.clear();
            lastWorld = new WeakReference<>(world);
        }
        // Keep this diagnostic history bounded even on servers with rotating fake TAB entries.
        if (!LAST_DRAWS.containsKey(id) && LAST_DRAWS.size() >= 160) {
            LAST_DRAWS.remove(LAST_DRAWS.keySet().iterator().next());
        }
        LAST_DRAWS.put(id, new DrawState(showHat, System.nanoTime()));
    }

    public static boolean handle(String command) {
        String root = command.split(" ", 2)[0];
        if (!root.equals("hatlayers") && !root.equals("chunkhat")) {
            return false;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() == null) {
            return true;
        }
        String name = command.substring(root.length()).trim();
        String version = FabricLoader.getInstance().getModContainer("chunk_hat").orElseThrow()
                .getMetadata().getVersion().getFriendlyString();
        say(client, "Hat Layers " + version + " | Yüz kalır; yalnızca ikinci kafa katmanı kontrol edilir.");
        int displayed = 0;
        for (PlayerListEntry entry : client.getNetworkHandler().getPlayerList()) {
            if (!name.isEmpty() && !entry.getProfile().name().equalsIgnoreCase(name)) {
                continue;
            }
            HatVisibility.Status status = HatVisibility.inspect(client, entry);
            // Without a name, show tracked players rather than flooding a large server's TAB list.
            if (name.isEmpty() && status.chunkDistance() < 0) {
                continue;
            }
            String distance = status.chunkDistance() < 0 ? "bilinmiyor" : status.chunkDistance() + " chunk";
            say(client, entry.getProfile().name() + ": hat " + (status.showHat() ? "AÇIK" : "KAPALI")
                    + " | " + status.reason() + " | uzaklık " + distance + " | sınır " + status.viewDistance());
            DrawState drawn = lastWorld.get() == client.world ? LAST_DRAWS.get(entry.getProfile().id()) : null;
            if (drawn != null) {
                long secondsAgo = (System.nanoTime() - drawn.time()) / 1_000_000_000L;
                say(client, "Son TAB çizimi: hat " + (drawn.showHat() ? "AÇIK" : "KAPALI")
                        + " (" + secondsAgo + " sn önce)");
            } else {
                say(client, "Bu oyuncu için henüz TAB çizimi kaydedilmedi. TAB'ı açıp tekrar kontrol et.");
            }
            if (++displayed >= 10) {
                break;
            }
        }
        if (displayed == 0 && !name.isEmpty()) {
            say(client, "TAB listesinde bu isim bulunamadı: " + name);
        }
        if (name.isEmpty()) {
            say(client, "Belirli bir oyuncu için: /hatlayers OyuncuAdı");
        }
        return true;
    }

    private static void say(MinecraftClient client, String message) {
        client.inGameHud.getChatHud().addMessage(Text.literal("[Hat Layers] " + message));
    }
}
