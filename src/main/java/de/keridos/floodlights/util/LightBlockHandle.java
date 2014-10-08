package de.keridos.floodlights.util;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Keridos on 03.10.14.
 * This Class is what each Phantom Light has "appended" to it, it stores the coordinates of all source blocks for it.
 */
public class LightBlockHandle {
    private ArrayList<int[]> sources = new ArrayList<int[]>();
    private int x;
    private int y;
    private int z;

    public LightBlockHandle(int xinput, int yinput, int zinput) {
        this.x = xinput;
        this.y = yinput;
        this.z = zinput;
    }

    public int[] getCoords() {
        int[] coords = {this.x, this.y, this.z};
        return coords;
    }

    public void addSource(int xcord, int ycord, int zcord) {
        boolean added = false;
        int[] source = {xcord, ycord, zcord};
        if (sources.size() == 0) {
            added = true;
            sources.add(source);
        }
        for (int[] sourceInput : sources) {
            if (sourceInput[0] == xcord && sourceInput[1] == ycord && sourceInput[2] == zcord) {
                return;
            }
        }
        if (!added) {
            sources.add(source);
        }
    }

    public void removeSource(int xcord, int ycord, int zcord) {
        if (sources == null) {
            return;
        }
        Iterator itr = sources.iterator();
        while (itr.hasNext()) {
            int[] source = (int[]) itr.next();
            if (source[0] == xcord && source[1] == ycord && source[2] == zcord) {
                itr.remove();
            }
        }
    }

    public int sourceNumber() {
        return sources.size();
    }
}
