package net.grodax.f1mod.screen;

import net.grodax.f1mod.F1Mod;
import net.grodax.f1mod.screen.custom.FactoryMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, F1Mod.MOD_ID);

    public static final RegistryObject<MenuType<FactoryMenu>> FACTORY_MENU =
            MENUS.register("factory_menu", () -> IForgeMenuType.create(FactoryMenu::new));


    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
