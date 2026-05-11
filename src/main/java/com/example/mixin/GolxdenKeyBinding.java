package com.suschunkfinder;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import net.minecraft.util.math.ChunkPos;

public class GolxdenKeyBinding {

    private static KeyBinding openGuiKey;

    public static void register() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.suschunkfinder.opengui",   // translation key
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,       // Right Shift
                "category.suschunkfinder"        // category in controls menu
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.world == null || client.player == null) return;

                // Scan 5 chunk radius and open GUI
                List<ChunkPos> results = ChunkScanner.findSusChunks(
                        client.world,
                        client.player.getBlockPos(),
                        5
                );

                client.execute(() -> client.setScreen(new GolxdenScreen(results)));
            }
        });
    }
}