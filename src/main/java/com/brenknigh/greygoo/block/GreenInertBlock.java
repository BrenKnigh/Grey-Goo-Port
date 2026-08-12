package com.brenknigh.greygoo.block;

import com.brenknigh.greygoo.core.GooWorldData;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Green Goo (Inert) — anchor block, no spreading. Port of {@code BlockInert}.
 */
public class GreenInertBlock extends GooBlock {
    public GreenInertBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && player.isShiftKeyDown()) {
            GooWorldData data = GooWorldData.get(level);
            player.displayClientMessage(Component.literal("TGD Bloom: " + data.hasTgdBloomed()), false);
            player.displayClientMessage(Component.literal("Global Goo Active: " + data.isGooActive()), false);
            player.displayClientMessage(Component.literal("Dimension: " + level.dimension().location()), false);
            player.displayClientMessage(Component.literal("EMP Array: Inactive (not ported yet)"), false);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
