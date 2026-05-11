package com.suschunkfinder;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SusChunkFinder implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("suschunkfinder");

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            SusChunkCommand.register(dispatcher);
        });

        GolxdenKeyBinding.register(); // ← registers Right Shift

        LOGGER.info("✦ Golxden Client loaded!");
    }
}