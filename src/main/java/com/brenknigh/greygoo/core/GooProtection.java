package com.brenknigh.greygoo.core;

import java.util.HashSet;
import java.util.Set;

import com.brenknigh.greygoo.registry.GreyGooBlocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * Protection and cleaner target lists from the original {@code mod_GreyGoo} init.
 * Expanded as more blocks are ported.
 */
public final class GooProtection {
    private static final Set<Block> GOO_NEVER_EAT = new HashSet<>();
    private static final Set<Block> CLEANER_TARGETS = new HashSet<>();
    private static final Set<Block> MINE_THESE_ONLY = new HashSet<>();

    private GooProtection() {}

    public static void init() {
        GOO_NEVER_EAT.clear();
        CLEANER_TARGETS.clear();
        MINE_THESE_ONLY.clear();

        GOO_NEVER_EAT.add(GreyGooBlocks.RED_GOO.get());
        GOO_NEVER_EAT.add(GreyGooBlocks.GREEN_GOO.get());
        GOO_NEVER_EAT.add(GreyGooBlocks.ORANGE_GOO.get());
        GOO_NEVER_EAT.add(Blocks.CHEST);
        GOO_NEVER_EAT.add(Blocks.ENDER_CHEST);

        CLEANER_TARGETS.add(GreyGooBlocks.GREEN_GOO.get());
        CLEANER_TARGETS.add(GreyGooBlocks.ORANGE_GOO.get());
        CLEANER_TARGETS.add(GreyGooBlocks.RED_GOO.get());
        CLEANER_TARGETS.add(GreyGooBlocks.PURPLE_GOO.get());

        MINE_THESE_ONLY.add(Blocks.GRAVEL);
        MINE_THESE_ONLY.add(Blocks.STONE);
        MINE_THESE_ONLY.add(Blocks.SAND);
        MINE_THESE_ONLY.add(Blocks.SANDSTONE);
        MINE_THESE_ONLY.add(Blocks.NETHERRACK);
        MINE_THESE_ONLY.add(Blocks.SOUL_SAND);
        MINE_THESE_ONLY.add(Blocks.CLAY);
    }

    public static boolean isNeverEaten(Block block) {
        return GOO_NEVER_EAT.contains(block);
    }

    public static boolean canBeCleaned(Block block) {
        return CLEANER_TARGETS.contains(block);
    }

    public static boolean isMineTarget(Block block) {
        return MINE_THESE_ONLY.contains(block);
    }

    public static Set<Block> neverEatBlocks() {
        return Set.copyOf(GOO_NEVER_EAT);
    }

    public static Set<Block> cleanerTargets() {
        return Set.copyOf(CLEANER_TARGETS);
    }
}
