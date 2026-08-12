package com.brenknigh.greygoo.block;

import com.brenknigh.greygoo.core.GooProtection;
import com.brenknigh.greygoo.core.GooSpreadCategory;
import com.brenknigh.greygoo.core.GooState;
import com.brenknigh.greygoo.registry.GreyGooBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Orange Goo (Wall) — unbreakable barrier spread from Green anchors. Port of {@code BlockWall}.
 */
public class OrangeWallBlock extends GooBlock {
    public static final BooleanProperty DORMANT = BooleanProperty.create("dormant");

    public OrangeWallBlock(Properties properties) {
        super(properties.randomTicks());
        registerDefaultState(stateDefinition.any().setValue(DORMANT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DORMANT);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canAct(level) || state.getValue(DORMANT)) {
            return;
        }
        if (shouldConvertToCleaner(level, pos)) {
            convertSelfToCleaner(level, pos);
            return;
        }
        if (tryAcquireSpread(GooSpreadCategory.GENERAL, false)) {
            grow(level, pos, random, false);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && !player.isShiftKeyDown()) {
            level.setBlock(pos, state.setValue(DORMANT, false), Block.UPDATE_ALL);
            if (level instanceof ServerLevel serverLevel) {
                grow(serverLevel, pos, level.getRandom(), true);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void grow(ServerLevel level, BlockPos origin, RandomSource random, boolean force) {
        boolean spreadThisTick = false;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (Math.abs(dx) + Math.abs(dy) + Math.abs(dz) >= 2) {
                        continue;
                    }

                    BlockPos target = origin.offset(dx, dy, dz);
                    BlockPos opposite = origin.offset(-dx, -dy, -dz);
                    BlockState oppositeState = level.getBlockState(opposite);

                    if (!GooState.isOrangeWall(oppositeState) && !GooState.isGreenGoo(oppositeState)) {
                        continue;
                    }

                    BlockState targetState = level.getBlockState(target);
                    Block targetBlock = targetState.getBlock();

                    if (GooState.isGreenGoo(targetState)) {
                        continue;
                    }

                    if (GooProtection.isNeverEaten(targetBlock) && !targetState.is(Blocks.SNOW)) {
                        continue;
                    }

                    level.setBlock(target, defaultBlockState(), Block.UPDATE_ALL);
                    tryAcquireSpread(GooSpreadCategory.GENERAL, true);
                    spreadThisTick = true;
                }
            }
        }

        if (!spreadThisTick && !force) {
            level.setBlock(origin, defaultBlockState().setValue(DORMANT, true), Block.UPDATE_ALL);
        }
    }
}
