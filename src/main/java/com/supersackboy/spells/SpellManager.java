package com.supersackboy.spells;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class SpellManager {
    public static Spell[] spells;
    public static void init() {
        spells = new Spell[]{ //create the spells
                Spell.builder("fireball",(spell, player) -> {

                    HitResult blockHit = player.getCameraEntity().raycast(20.0D, 0.0f, false);
                    Vec3d blockPos = blockHit.getPos();
                    player.world.createExplosion(player, blockPos.getX(),blockPos.getY(),blockPos.getZ(),3, World.ExplosionSourceType.TNT);
                }).runes(new int[]{1}).build()
        };
    }
}
