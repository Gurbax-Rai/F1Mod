package net.grodax.f1mod.datagen;

import net.grodax.f1mod.F1Mod;
import net.grodax.f1mod.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, F1Mod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.FACTORY_BLOCK.get())
                .add(ModBlocks.TRACK_BLOCK.get()
                );

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.FACTORY_BLOCK.get())
                .add(ModBlocks.TRACK_BLOCK.get()
                );
    }
}
