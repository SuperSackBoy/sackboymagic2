package com.supersackboy.spells;



public class SpellManager {
    public static Spell[] spells;
    public static void init() {
        spells = new Spell[]{ //create the spells
                Spell.builder("fireball",spell -> System.out.println("abcd")).build()
        };
    }
}
