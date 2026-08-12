package com.brenknigh.greygoo.event;

import com.brenknigh.greygoo.GreyGooMod;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * Resets spread counters each server tick, matching the original {@code CommonTickHandler}.
 */
public final class GooTickHandler {
    private GooTickHandler() {}

    public static void register() {
        NeoForge.EVENT_BUS.register(new GooTickHandler());
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        GreyGooMod.getInstance().getSpreadLimiter().resetCounters();
    }
}
