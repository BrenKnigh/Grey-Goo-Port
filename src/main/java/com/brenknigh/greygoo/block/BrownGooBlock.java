package com.brenknigh.greygoo.block;

import com.brenknigh.greygoo.core.GooProtection;
import com.brenknigh.greygoo.core.GooSpreadCategory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
 * Brown Goo (Miner) — selectively mines stone/dirt types, preserves ores. Port of {@code BlockMinerGoo}.
 */
public class BrownGooBlock extends GooBlock {
    public static final BooleanProperty DORMANT = BooleanProperty.create("dormant");

    public BrownGooBlock(Properties properties) {
        super(properties.randomTicks());
        registerDefaultState(stateDefinition.any().setValue(DORMANT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DORMANT);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canAct(level) || !tryAcquireSpread(GooSpreadCategory.GENERAL, false)) {
            return;
        }
        mine(level, pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && !player.isShiftKeyDown() && level instanceof ServerLevel serverLevel) {
            mine(serverLevel, pos, state);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void mine(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.getValue(DORMANT)) {
            return;
        }

        if (shouldConvertToCleaner(level, pos)) {
            convertSelfToCleaner(level, pos);
            return;
        }

        boolean hasFood = false;
        for (Direction direction : Direction.values()) {
            BlockPos target = pos.relative(direction);
            BlockState targetState = level.getBlockState(target);
            Block targetBlock = targetState.getBlock();

            if (shouldSkip(targetBlock) || level.isEmptyBlock(target)) {
                continue;
            }
            if (GooProtection.isNeverEaten(targetBlock) || !GooProtection.isMineTarget(targetBlock)) {
                continue;
            }

            level.setBlock(target, defaultBlockState(), Block.UPDATE_ALL);
            hasFood = true;
            tryAcquireSpread(GooSpreadCategory.GENERAL, true);
        }

        if (!hasFood) {
            level.setBlock(pos, state.setValue(DORMANT, true), Block.UPDATE_ALL);
        }
    }

    private boolean shouldSkip(Block block) {
        return block == this
                || block == Blocks.BEDROCK
                || block == Blocks.CHEST
                || block == Blocks.DIAMOND_ORE
                || block == Blocks.DEEPSLATE_DIAMOND_ORE
                || block == Blocks.GOLD_ORE
                || block == Blocks.DEEPSLATE_GOLD_ORE
                || block == Blocks.IRON_ORE
                || block == Blocks.DEEPSLATE_IRON_ORE
                || block == Blocks.COAL_ORE
                || block == Blocks.DEEPSLATE_COAL_ORE
                || block == Blocks.LAPIS_ORE
                || block == Blocks.DEEPSLATE_LAPIS_ORE
                || block == Blocks.REDSTONE_ORE
                || block == Blocks.DEEPSLATE_REDSTONE_ORE;
    }
}
