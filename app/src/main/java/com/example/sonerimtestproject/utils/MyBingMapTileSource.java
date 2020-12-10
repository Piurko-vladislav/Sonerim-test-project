package com.example.sonerimtestproject.utils;

import org.osmdroid.tileprovider.tilesource.bing.BingMapTileSource;

public class MyBingMapTileSource extends BingMapTileSource {
    /**
     * Constructor.<br> <b>Warning, the static method {@link #retrieveBingKey} should have been invoked once before constructor invocation</b>
     *
     * @param aLocale The language used with BingMap REST service to retrieve tiles.<br> If null, the system default locale is used.
     */
    public MyBingMapTileSource(String aLocale) {
        super(aLocale);
    }

    @Override
    public String getTileURLString(long pMapTileIndex) {
        try {
            return super.getTileURLString(pMapTileIndex);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

}
