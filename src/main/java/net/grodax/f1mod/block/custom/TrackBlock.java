package net.grodax.f1mod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class TrackBlock extends Block {
    public TrackBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (pEntity instanceof Player player) {
            // Apply a controlled speed boost that doesn't cause infinite acceleration
            Vec3 deltaMovement = player.getDeltaMovement();

            // Only apply boost if player is moving slowly (to prevent overpowered jumping)
            double speed = Math.sqrt(deltaMovement.x * deltaMovement.x + deltaMovement.z * deltaMovement.z);
            if (speed < 0.5) { // Only boost when moving slowly
                player.setDeltaMovement(
                        deltaMovement.x * 1.3,  // Moderate boost
                        deltaMovement.y,
                        deltaMovement.z * 1.3
                );
            }
        }
        super.stepOn(pLevel, pPos, pState, pEntity);
    }
}
