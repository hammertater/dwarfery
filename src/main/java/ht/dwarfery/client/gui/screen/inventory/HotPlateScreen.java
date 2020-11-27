package ht.dwarfery.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import ht.dwarfery.DwarferyMod;
import ht.dwarfery.inventory.container.HotPlateContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class HotPlateScreen extends ContainerScreen<HotPlateContainer> {

    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(DwarferyMod.MOD_ID,"textures/gui/container/hot_plate.png");

    public HotPlateScreen(HotPlateContainer hotPlateContainer, PlayerInventory playerInventory, ITextComponent title) {
        super(hotPlateContainer, playerInventory, title);
    }

    /**
     * Used to center title text
     */
    public void init() {
        super.init();
        titleX = (xSize - font.getStringPropertyWidth(title)) / 2;
    }

    /**
     * render
     */
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    /**
     * Draw the background layer
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);

        int i = guiLeft;
        int j = guiTop;
        this.blit(matrixStack, i, j, 0, 0, xSize, ySize);

        if (this.container.isBurning()) {
            int k = (int) (this.container.getBurnLeftScaled() * 13);
            this.blit(matrixStack, i + 80, j + 24 + 12 - k, 176, 12 - k, 14, k + 1);
        }
    }

}
