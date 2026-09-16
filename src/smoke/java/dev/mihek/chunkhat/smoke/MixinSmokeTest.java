package dev.mihek.chunkhat.smoke;

import java.lang.reflect.Method;
import java.util.Arrays;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.FabricLoader;

public final class MixinSmokeTest implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        try {
            assertInjected("net.minecraft.client.gui.hud.PlayerListHud", "chunkHat$drawHead");
            assertInjected("net.minecraft.client.gui.hud.PlayerListHud", "chunkHat$drawHeadsOnAllServers");
            assertInjected("net.minecraft.client.network.ClientPlayNetworkHandler", "chunkHat$localCommand");
            if (FabricLoader.getInstance().isModLoaded("offline-tab-head")) {
                assertInjected("net.minecraft.client.gui.hud.PlayerListHud", "onTabRender");
            }
            if (FabricLoader.getInstance().isModLoaded("betterpingdisplay")) {
                assertInjected("net.minecraft.client.gui.hud.PlayerListHud", "redirectRenderLatencyIconCall");
            }
            System.out.println("CHUNK_HAT_MIXIN_SMOKE_OK: actual Minecraft classes transformed successfully.");
            System.exit(0);
        } catch (Throwable failure) {
            failure.printStackTrace();
            System.exit(1);
        }
    }

    private void assertInjected(String name, String handler) throws Exception {
        Class<?> target = Class.forName(name, false, getClass().getClassLoader());
        long count = Arrays.stream(target.getDeclaredMethods()).map(Method::getName)
                .filter(method -> method.endsWith(handler)).count();
        if (count != 1) {
            throw new AssertionError("Expected one transformed handler " + handler + ", found " + count);
        }
    }
}
