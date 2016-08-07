package de.keridos.floodlights.util;

import de.keridos.floodlights.handler.ConfigHandler;

import java.util.Random;

/**
 * Created by Keridos on 08/02/2016.
 * This Class
 */
public class RandomUtil {
    public static Random random;
    public static int averageTickTimeoutGrowing;

    public static void init() {
        random = new Random();
        averageTickTimeoutGrowing = MathUtil.roundDoubleToInt(20/ConfigHandler.chanceGrowLight);
    }

    public static int getRandomTickTimeoutGrowing(){
        return random.nextInt(2*averageTickTimeoutGrowing)+1;
    }
}

