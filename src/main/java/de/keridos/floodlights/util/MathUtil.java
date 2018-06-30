package de.keridos.floodlights.util;


import net.minecraft.util.EnumFacing;

/**
 * Created by Keridos on 28/11/2014.
 * This Class
 */
public class MathUtil {

    // This is for rotation a coordinate from the +x direction to another axis around (0, 0, 0)
    public static double[] rotateD(double x, double y, double z, EnumFacing direction) {
        double[] result = new double[3];
        switch (direction) {
            case DOWN:
                result[0] = y;
                result[1] = 1 - x;
                result[2] = z;
                break;
            case UP:
                result[0] = 1 - y;
                result[1] = x;
                result[2] = z;
                break;
            case NORTH:
                result[0] = z;
                result[1] = y;
                result[2] = 1 - x;
                break;
            case SOUTH:
                result[0] = 1 - z;
                result[1] = y;
                result[2] = x;
                break;
            case WEST:
                result[0] = 1 - x;
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

    public static int[] rotate(int x, int y, int z, EnumFacing direction) {
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

    public static int truncateDoubleToInt(double number) {
        return (int) Math.floor(number);
    }

    public static int roundDoubleToInt(double number) {
        return (int) Math.floor(number + 0.5D);
    }
}
