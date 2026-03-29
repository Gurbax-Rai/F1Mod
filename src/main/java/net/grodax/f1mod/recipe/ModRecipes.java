package net.grodax.f1mod.recipe;

import net.grodax.f1mod.F1Mod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, F1Mod.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, F1Mod.MOD_ID);

    // This now points to your new Serializer class
    public static final RegistryObject<RecipeSerializer<FactoryRecipe>> FACTORY_SERIALIZER =
            SERIALIZERS.register("factory_crafting", FactoryRecipe.Serializer::new);

    public static final RegistryObject<RecipeType<FactoryRecipe>> FACTORY_TYPE =
            TYPES.register("factory_crafting", () -> new RecipeType<>() {
                @Override
                public String toString() { return "factory_crafting"; }
            });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
