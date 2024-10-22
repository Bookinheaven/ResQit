package org.burnknuckle.utils;

import org.jxmapviewer.OSMTileFactoryInfo;

public class EnglishTileFactoryInfo extends OSMTileFactoryInfo {
    public EnglishTileFactoryInfo() {
        super("English OSM", "https://tile.openstreetmap.org");
    }

    @Override
    public String getTileUrl(int x, int y, int zoom) {
        int invZoom = 19 - zoom;
        return String.format("https://tile.openstreetmap.org/%d/%d/%d.png", invZoom, x, y);
    }
}
