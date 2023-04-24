package com.supersackboy.spells;

import com.supersackboy.gui.techtree.TreeSideBar;
import net.minecraft.server.network.ServerPlayerEntity;

public class Spell {
    public String id;
    protected final CastAction onCast;
    public int[] code;
    private Spell(String id, CastAction onCast) {
        this.id = id;
        this.onCast = onCast;
    }

    public void setCode(int[] runes) {
        if(runes.length != 6) {
            this.code = new int[]{0,0,0,0,0,0};
            for(int x = 0; x < runes.length; x++) {
                this.code[x] = runes[x];
            }
        } else {
            this.code = runes;
        }
    }

    public void onCast(ServerPlayerEntity player) {
        this.onCast.onCast(this, player);
    }
    public static SpellBuilder builder(String id, CastAction onCast) {
        return new SpellBuilder(id, onCast);
    }

    public static class SpellBuilder {
        public CastAction onCast;
        public String id;
        public int[] runes = {1};
        public SpellBuilder(String id, CastAction onCast) {
            this.id = id;
            this.onCast = onCast;
        }
        public SpellBuilder runes(int[] runes) {
            this.runes = runes;
            return this;
        }
        public Spell build() {
            Spell spell = new Spell(this.id, this.onCast);
            spell.setCode(this.runes);
            return spell;
        }
    }

    public interface CastAction {
        void onCast(Spell spell, ServerPlayerEntity player);
    }
}
