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

    public static double[] sortMinMaxToMax(double[] min, double[] max) {
        double[] newMax = new double[3];
        newMax[0] = Math.max(min[0], max[0]);
        newMax[1] = Math.max(min[1], max[1]);
        newMax[2] = Math.max(min[2], max[2]);
        return newMax;
    }

    public static double[] sortMinMaxToMin(double[] min, double[] max) {
        double[] newMin = new double[3];
        newMin[0] = Math.min(min[0], max[0]);
        newMin[1] = Math.min(min[1], max[1]);
        newMin[2] = Math.min(min[2], max[2]);
        return newMin;
    }

    public static int truncateDoubleToInt(double number) {
        return (int) Math.floor(number);
    }
}
