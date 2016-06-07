package de.keridos.floodlights.util;

import java.util.Random;

/**
 * Created by Keridos on 08/02/2016.
 * This Class
 */
public class RandomUtil {
    public static Random random;

    public static void init() {
        random = new Random();
    }

    public static int getRandomTickTimeoutFromFloatChance(float chance) {
        return random.nextInt(Math.round(20 / chance)) + Math.round(0.5F * 20 / chance);
    }
}
