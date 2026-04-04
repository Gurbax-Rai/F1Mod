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

// This class represents a custom recipe for the Factory block.
// It defines a 5x5 grid of ingredients required to produce a specific
// result item, supporting pattern-based definitions similar to crafting tables.
public class FactoryRecipe implements Recipe<FactoryInput> {
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;

    // Constructs a new FactoryRecipe with a list of ingredients and a result stack.
    // Parameters:
    //   ingredients - A list of ingredients to be mapped to the 5x5 grid.
    //   result - The item stack produced when this recipe is completed.
    public FactoryRecipe(List<Ingredient> ingredients, ItemStack result) {
        this.ingredients = NonNullList.withSize(25, Ingredient.EMPTY);
        for (int i = 0; i < Math.min(ingredients.size(), 25); i++) {
            this.ingredients.set(i, ingredients.get(i));
        }
        this.result = result;
    }

    // Retrieves the list of ingredients required for this recipe.
    // Returns:
    //   A NonNullList containing the 25 ingredients (including empty slots).
    public NonNullList<Ingredient> getIngredients() { return ingredients; }

    // Retrieves the output item stack for this recipe.
    // Returns:
    //   The result ItemStack.
    public ItemStack getResult() { return result; }

    // Converts a list of pattern strings and an ingredient key map into a
    // flat list of 25 ingredients for the 5x5 grid.
    // Parameters:
    //   pattern - A list of 5 strings, each 5 characters long, representing the grid.
    //   key - A map linking single-character symbols in the pattern to Ingredients.
    // Returns:
    //   A NonNullList of 25 ingredients mapped from the pattern.
    public static NonNullList<Ingredient> ingredientsFromPattern(List<String> pattern, Map<String, Ingredient> key) {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(25, Ingredient.EMPTY);
        for (int row = 0; row < 5; row++) {
            String line = pattern.get(row);
            for (int col = 0; col < 5; col++) {
                String symbol = String.valueOf(line.charAt(col));
                if (!symbol.equals(" ")) {
                    ingredients.set(col + (row * 5), key.getOrDefault(symbol, Ingredient.EMPTY));
                }
            }
        }
        return ingredients;
    }

    // Checks if the items provided in the FactoryInput match the requirements of this recipe.
    // Parameters:
    //   input - The current items placed in the factory's input slots.
    //   level - The world level where the check is occurring.
    // Returns:
    //   true if every slot in the input matches the recipe's ingredients; false otherwise.
    @Override
    public boolean matches(FactoryInput input, Level level) {
        if (input.size() != 25) return false;
        for (int i = 0; i < 25; i++) {
            Ingredient ingredient = ingredients.get(i);
            ItemStack stack = input.getItem(i);
            if (ingredient.isEmpty()) {
                if (!stack.isEmpty()) return false;
            } else {
                if (!ingredient.test(stack)) return false;
            }
        }
        return true;
    }

    // Creates the resulting item stack based on the provided input.
    // Parameters:
    //   input - The items used to craft the recipe.
    //   registries - Provider for registry-specific data.
    // Returns:
    //   A copy of the recipe's result ItemStack.
    @Override
    public ItemStack assemble(FactoryInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    // Determines if this recipe can fit within the given dimensions.
    // Parameters:
    //   width - The width of the crafting area.
    //   height - The height of the crafting area.
    // Returns:
    //   Always true, as the factory dimensions are fixed.
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    // Retrieves the base result item for display purposes (e.g., in JEI or Recipe Books).
    // Parameters:
    //   registries - Provider for registry-specific data.
    // Returns:
    //   The result ItemStack.
    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    // Retrieves the serializer used to encode/decode this recipe.
    // Returns:
    //   The FactoryRecipe serializer.
    @Override
    public RecipeSerializer<?> getSerializer() { return ModRecipes.FACTORY_SERIALIZER.get(); }

    // Retrieves the recipe type category for this recipe.
    // Returns:
    //   The FactoryRecipe type.
    @Override
    public RecipeType<?> getType() { return ModRecipes.FACTORY_TYPE.get(); }

    // This inner class handles the serialization and deserialization of FactoryRecipes
    // to and from JSON (via Codecs) and network packets (via StreamCodecs).
    public static class Serializer implements RecipeSerializer<FactoryRecipe> {

        // The MapCodec used for data-driven recipe loading from JSON files.
        public static final MapCodec<FactoryRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Codec.STRING.listOf().fieldOf("pattern").forGetter(r -> List.of()),
                Codec.unboundedMap(Codec.STRING, Ingredient.CODEC).fieldOf("key").forGetter(r -> Map.of()),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(FactoryRecipe::getResult)
        ).apply(inst, (pattern, key, result) -> new FactoryRecipe(ingredientsFromPattern(pattern, key), result)));

        // The StreamCodec used for syncing recipe data between the server and the client.
        public static final StreamCodec<RegistryFriendlyByteBuf, FactoryRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
                FactoryRecipe::getIngredients,
                ItemStack.STREAM_CODEC,
                FactoryRecipe::getResult,
                FactoryRecipe::new
        );

        // Retrieves the JSON codec for the recipe.
        // Returns:
        //   A MapCodec for FactoryRecipe.
        @Override public MapCodec<FactoryRecipe> codec() { return CODEC; }

        // Retrieves the network stream codec for the recipe.
        // Returns:
        //   A StreamCodec for FactoryRecipe.
        @Override public StreamCodec<RegistryFriendlyByteBuf, FactoryRecipe> streamCodec() { return STREAM_CODEC; }
    }
}