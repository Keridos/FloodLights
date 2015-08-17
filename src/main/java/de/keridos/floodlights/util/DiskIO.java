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
                if (path.toFile().mkdir()) {
                    throw new Exception("Failed to create dir");
                }
            }
            FileOutputStream saveFile = new FileOutputStream(fullpath.toFile());
            ObjectOutputStream save = new ObjectOutputStream(saveFile);
            save.writeObject(input);
            save.close();
            saveFile.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Files.deleteIfExists(fullpath);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static LightHandler loadFromDisk() {
        LightHandler input = null;
        try {
            FileInputStream saveFile = new FileInputStream("world/floodlights/LightHandler.sav");
            ObjectInputStream save = new ObjectInputStream(saveFile);
            input = (LightHandler) save.readObject();
            input.removeDuplicateWorlds();
            if (input.wrongVersion()) {
                input = null;
            }
            save.close();
            saveFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }
}
