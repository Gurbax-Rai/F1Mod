package net.grodax.f1mod.block.entity;

import net.grodax.f1mod.F1Mod;
import net.grodax.f1mod.block.ModBlocks;
import net.grodax.f1mod.block.entity.custom.FactoryBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, F1Mod.MOD_ID);

    public static final RegistryObject<BlockEntityType<FactoryBlockEntity>> FACTORY_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("factory_block_entity", () -> BlockEntityType.Builder.of(
                    FactoryBlockEntity::new, ModBlocks.FACTORY_BLOCK.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}