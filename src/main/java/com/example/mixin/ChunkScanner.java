package com.suschunkfinder;

import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;

public class ChunkScanner {

    private static final int SUS_Y_LEVEL = 23;

    public static List<ChunkPos> findSusChunks(ClientWorld world, BlockPos center, int radiusInChunks) {
        List<ChunkPos> susChunks = new ArrayList<>();
        ChunkPos centerChunk = new ChunkPos(center);

        for (int cx = centerChunk.x - radiusInChunks; cx <= centerChunk.x + radiusInChunks; cx++) {
            for (int cz = centerChunk.z - radiusInChunks; cz <= centerChunk.z + radiusInChunks; cz++) {
                if (!world.isChunkLoaded(cx, cz)) continue;
                WorldChunk chunk = world.getChunk(cx, cz);
                if (chunk == null) continue;

                if (chunkHasSusCobbledDeepslate(world, cx, cz)) {
                    susChunks.add(new ChunkPos(cx, cz));
                }
            }
        }

        return susChunks;
    }

    private static boolean chunkHasSusCobbledDeepslate(ClientWorld world, int chunkX, int chunkZ) {
        int startX = chunkX * 16;
        int startZ = chunkZ * 16;

        for (int x = startX; x < startX + 16; x++) {
            for (int z = startZ; z < startZ + 16; z++) {
                for (int y = world.getBottomY(); y < SUS_Y_LEVEL; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (world.getBlockState(pos).isOf(Blocks.COBBLED_DEEPSLATE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}