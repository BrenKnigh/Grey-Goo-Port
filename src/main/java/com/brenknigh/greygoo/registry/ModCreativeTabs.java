package com.brenknigh.greygoo.registry;

import com.brenknigh.greygoo.GreyGooMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GreyGooMod.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GREY_GOO_TAB = CREATIVE_TABS.register("grey_goo",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.greygoo"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(() -> GreyGooBlocks.GREEN_GOO_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(GreyGooBlocks.GREEN_GOO_ITEM.get());
                        output.accept(GreyGooBlocks.ORANGE_GOO_ITEM.get());
                        output.accept(GreyGooBlocks.RED_GOO_ITEM.get());
                        output.accept(GreyGooBlocks.PURPLE_GOO_ITEM.get());
                        output.accept(GreyGooBlocks.BLUE_GOO_ITEM.get());
                        output.accept(GreyGooBlocks.WHITE_GOO_ITEM.get());
                        output.accept(GreyGooBlocks.BROWN_GOO_ITEM.get());
                    })
                    .build());

    private ModCreativeTabs() {}
}
