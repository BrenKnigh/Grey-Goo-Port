package com.brenknigh.greygoo.core;

import com.brenknigh.greygoo.registry.GreyGooBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Global goo activation checks from the original {@code isGooActive}.
 */
public final class GooState {
    private GooState() {}

    public static boolean isGooActive(Level level) {
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) {
            return false;
        }
        return GooWorldData.get(serverLevel).isGooActive();
    }

    public static boolean isAdjacentCleaner(Level level, BlockPos pos) {
        for (BlockPos offset : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            if (level.getBlockState(offset).is(GreyGooBlocks.RED_GOO.get())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGreenGoo(BlockState state) {
        return state.is(GreyGooBlocks.GREEN_GOO.get());
    }

    public static boolean isOrangeWall(BlockState state) {
        return state.is(GreyGooBlocks.ORANGE_GOO.get());
    }

    public static boolean isProtectedGoo(Block block) {
        return GooProtection.isNeverEaten(block);
    }
}
