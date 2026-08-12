package com.brenknigh.greygoo.block;

import com.brenknigh.greygoo.core.GooSpreadCategory;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Blue Goo (Water Eater) — converts adjacent water/lava, with column collapse. Port of {@code BlockWaterEater}.
 */
public class BlueGooBlock extends GooBlock {
    public static final BooleanProperty DORMANT = BooleanProperty.create("dormant");

    public BlueGooBlock(Properties properties) {
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
        assimilate(level, pos, state);
        decay(level, pos);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && !player.isShiftKeyDown() && level instanceof ServerLevel serverLevel) {
            assimilate(serverLevel, pos, state);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void assimilate(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.getValue(DORMANT)) {
            return;
        }

        if (shouldConvertToCleaner(level, pos)) {
            convertSelfToCleaner(level, pos);
            return;
        }

        boolean noFood = true;
        for (Direction direction : Direction.values()) {
            BlockPos target = pos.relative(direction);
            FluidState fluid = level.getFluidState(target);
            if (fluid.is(FluidTags.WATER) || fluid.is(FluidTags.LAVA)) {
                level.setBlock(target, defaultBlockState(), Block.UPDATE_ALL);
                noFood = false;
                tryAcquireSpread(GooSpreadCategory.GENERAL, true);
            }
        }

        if (noFood) {
            level.setBlock(pos, state.setValue(DORMANT, true), Block.UPDATE_ALL);
        }
    }

    private void decay(ServerLevel level, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        if (level.isEmptyBlock(pos.above())
                && level.isEmptyBlock(pos.east())
                && level.isEmptyBlock(pos.west())
                && level.isEmptyBlock(pos.south())
                && level.isEmptyBlock(pos.north())) {
            int l = 0;
            boolean done = false;

            while (!done) {
                BlockPos check = new BlockPos(x, y + l, z);
                if (level.isEmptyBlock(check.east())
                        && level.isEmptyBlock(check.west())
                        && level.isEmptyBlock(check.south())
                        && level.isEmptyBlock(check.north())
                        && l > -100) {
                    l--;
                } else {
                    done = true;
                }
            }

            for (int depth = Math.abs(l); depth != 0; depth--) {
                level.removeBlock(new BlockPos(x, y - depth, z), false);
            }
            level.removeBlock(pos, false);
        } else if (level.isEmptyBlock(pos.above())) {
            level.removeBlock(pos, false);
        }
    }
}
