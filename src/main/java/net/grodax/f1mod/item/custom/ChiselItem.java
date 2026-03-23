package net.grodax.f1mod.item.custom;

import net.grodax.f1mod.block.ModBlocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ChiselItem extends Item {
    public ChiselItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        Block clickedBlock = level.getBlockState(pContext.getClickedPos()).getBlock();

        if (!level.isClientSide() && clickedBlock == Blocks.BLACK_CONCRETE) {
            level.setBlockAndUpdate(pContext.getClickedPos(), ModBlocks.TRACK_BLOCK.get().defaultBlockState());
            pContext.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), ((ServerPlayer) pContext.getPlayer()),
                                            item -> pContext.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
            level.playSound(null, pContext.getClickedPos(), SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS);
        }

        return InteractionResult.SUCCESS;
    }
}
