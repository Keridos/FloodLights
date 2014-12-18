package de.keridos.floodlights.util;

import de.keridos.floodlights.handler.lighting.LightHandler;
import net.minecraftforge.common.DimensionManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Keridos on 04.10.14.
 * This Class handles writing the Lighthandler to the disk.
 */
public class DiskIO {
    public static void saveToDisk(LightHandler input) {
        Path path = Paths.get(DimensionManager.getCurrentSaveRootDirectory().toString() + "/floodlights/");
        Path fullpath = Paths.get(DimensionManager.getCurrentSaveRootDirectory().toString() + "/floodlights/LightHandler.sav");
        try {
            if (Files.notExists(path)) {
                path.toFile().mkdir();
            }

            FileOutputStream saveFile = new FileOutputStream(fullpath.toFile());
            ObjectOutputStream save = new ObjectOutputStream(saveFile);
            save.writeObject(input);
        } catch (Exception e) {

        }
    }

    public static LightHandler loadFromDisk() {
        LightHandler input = null;
        try {

            FileInputStream saveFile = new FileInputStream("world/floodlights/LightHandler.sav");
            ObjectInputStream save = new ObjectInputStream(saveFile);
            input = (LightHandler) save.readObject();
        } catch (Exception e) {

        }
        return input;
    }
}
