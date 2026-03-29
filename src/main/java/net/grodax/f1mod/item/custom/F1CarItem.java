package net.grodax.f1mod.item.custom;

import net.grodax.f1mod.client.renderer.item.F1CarItemRenderer;
import net.grodax.f1mod.entity.ModEntities;
import net.grodax.f1mod.entity.custom.F1CarEntity;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class F1CarItem extends Item {
    public F1CarItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (!level.isClientSide) {
            BlockPos pos = context.getClickedPos();

            F1CarEntity car = new F1CarEntity(
                    level,
                    pos.getX() + 0.5,
                    pos.getY() + 1,
                    pos.getZ() + 0.5
            );

            car.setYRot(context.getPlayer().getYRot()); // IMPORTANT

            level.addFreshEntity(car);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new F1CarItemRenderer();
            }
        });
    }
}
