package com.brenknigh.greygoo.block;

import com.brenknigh.greygoo.GreyGooMod;
import com.brenknigh.greygoo.core.GooSpreadCategory;
import com.brenknigh.greygoo.core.GooState;
import com.brenknigh.greygoo.registry.GreyGooBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Base class for spreading goo blocks.
 */
public abstract class GooBlock extends Block {
    protected GooBlock(Properties properties) {
        super(properties);
    }

    protected boolean canAct(Level level) {
        return !level.isClientSide && GooState.isGooActive(level);
    }

    protected boolean tryAcquireSpread(GooSpreadCategory category, boolean incrementAfterSpread) {
        return GreyGooMod.getInstance().getSpreadLimiter().trySpread(category, incrementAfterSpread);
    }

    protected boolean shouldConvertToCleaner(Level level, BlockPos pos) {
        return GooState.isAdjacentCleaner(level, pos);
    }

    protected void convertSelfToCleaner(Level level, BlockPos pos) {
        level.setBlock(pos, GreyGooBlocks.RED_GOO.get().defaultBlockState(), Block.UPDATE_ALL);
    }
}
