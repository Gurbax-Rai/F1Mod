package net.grodax.f1mod.entity;

import net.grodax.f1mod.F1Mod;
import net.grodax.f1mod.entity.custom.F1CarEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, F1Mod.MOD_ID);

    public static final RegistryObject<EntityType<F1CarEntity>> F1_CAR_ENTITY =
            ENTITIES.register("f1_car_entity", () -> EntityType.Builder.<F1CarEntity>of(F1CarEntity::new, MobCategory.MISC)
                    .sized(4,2) // Width and height
                    .clientTrackingRange(10)
                    .build("f1_car_entity"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
