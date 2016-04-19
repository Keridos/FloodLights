package de.keridos.floodlights.util;

/**
 * Created by Keridos on 28.07.2015.
 * This Class
 */
public class RenderUtil {
    public static float[] r = new float[17];
    public static float[] g = new float[17];
    public static float[] b = new float[17];

    public static void setupColors() {   // 0-15 minecraft colors;
        for (int i = 0; i <= 16; i++) {
            switch (i) {
                case 0:
                    r[0] = 1.0F;
                    g[0] = 1.0F;
                    b[0] = 1.0F;
                    break;
                case 1:
                    r[1] = 0.84706F;
                    g[1] = 0.49804F;
                    b[1] = 0.2F;
                    break;
                case 2:
                    r[2] = 0.69804F;
                    g[2] = 0.29804F;
                    b[2] = 0.84706F;
                    break;
                case 3:
                    r[3] = 0.4F;
                    g[3] = 0.6F;
                    b[3] = 0.84706F;
                    break;
                case 4:
                    r[4] = 0.89804F;
                    g[4] = 0.89804F;
                    b[4] = 0.2F;
                    break;
                case 5:
                    r[5] = 0.49804F;
                    g[5] = 0.8F;
                    b[5] = 0.09804F;
                    break;
                case 6:
                    r[6] = 0.94902F;
                    g[6] = 0.49804F;
                    b[6] = 0.64706F;
                    break;
                case 7:
                    r[7] = 0.29804F;
                    g[7] = 0.29804F;
                    b[7] = 0.29804F;
                    break;
                case 8:
                    r[8] = 0.6F;
                    g[8] = 0.6F;
                    b[8] = 0.6F;
                    break;
                case 9:
                    r[9] = 0.29804F;
                    g[9] = 0.49804F;
                    b[9] = 0.6F;
                    break;
                case 10:
                    r[10] = 0.49804F;
                    g[10] = 0.24706F;
                    b[10] = 0.69804F;
                    break;
                case 11:
                    r[11] = 0.2F;
                    g[11] = 0.29804F;
                    b[11] = 0.69804F;
                    break;
                case 12:
                    r[12] = 0.4F;
                    g[12] = 0.29804F;
                    b[12] = 0.2F;
                    break;
                case 13:
                    r[13] = 0.4F;
                    g[13] = 0.49804F;
                    b[13] = 0.2F;
                    break;
                case 14:
                    r[14] = 0.6F;
                    g[14] = 0.2F;
                    b[14] = 0.2F;
                    break;
                case 15:
                    r[15] = 0.09804F;
                    g[15] = 0.09804F;
                    b[15] = 0.09804F;
                    break;
                case 16:
                    r[16] = 1.0F;
                    g[16] = 1.0F;
                    b[16] = 1.0F;
                    break;
            }
        }
    }

    public static int getColorAsInt(int colorvalue) {
        return Math.round(256 * 256 * 255 * r[colorvalue]) + Math.round(256 * 255 * g[colorvalue]) + Math.round(255 * b[colorvalue]);

    }
}
