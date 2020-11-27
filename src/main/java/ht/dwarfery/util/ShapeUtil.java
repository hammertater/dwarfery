package ht.dwarfery.util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class ShapeUtil {

    // Borrowed from https://forums.minecraftforge.net/topic/74979-1144-rotate-voxel-shapes/?do=findComment&comment=391969
    public static VoxelShape rotateShape(VoxelShape shape, Direction from, Direction to) {
        VoxelShape[] buffer = new VoxelShape[]{ shape, VoxelShapes.empty() };

        int times = (to.getHorizontalIndex() - from.getHorizontalIndex() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.or(buffer[1], VoxelShapes.create(1-maxZ, minY, minX, 1-minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }

    public static VoxelShape rotateShape(VoxelShape shape, Direction to) {
        return rotateShape(shape, Direction.EAST, to);
    }
}
