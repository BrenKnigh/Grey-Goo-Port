package com.brenknigh.greygoo;

import org.slf4j.Logger;

import com.brenknigh.greygoo.core.GooProtection;
import com.brenknigh.greygoo.core.SpreadLimiter;
import com.brenknigh.greygoo.event.GooTickHandler;
import com.brenknigh.greygoo.registry.GreyGooBlocks;
import com.brenknigh.greygoo.registry.ModCreativeTabs;
import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(GreyGooMod.MOD_ID)
public class GreyGooMod {
    public static final String MOD_ID = "greygoo";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static GreyGooMod instance;

    private final SpreadLimiter spreadLimiter = new SpreadLimiter();

    public GreyGooMod(IEventBus modEventBus, ModContainer modContainer) {
        instance = this;
        modEventBus.addListener(this::commonSetup);

        GreyGooBlocks.BLOCKS.register(modEventBus);
        GreyGooBlocks.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, GreyGooConfig.SPEC);
        GooTickHandler.register();
    }

    public static GreyGooMod getInstance() {
        return instance;
    }

    public SpreadLimiter getSpreadLimiter() {
        return spreadLimiter;
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(GooProtection::init);
        LOGGER.info("Grey Goo port loaded — place Green, Orange, and Red goo from the creative tab");
    }
}
