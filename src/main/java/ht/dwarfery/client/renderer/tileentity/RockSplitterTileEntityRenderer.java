package ht.dwarfery.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import ht.dwarfery.block.RockSplitterBlock;
import ht.dwarfery.block.RockSplitterChiselBlock;
import ht.dwarfery.init.ModBlocks;
import ht.dwarfery.tileentity.RockSplitterTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class RockSplitterTileEntityRenderer extends TileEntityRenderer<RockSplitterTileEntity> {
    private BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();

    public RockSplitterTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(RockSplitterTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        World world = tileEntity.getWorld();
        if (world != null) {
            BlockPos blockpos = tileEntity.getPos().offset(tileEntity.getMotionDirection().getOpposite());
            BlockState blockstate = tileEntity.getChiselState();
            if (!blockstate.isAir() && !(tileEntity.getProgress(partialTicks) >= 1.0F)) {
                BlockModelRenderer.enableCache();
                matrixStack.push();
                matrixStack.translate(tileEntity.getOffsetX(partialTicks), tileEntity.getOffsetY(partialTicks), tileEntity.getOffsetZ(partialTicks));
                if (blockstate.isIn(ModBlocks.ROCK_SPLITTER_CHISEL.get()) && tileEntity.getProgress(partialTicks) <= 4.0F) {
                    func_228876_a_(blockpos, blockstate, matrixStack, buffer, world, false, combinedOverlay);
                } else if (tileEntity.shouldChiselBeRendered() && !tileEntity.isExtending()) {
                    BlockState blockstate1 = ModBlocks.ROCK_SPLITTER_CHISEL.get().getDefaultState().with(RockSplitterChiselBlock.FACING, blockstate.get(RockSplitterBlock.FACING));
                    func_228876_a_(blockpos, blockstate1, matrixStack, buffer, world, false, combinedOverlay);
                    BlockPos blockpos1 = blockpos.offset(tileEntity.getMotionDirection());
                    matrixStack.pop();
                    matrixStack.push();
                    blockstate = blockstate.with(RockSplitterBlock.EXTENDED, Boolean.TRUE);
                    func_228876_a_(blockpos1, blockstate, matrixStack, buffer, world, true, combinedOverlay);
                } else {
                    func_228876_a_(blockpos, blockstate, matrixStack, buffer, world, false, combinedOverlay);
                }

                matrixStack.pop();
                BlockModelRenderer.disableCache();
            }
        }
    }

    private void func_228876_a_(BlockPos pos, BlockState blockState, MatrixStack matrixStack, IRenderTypeBuffer buffer, World world, boolean checkSides, int combinedOverlay) {
        net.minecraft.client.renderer.RenderType.getBlockRenderTypes().stream().filter(t -> RenderTypeLookup.canRenderInLayer(blockState, t)).forEach(rendertype -> {
            net.minecraftforge.client.ForgeHooksClient.setRenderLayer(rendertype);
            IVertexBuilder ivertexbuilder = buffer.getBuffer(rendertype);
            if (blockRenderer == null) blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
            blockRenderer.getBlockModelRenderer().renderModel(world, blockRenderer.getModelForState(blockState), blockState, pos, matrixStack, ivertexbuilder, checkSides, new Random(), blockState.getPositionRandom(pos), combinedOverlay);
        });
        net.minecraftforge.client.ForgeHooksClient.setRenderLayer(null);
    }

}
