package com.supersackboy.mixin;

import com.supersackboy.MagicMod;
import com.supersackboy.playerdata.IEntityDataSaver;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class ModEntityDataSaverMixin implements IEntityDataSaver {
    private NbtCompound runeData;
    private NbtCompound spellData;
    @Override
    public NbtCompound getRuneData() {
        if (this.runeData == null) {
            this.runeData = new NbtCompound();
        }
        return runeData;
    }

    @Override
    public NbtCompound getSpellData() {
        if (this.spellData == null) {
            this.spellData = new NbtCompound();
        }
        return spellData;
    }

    public int resetSpellData() {
        if(this.spellData != null) {
            this.spellData = new NbtCompound();
            return 1;
        }
        return 0;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if(runeData != null) {
            nbt.put(MagicMod.ModID + ".runedata", runeData);
        }
        if(spellData != null) {
            nbt.put(MagicMod.ModID + ".spelldata", spellData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void injectReadMethod(NbtCompound nbt, CallbackInfo ci) {
        if(nbt.contains(MagicMod.ModID + ".runedata")) {
            runeData = nbt.getCompound(MagicMod.ModID + ".runedata");
        }
        if(nbt.contains(MagicMod.ModID + ".spelldata")) {
            spellData = nbt.getCompound(MagicMod.ModID + ".spelldata");
        }
    }
}
