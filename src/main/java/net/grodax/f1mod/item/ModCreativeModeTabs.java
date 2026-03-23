package net.grodax.f1mod.item;

import net.grodax.f1mod.F1Mod;
import net.grodax.f1mod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, F1Mod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> F1_TAB = CREATIVE_MODE_TABS.register("f1_tab", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.F1CAR.get()))
            .title(Component.translatable("creativetab.f1mod.f1_tab"))
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(ModItems.F1CAR.get());
                pOutput.accept(ModBlocks.FACTORY_BLOCK.get());
                pOutput.accept(ModItems.CHISEL.get());
                pOutput.accept(ModBlocks.TRACK_BLOCK.get());
            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
