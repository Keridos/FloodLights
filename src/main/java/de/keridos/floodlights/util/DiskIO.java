package de.keridos.floodlights.util;

import de.keridos.floodlights.core.LightHandler;
import net.minecraftforge.common.DimensionManager;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Created by Nico on 04/10/2014.
 */
public class DiskIO {
    public static void saveToDisk(LightHandler input){
        Path path = Paths.get(DimensionManager.getCurrentSaveRootDirectory().toString() + "/floodlights/");
        Path fullpath = Paths.get(DimensionManager.getCurrentSaveRootDirectory().toString() + "/floodlights/LightHandler.sav");
        Logger.getGlobal().info(path.toString());
        try {
            if (Files.notExists(path)){
                path.toFile().mkdir();
            }

            FileOutputStream saveFile = new FileOutputStream(fullpath.toFile());
            ObjectOutputStream save = new ObjectOutputStream(saveFile);
            save.writeObject(input);

        }
        catch (Exception e){

        }
    }

    public static LightHandler loadFromDisk(){
        LightHandler input = null;
        try {

            FileInputStream saveFile = new FileInputStream("world/floodlights/LightHandler.sav");
            ObjectInputStream save = new ObjectInputStream(saveFile);
            input = (LightHandler)save.readObject();
        }
        catch (Exception e){

        }
        return input;
    }
}
