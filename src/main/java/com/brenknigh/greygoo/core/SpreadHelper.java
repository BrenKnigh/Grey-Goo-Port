package com.brenknigh.greygoo.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Spatial search utility used by spreading goo blocks. Port of the original {@code SpreadHelper},
 * adapted for block registry objects instead of numeric block IDs.
 */
public class SpreadHelper {
    public boolean onlyCheckMaxRadius = false;
    public boolean checkCubeOutline = false;
    public boolean doDiagonals = false;

    private final List<CoordHolder> foundBlockCoords = new ArrayList<>();
    private final Set<Block> blocksToFind = new HashSet<>();

    private boolean findPositiveMatches = true;
    private int xBase;
    private int yBase;
    private int zBase;
    private int searchRadius;
    private Level level;

    public SpreadHelper(Level level, int x, int y, int z, int radius, boolean diagonals, boolean findPositiveMatches) {
        this.level = level;
        this.xBase = x;
        this.yBase = y;
        this.zBase = z;
        this.searchRadius = radius;
        this.doDiagonals = diagonals;
        this.findPositiveMatches = findPositiveMatches;
    }

    public SpreadHelper(Level level) {
        this(level, 0, 0, 0, 0, false, true);
    }

    public void addBlock(Block block) {
        this.blocksToFind.add(block);
    }

    public void removeBlock(Block block) {
        this.blocksToFind.remove(block);
    }

    public void clearBlockCheckList() {
        this.blocksToFind.clear();
    }

    public void setPositiveSearch(boolean positive) {
        this.findPositiveMatches = positive;
    }

    public void setBase(int x, int y, int z) {
        this.xBase = x;
        this.yBase = y;
        this.zBase = z;
    }

    /**
     * @return matching coordinates, or an empty list on the client
     */
    public List<CoordHolder> findBlocks() {
        this.foundBlockCoords.clear();

        if (this.level.isClientSide) {
            return List.of();
        }

        for (int xOffset = -searchRadius; xOffset <= searchRadius; xOffset++) {
            for (int yOffset = -searchRadius; yOffset <= searchRadius; yOffset++) {
                for (int zOffset = -searchRadius; zOffset <= searchRadius; zOffset++) {
                    CoordHolder location = CoordHolder.of(xOffset + xBase, yOffset + yBase, zOffset + zBase);
                    Predicate<CoordHolder> checker = findPositiveMatches ? this::positiveMatch : this::negativeMatch;
                    if (checker.test(location)) {
                        this.foundBlockCoords.add(location);
                    }
                }
            }
        }

        return List.copyOf(foundBlockCoords);
    }

    private boolean positiveMatch(CoordHolder location) {
        BlockState state = level.getBlockState(toPos(location));
        if (!blocksToFind.contains(state.getBlock())) {
            return false;
        }
        return passesDistanceChecks(location);
    }

    private boolean negativeMatch(CoordHolder location) {
        BlockState state = level.getBlockState(toPos(location));
        if (blocksToFind.contains(state.getBlock())) {
            return false;
        }
        return passesDistanceChecks(location);
    }

    private boolean passesDistanceChecks(CoordHolder location) {
        int xLoc = location.x();
        int yLoc = location.y();
        int zLoc = location.z();

        if (checkCubeOutline) {
            return (Math.abs(yLoc - yBase) == 3 && Math.abs(xLoc - xBase) == 3)
                    || (Math.abs(zLoc - zBase) == 3 && Math.abs(yLoc - yBase) == 3)
                    || (Math.abs(zLoc - zBase) == 3 && Math.abs(xLoc - xBase) == 3);
        }

        if (onlyCheckMaxRadius) {
            if (!doDiagonals) {
                return manhattanDistance(xLoc, yLoc, zLoc) == searchRadius;
            }
            return Math.abs(xLoc - xBase) == searchRadius
                    || Math.abs(yLoc - yBase) == searchRadius
                    || Math.abs(zLoc - zBase) == searchRadius;
        }

        if (!doDiagonals && manhattanDistance(xLoc, yLoc, zLoc) > searchRadius) {
            return false;
        }

        return true;
    }

    private int manhattanDistance(int xLoc, int yLoc, int zLoc) {
        return Math.abs(xLoc - xBase) + Math.abs(yLoc - yBase) + Math.abs(zLoc - zBase);
    }

    private static BlockPos toPos(CoordHolder location) {
        return new BlockPos(location.x(), location.y(), location.z());
    }
}
