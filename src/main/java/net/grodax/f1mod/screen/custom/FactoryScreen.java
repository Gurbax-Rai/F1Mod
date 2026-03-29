package net.grodax.f1mod.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.grodax.f1mod.F1Mod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FactoryScreen extends AbstractContainerScreen<FactoryMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(F1Mod.MOD_ID,"textures/gui/factory/factory_gui.png");
    private static final ResourceLocation ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(F1Mod.MOD_ID,"textures/gui/arrow_progress.png");

    public FactoryScreen(FactoryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 176;  // Standard width (usually)
        this.imageHeight = 204; // The ACTUAL height of your drawn GUI box
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        pGuiGraphics.blit(GUI_TEXTURE, x, y, 0, 86, this.imageWidth, this.imageHeight, 256, 384);

        renderProgressArrow(pGuiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(ARROW_TEXTURE,x + 116, y + 56, 0, 0, menu.getScaledArrowProgress(), 16, 24, 16);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 8, 6, 4210752, false);

        // 'inventoryLabelY' is a variable Minecraft uses to track where the inventory text goes.
        // Since your 5x5 grid is tall, we override the Y position here (e.g., 110).
        // If 'playerInventoryTitle' is still red, use: Component.translatable("container.inventory")
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, 112, 4210752, false);
    }
}