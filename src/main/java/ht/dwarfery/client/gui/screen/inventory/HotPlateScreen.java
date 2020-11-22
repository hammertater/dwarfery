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
    public void func_231160_c_() {
        super.func_231160_c_();
        this.field_238742_p_ = (this.xSize - this.field_230712_o_.func_238414_a_(this.field_230704_d_)) / 2;
    }

    /**
     * render
     */
    public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.func_230446_a_(matrixStack); // this.renderBackground
        super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks); // super.render
        this.func_230459_a_(matrixStack, mouseX, mouseY); // this.renderHoveredToolTip
    }

    /**
     * Draw the background layer
     */
    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_230706_i_.getTextureManager().bindTexture(BACKGROUND_TEXTURE);

        int i = this.guiLeft;
        int j = this.guiTop;
        this.func_238474_b_(matrixStack, i, j, 0, 0, this.xSize, this.ySize);

        if (this.container.isBurning()) {
            int k = (int) (this.container.getBurnLeftScaled() * 13);
            this.func_238474_b_(matrixStack, i + 80, j + 24 + 12 - k, 176, 12 - k, 14, k + 1);
        }
    }

}
