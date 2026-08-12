package com.brenknigh.greygoo.block;

import com.brenknigh.greygoo.core.CoordHolder;
import com.brenknigh.greygoo.core.GooSpreadCategory;
import com.brenknigh.greygoo.core.SpreadHelper;
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
 * White Goo (Air Eater) — fills lit air, dies in darkness. Port of {@code BlockAirEater}.
 */
public class WhiteGooBlock extends GooBlock {
    public static final BooleanProperty DORMANT = BooleanProperty.create("dormant");

    public WhiteGooBlock(Properties properties) {
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

        SpreadHelper helper = new SpreadHelper(level, pos.getX(), pos.getY(), pos.getZ(), 1, false, true);
        helper.addBlock(GreyGooBlocks.RED_GOO.get());
        if (!helper.findBlocks().isEmpty()) {
            convertSelfToCleaner(level, pos);
            return;
        }

        helper.clearBlockCheckList();
        helper.addBlock(Blocks.AIR);
        helper.addBlock(Blocks.CAVE_AIR);
        helper.addBlock(Blocks.VOID_AIR);

        boolean hasFood = false;
        for (CoordHolder hold : helper.findBlocks()) {
            BlockPos target = new BlockPos(hold.x(), hold.y(), hold.z());
            if (level.getMaxLocalRawBrightness(target) > 6) {
                level.setBlock(target, defaultBlockState(), Block.UPDATE_ALL);
                hasFood = true;
                tryAcquireSpread(GooSpreadCategory.GENERAL, true);
            }
        }

        if (!hasFood) {
            level.setBlock(pos, state.setValue(DORMANT, true), Block.UPDATE_ALL);
        }
    }

    private void decay(ServerLevel level, BlockPos pos) {
        for (BlockPos check : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            if (level.getMaxLocalRawBrightness(check) < 2) {
                level.removeBlock(pos, false);
                return;
            }
        }
    }
}
