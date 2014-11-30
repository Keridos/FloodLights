package de.keridos.floodlights.util;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Keridos on 28/11/2014.
 * This Class
 */
public class MathUtil {

    // This is for rotation a coordinate from the +x direction to another axis around (0, 0, 0)
    public static int[] rotate(int x, int y, int z, ForgeDirection direction) {
        int[] result = new int[3];
        switch (direction) {
            case DOWN:
                result[0] = y;
                result[1] = -x;
                result[2] = z;
                break;
            case UP:
                result[0] = -y;
                result[1] = x;
                result[2] = z;
                break;
            case NORTH:
                result[0] = z;
                result[1] = y;
                result[2] = -x;
                break;
            case SOUTH:
                result[0] = -z;
                result[1] = y;
                result[2] = x;
                break;
            case WEST:
                result[0] = -x;
                result[1] = y;
                result[2] = z;
                break;
            case EAST:
                result[0] = x;
                result[1] = y;
                result[2] = z;
                break;
        }
        return result;
    }
}
