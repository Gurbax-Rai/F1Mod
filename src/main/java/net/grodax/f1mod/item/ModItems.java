package net.grodax.f1mod.item;

import net.grodax.f1mod.F1Mod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // tells Minecraft that these are our items, so add them to the game
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, F1Mod.MOD_ID);

    public static final RegistryObject<Item> F1CAR = ITEMS.register("f1_car", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
