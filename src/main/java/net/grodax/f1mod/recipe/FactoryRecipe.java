package net.grodax.f1mod.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public class FactoryRecipe implements Recipe<CraftingInput> {
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients;

    public FactoryRecipe(List<Ingredient> ingredients, ItemStack result) {
        // Convert the List to NonNullList for Minecraft compatibility
        this.ingredients = NonNullList.withSize(25, Ingredient.EMPTY);
        for (int i = 0; i < ingredients.size(); i++) {
            this.ingredients.set(i, ingredients.get(i));
        }
        this.result = result;
    }

    public FactoryRecipe(NonNullList<Ingredient> ingredients, ItemStack result) {
        this.ingredients = ingredients;
        this.result = result;
    }

    // Helper method to turn "Pattern + Key" into the 25-slot list
    public static NonNullList<Ingredient> ingredientsFromPattern(List<String> pattern, Map<String, Ingredient> key) {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(25, Ingredient.EMPTY);
        for (int row = 0; row < 5; row++) {
            String line = pattern.get(row);
            for (int col = 0; col < 5; col++) {
                String symbol = String.valueOf(line.charAt(col));
                // Lookup the symbol in our key map (e.g., 'A' -> Iron Ingot)
                ingredients.set(col + (row * 5), key.getOrDefault(symbol, Ingredient.EMPTY));
            }
        }
        return ingredients;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.size() != 25) return false;
        for (int i = 0; i < 25; i++) {
            // Compare the ingredient list to the input slots
            if (!ingredients.get(i).test(input.getItem(i))) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width == 5 && height == 5;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FACTORY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.FACTORY_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<FactoryRecipe> {
        private static final MapCodec<FactoryRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Codec.STRING.listOf().fieldOf("pattern").forGetter(r -> List.of()), // Pattern strings
                Codec.unboundedMap(Codec.STRING, Ingredient.CODEC).fieldOf("key").forGetter(r -> Map.of()), // Key map
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result)
        ).apply(inst, (pattern, key, result) -> {
            // This converts the JSON pattern/key into the 25-slot list on load
            return new FactoryRecipe(ingredientsFromPattern(pattern, key), result);
        }));

        private static final StreamCodec<RegistryFriendlyByteBuf, FactoryRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.ingredients,
                ItemStack.STREAM_CODEC, r -> r.result,
                FactoryRecipe::new
        );

        @Override public MapCodec<FactoryRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, FactoryRecipe> streamCodec() { return STREAM_CODEC; }
    }

}