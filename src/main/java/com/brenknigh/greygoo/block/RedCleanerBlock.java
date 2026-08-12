package com.brenknigh.greygoo.block;

import com.brenknigh.greygoo.core.GooProtection;
import com.brenknigh.greygoo.registry.GreyGooBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Red Goo (Cleaner) — converts other goo to itself, then self-destructs. Port of {@code BlockCleaner}.
 */
public class RedCleanerBlock extends GooBlock {
    public RedCleanerBlock(Properties properties) {
        super(properties.randomTicks());
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (canAct(level)) {
            clean(level, pos, random);
            level.removeBlock(pos, false);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && !player.isShiftKeyDown() && level instanceof ServerLevel serverLevel) {
            clean(serverLevel, pos, level.getRandom());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void clean(ServerLevel level, BlockPos origin, RandomSource random) {
        boolean gooDetected = false;

        for (int dx = -3; dx <= 3; dx++) {
            for (int dy = -3; dy <= 3; dy++) {
                for (int dz = -3; dz <= 3; dz++) {
                    BlockPos scan = origin.offset(dx, dy, dz);
                    if (GooProtection.canBeCleaned(level.getBlockState(scan).getBlock())) {
                        gooDetected = true;
                    }
                }
            }
        }

        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    if (Math.abs(dx) + Math.abs(dy) + Math.abs(dz) >= 3) {
                        continue;
                    }

                    BlockPos target = origin.offset(dx, dy, dz);
                    Block targetBlock = level.getBlockState(target).getBlock();

                    if (GooProtection.canBeCleaned(targetBlock)) {
                        level.setBlock(target, GreyGooBlocks.RED_GOO.get().defaultBlockState(), Block.UPDATE_ALL);
                    } else if (!gooDetected
                            && targetBlock == GreyGooBlocks.RED_GOO.get()
                            && !target.equals(origin)) {
                        level.removeBlock(target, false);
                    }
                }
            }
        }
    }
}
