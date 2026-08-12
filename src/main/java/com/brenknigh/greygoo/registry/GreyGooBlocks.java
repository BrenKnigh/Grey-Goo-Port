package com.brenknigh.greygoo.registry;

import com.brenknigh.greygoo.GreyGooMod;
import com.brenknigh.greygoo.block.BlueGooBlock;
import com.brenknigh.greygoo.block.BrownGooBlock;
import com.brenknigh.greygoo.block.GreenInertBlock;
import com.brenknigh.greygoo.block.OrangeWallBlock;
import com.brenknigh.greygoo.block.PurpleGooBlock;
import com.brenknigh.greygoo.block.RedCleanerBlock;
import com.brenknigh.greygoo.block.WhiteGooBlock;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class GreyGooBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(GreyGooMod.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GreyGooMod.MOD_ID);

    public static final DeferredBlock<GreenInertBlock> GREEN_GOO = BLOCKS.register("green_goo",
            () -> new GreenInertBlock(blockProperties(MapColor.COLOR_GREEN, 0.0F).lightLevel(state -> 2)));

    public static final DeferredBlock<OrangeWallBlock> ORANGE_GOO = BLOCKS.register("orange_goo",
            () -> new OrangeWallBlock(blockProperties(MapColor.COLOR_ORANGE, -1.0F)
                    .lightLevel(state -> 6)
                    .strength(-1.0F, 3600000.0F)));

    public static final DeferredBlock<RedCleanerBlock> RED_GOO = BLOCKS.register("red_goo",
            () -> new RedCleanerBlock(blockProperties(MapColor.COLOR_RED, 0.0F).lightLevel(state -> 15)));

    public static final DeferredBlock<PurpleGooBlock> PURPLE_GOO = BLOCKS.register("purple_goo",
            () -> new PurpleGooBlock(blockProperties(MapColor.COLOR_PURPLE, 0.0F).lightLevel(state -> 9)));

    public static final DeferredBlock<BlueGooBlock> BLUE_GOO = BLOCKS.register("blue_goo",
            () -> new BlueGooBlock(blockProperties(MapColor.COLOR_BLUE, 0.0F).lightLevel(state -> 9)));

    public static final DeferredBlock<WhiteGooBlock> WHITE_GOO = BLOCKS.register("white_goo",
            () -> new WhiteGooBlock(blockProperties(MapColor.SNOW, 0.0F).lightLevel(state -> 2)));

    public static final DeferredBlock<BrownGooBlock> BROWN_GOO = BLOCKS.register("brown_goo",
            () -> new BrownGooBlock(blockProperties(MapColor.COLOR_BROWN, 0.0F).lightLevel(state -> 14)));

    public static final DeferredItem<BlockItem> GREEN_GOO_ITEM = registerBlockItem(GREEN_GOO);
    public static final DeferredItem<BlockItem> ORANGE_GOO_ITEM = registerBlockItem(ORANGE_GOO);
    public static final DeferredItem<BlockItem> RED_GOO_ITEM = registerBlockItem(RED_GOO);
    public static final DeferredItem<BlockItem> PURPLE_GOO_ITEM = registerBlockItem(PURPLE_GOO);
    public static final DeferredItem<BlockItem> BLUE_GOO_ITEM = registerBlockItem(BLUE_GOO);
    public static final DeferredItem<BlockItem> WHITE_GOO_ITEM = registerBlockItem(WHITE_GOO);
    public static final DeferredItem<BlockItem> BROWN_GOO_ITEM = registerBlockItem(BROWN_GOO);

    private GreyGooBlocks() {}

    private static BlockBehaviour.Properties blockProperties(MapColor color, float hardness) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .strength(hardness)
                .sound(SoundType.SLIME_BLOCK)
                .noOcclusion();
    }

    private static <B extends Block> DeferredItem<BlockItem> registerBlockItem(DeferredBlock<B> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
