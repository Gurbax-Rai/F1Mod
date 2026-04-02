package net.grodax.f1mod.item;

import net.grodax.f1mod.F1Mod;
import net.grodax.f1mod.item.custom.ChiselItem;
import net.grodax.f1mod.item.custom.F1CarItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, F1Mod.MOD_ID);

    public static final RegistryObject<Item> F1CAR = ITEMS.register("f1_car", () -> new F1CarItem(new Item.Properties()));

    public static final RegistryObject<Item> CHISEL = ITEMS.register("chisel", () -> new ChiselItem(new Item.Properties().durability(32)));

    public static final RegistryObject<Item> CARBON_FIBER = ITEMS.register("carbon_fiber", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WHEEL = ITEMS.register("wheel", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FRONT_WING = ITEMS.register("front_wing", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> REAR_WING = ITEMS.register("rear_wing", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SUSPENSION = ITEMS.register("suspension", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SIDEPOD = ITEMS.register("sidepod", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
