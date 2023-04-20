package com.supersackboy.spells;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;


public class SpellManager {
    public static Spell[] spells;
    public static void init() {
        spells = new Spell[]{
                Spell.builder("fireball",spell -> System.out.println("abcd")).build()
        };
    }
}
