package net.grodax.f1mod.screen.custom;

import net.grodax.f1mod.block.ModBlocks;
import net.grodax.f1mod.block.entity.custom.FactoryBlockEntity;
import net.grodax.f1mod.screen.ModMenuTypes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

// This class manages the synchronization and slot logic between the server-side
// FactoryBlockEntity and the client-side FactoryScreen. It handles the 5x5 crafting
// grid, the output slot, and standard player inventory interactions.
public class FactoryMenu extends AbstractContainerMenu {
    public final FactoryBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 26;

    // Constructor used by the client to initialize the menu with data sent over the network.
    // Parameters:
    //   pContainerId - The ID for this specific container instance.
    //   inv - The inventory of the player opening the menu.
    //   extraData - Buffer containing the BlockPos of the FactoryBlockEntity.
    public FactoryMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    // Main constructor that initializes the menu layout, adding player inventory
    // slots and the factory-specific 5x5 grid and output slots.
    // Parameters:
    //   pContainerId - The ID for this specific container instance.
    //   inv - The inventory of the player opening the menu.
    //   entity - The BlockEntity this menu is associated with.
    //   data - The synchronized integer data for crafting progress.
    public FactoryMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.FACTORY_MENU.get(), pContainerId);
        checkContainerDataCount(data, 2);
        this.blockEntity = ((FactoryBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;
        int gridStartX = 16;
        int gridStartY = 22;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        // Add the 5x5 Grid (Slots 0-24)
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                this.addSlot(new SlotItemHandler(blockEntity.itemHandler,
                        col + row * 5,
                        gridStartX + col * 18,
                        gridStartY + row * 18
                ));
            }
        }

        // Add Output Slot (Slot 25)
        this.addSlot(new SlotItemHandler(blockEntity.itemHandler, 25, 146, 55) {
            // Determines if an item can be placed into this slot via the cursor.
            // Parameters:
            //   stack - The item stack being placed.
            // Returns:
            //   false, to prevent manual placement into the output slot.
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        addDataSlots(data);
    }

    // Checks if the factory is currently in the process of crafting an item.
    // Returns:
    //   true if the progress data is greater than zero, false otherwise.
    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    // Calculates the visual length of the progress arrow based on current crafting progress.
    // Returns:
    //   An integer representing the pixel width of the progress arrow.
    public int getScaledArrowProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowPixelSize = 24;

        return maxProgress != 0 && progress != 0 ? progress * arrowPixelSize / maxProgress : 0;
    }

    // Handles Shift-Clicking items to move them quickly between the player
    // inventory and the factory slots.
    // Parameters:
    //   playerIn - The player performing the move.
    //   pIndex - The index of the slot that was clicked.
    // Returns:
    //   The remaining ItemStack in the slot after the move, or EMPTY if the move failed.
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    // Determines if the player is still close enough and valid to use this menu.
    // Parameters:
    //   pPlayer - The player using the menu.
    // Returns:
    //   true if the player can continue using the menu, false otherwise.
    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.FACTORY_BLOCK.get());
    }

    // Adds the standard 3-row player inventory slots to the menu.
    // Parameters:
    //   playerInventory - The player's inventory container.
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 125 + i * 18));
            }
        }
    }

    // Adds the player's hotbar slots to the menu.
    // Parameters:
    //   playerInventory - The player's inventory container.
    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 183));
        }
    }
}