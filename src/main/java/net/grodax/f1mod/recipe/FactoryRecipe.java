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

public class FactoryRecipe implements Recipe<FactoryInput> {
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;

    // FIX: This constructor now takes a regular List to satisfy the StreamCodec
    public FactoryRecipe(List<Ingredient> ingredients, ItemStack result) {
        this.ingredients = NonNullList.withSize(25, Ingredient.EMPTY);
        for (int i = 0; i < Math.min(ingredients.size(), 25); i++) {
            this.ingredients.set(i, ingredients.get(i));
        }
        this.result = result;
    }

    // Standard getters
    public NonNullList<Ingredient> getIngredients() { return ingredients; }
    public ItemStack getResult() { return result; }

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

    @Override
    public ItemStack assemble(FactoryInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() { return ModRecipes.FACTORY_SERIALIZER.get(); }
    @Override
    public RecipeType<?> getType() { return ModRecipes.FACTORY_TYPE.get(); }

    public static class Serializer implements RecipeSerializer<FactoryRecipe> {
        public static final MapCodec<FactoryRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Codec.STRING.listOf().fieldOf("pattern").forGetter(r -> List.of()),
                Codec.unboundedMap(Codec.STRING, Ingredient.CODEC).fieldOf("key").forGetter(r -> Map.of()),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(FactoryRecipe::getResult)
        ).apply(inst, (pattern, key, result) -> new FactoryRecipe(ingredientsFromPattern(pattern, key), result)));

        // FIX: ByteBufCodecs.list() now maps perfectly to our (List, ItemStack) constructor
        public static final StreamCodec<RegistryFriendlyByteBuf, FactoryRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
                FactoryRecipe::getIngredients, // Returns NonNullList (which is a List), so this is OK
                ItemStack.STREAM_CODEC,
                FactoryRecipe::getResult,
                FactoryRecipe::new // Calls the (List, ItemStack) constructor
        );

        @Override public MapCodec<FactoryRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, FactoryRecipe> streamCodec() { return STREAM_CODEC; }
    }
}