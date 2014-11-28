package de.keridos.floodlights.util;

import net.minecraftforge.common.util.ForgeDirection;

import java.util.logging.Logger;

/**
 * Created by Keridos on 28/11/2014.
 * This Class
 */
public class MathUtil {

    // This is for rotation from the +x axis to another axis
    public static int[] rotate(int x, int y, int z, ForgeDirection direction) {
        int[] result = new int[3];
        switch (direction) {
            case DOWN:
                Logger.getGlobal().info("down");
                result[0] = y;
                result[1] = -x;
                result[2] = z;
            case UP:
                result[0] = -y;
                result[1] = x;
                result[2] = z;
            case NORTH:
                result[0] = z;
                result[1] = y;
                result[2] = -x;
            case SOUTH:
                result[0] = -z;
                result[1] = y;
                result[2] = x;
            case WEST:
                result[0] = -x;
                result[1] = y;
                result[2] = z;
            case EAST:
                result[0] = x;
                result[1] = y;
                result[2] = z;
        }
        return result;
    }
}
