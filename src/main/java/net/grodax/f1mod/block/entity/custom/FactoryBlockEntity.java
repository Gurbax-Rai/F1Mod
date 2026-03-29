package net.grodax.f1mod.block.entity.custom;

import net.grodax.f1mod.block.entity.ModBlockEntities;
import net.grodax.f1mod.recipe.FactoryRecipe;
import net.grodax.f1mod.recipe.ModRecipes;
import net.grodax.f1mod.screen.custom.FactoryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FactoryBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(26) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(level != null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private static final int OUTPUT_SLOT = 25;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;

    public FactoryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FACTORY_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> FactoryBlockEntity.this.progress;
                    case 1 -> FactoryBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0 -> FactoryBlockEntity.this.progress = value;
                    case 1 -> FactoryBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    // --- NEW RECIPE LOGIC ---

    private CraftingInput createInput() {
        NonNullList<ItemStack> inputItems = NonNullList.withSize(25, ItemStack.EMPTY);
        for (int i = 0; i < 25; i++) {
            inputItems.set(i, itemHandler.getStackInSlot(i));
        }
        return CraftingInput.of(5, 5, inputItems);
    }

    private boolean hasRecipe() {
        if (this.level == null) return false;

        // Ask the game if any JSON matches our 25 slots
        Optional<RecipeHolder<FactoryRecipe>> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipes.FACTORY_TYPE.get(), createInput(), level);

        if (recipe.isPresent()) {
            ItemStack result = recipe.get().value().getResultItem(level.registryAccess());
            return canInsertItemIntoOutputSlot(result) && canInsertAmountIntoOutputSlot(result.getCount());
        }
        return false;
    }

    private void craftItem() {
        if (this.level == null) return;

        Optional<RecipeHolder<FactoryRecipe>> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipes.FACTORY_TYPE.get(), createInput(), level);

        if (recipe.isPresent()) {
            // 1. Consume 1 item from every input slot
            for (int i = 0; i < 25; i++) {
                itemHandler.extractItem(i, 1, false);
            }

            // 2. Add the result to the output slot
            ItemStack result = recipe.get().value().getResultItem(level.registryAccess());
            itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                    itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
        }
    }

    // --- TICK AND HELPERS ---

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (level.isClientSide()) return;

        if(hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean canInsertItemIntoOutputSlot(ItemStack output) {
        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                ItemStack.isSameItem(this.itemHandler.getStackInSlot(OUTPUT_SLOT), output);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        int maxCount = itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ? 64 :
                itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = itemHandler.getStackInSlot(OUTPUT_SLOT).getCount();

        return maxCount >= currentCount + count;
    }

    // --- STANDARD BOILERPLATE ---

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
        pTag.putInt("factory_block.progress", progress);
        pTag.putInt("factory_block.max_progress", maxProgress);
        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        progress = pTag.getInt("factory_block.progress");
        maxProgress = pTag.getInt("factory_block.max_progress");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.f1mod.factory_block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new FactoryMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithoutMetadata(pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}