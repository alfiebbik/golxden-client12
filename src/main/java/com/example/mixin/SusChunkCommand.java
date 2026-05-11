package com.suschunkfinder;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;

import java.util.List;

public class SusChunkCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            ClientCommandManager.literal("suschunk")
                .executes(ctx -> runScan(ctx.getSource(), 5))
                .then(ClientCommandManager.argument("radius", IntegerArgumentType.integer(1, 20))
                    .executes(ctx -> runScan(ctx.getSource(),
                            IntegerArgumentType.getInteger(ctx, "radius"))))
        );
    }

    private static int runScan(FabricClientCommandSource source, int radius) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.world == null || client.player == null) {
            source.sendFeedback(Text.literal("§cNot in a world!"));
            return 0;
        }

        source.sendFeedback(Text.literal("§6[Golxden] §eScanning chunks..."));

        List<ChunkPos> results = ChunkScanner.findSusChunks(
                client.world,
                client.player.getBlockPos(),
                radius
        );

        client.execute(() -> client.setScreen(new GolxdenScreen(results)));

        return 1;
    }
}