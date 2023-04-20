package com.supersackboy.playerdata;

import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver {
    NbtCompound getRuneData();
    NbtCompound getSpellData();

    int resetSpellData();
}
