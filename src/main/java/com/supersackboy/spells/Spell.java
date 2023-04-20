package com.supersackboy.spells;

import com.supersackboy.gui.techtree.TreeSideBar;

public class Spell {
    public String id;
    protected final CastAction onCast;
    public int[] code;
    public TreeSideBar menu;
    private Spell(String id, CastAction onCast) {
        this.id = id;
        this.onCast = onCast;
    }

    public void connect(TreeSideBar menu) {
        this.menu = menu;
        if(menu.code.length != 6) {
            this.code = new int[]{0,0,0,0,0,0};
            for(int x = 0; x < menu.code.length; x++) {
                this.code[x] = menu.code[x];
            }
        } else {
            this.code = menu.code;
        }
    }

    public void onCast() {
        this.onCast.onCast(this);
    }
    public static SpellBuilder builder(String id, CastAction onCast) {
        return new SpellBuilder(id, onCast);
    }

    public static class SpellBuilder {
        public CastAction onCast;
        public String id;
        public SpellBuilder(String id, CastAction onCast) {
            this.id = id;
            this.onCast = onCast;
        }
        public Spell build() {
            return new Spell(this.id, this.onCast);
        }
    }

    public interface CastAction {
        void onCast(Spell var1);
    }
}
