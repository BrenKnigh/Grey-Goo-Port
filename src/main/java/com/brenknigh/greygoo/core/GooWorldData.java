package com.brenknigh.greygoo.core;

import com.brenknigh.greygoo.GreyGooMod;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

/**
 * Persists global goo state ({@code GooActive}, {@code TGDbloom}) from the original mod.
 */
public class GooWorldData extends SavedData {
    private static final String ID = GreyGooMod.MOD_ID + "_world_data";

    private boolean gooActive = true;
    private boolean tgdBloom = false;

    public static GooWorldData get(ServerLevel level) {
        DimensionDataStorage storage = level.getDataStorage();
        return storage.computeIfAbsent(new Factory<>(GooWorldData::new, GooWorldData::load, null), ID);
    }

    public static GooWorldData get(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return get(serverLevel);
        }
        throw new IllegalStateException("GooWorldData can only be accessed on the server");
    }

    private static GooWorldData load(CompoundTag tag, HolderLookup.Provider provider) {
        GooWorldData data = new GooWorldData();
        data.gooActive = !tag.contains("GooActive") || tag.getBoolean("GooActive");
        data.tgdBloom = tag.getBoolean("TGDbloom");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putBoolean("GooActive", gooActive);
        tag.putBoolean("TGDbloom", tgdBloom);
        return tag;
    }

    public boolean isGooActive() {
        return gooActive;
    }

    public void setGooActive(boolean gooActive) {
        this.gooActive = gooActive;
        setDirty();
    }

    public boolean hasTgdBloomed() {
        return tgdBloom;
    }

    public void setTgdBloom(boolean tgdBloom) {
        this.tgdBloom = tgdBloom;
        setDirty();
    }
}
