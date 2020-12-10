package com.example.sonerimtestproject.utils;

import android.os.Parcel;
import android.os.Parcelable;

import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

public enum MapType implements Parcelable {

    OPEN_STREET_MAP("Open street map", TileSourceFactory.MAPNIK),
    OPEN_STREET_MAP_USGS_SAT("Open street map", TileSourceFactory.USGS_SAT);

    private final String mTitle;
    private final ITileSource mTileSource;

    MapType(String title, ITileSource tileSource) {
        mTitle = title;
        mTileSource = tileSource;
    }

    public static final Creator<MapType> CREATOR = new Creator<MapType>() {
        @Override
        public MapType createFromParcel(Parcel in) {
            return MapType.values()[in.readInt()];
        }

        @Override
        public MapType[] newArray(int size) {
            return new MapType[size];
        }
    };

    private static MyBingMapTileSource provideBingMapTileSource() {
        MyBingMapTileSource bingMapTileSource = new MyBingMapTileSource(null);
        bingMapTileSource.setStyle(MyBingMapTileSource.IMAGERYSET_AERIALWITHLABELS);
        return bingMapTileSource;
    }

    public static String[] getStings() {
        String[] strings = new String[MapType.values().length];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = MapType.values()[i].getTitle();
        }

        return strings;
    }

    public String getTitle() {
        return mTitle;
    }

    public static int getPosition(MapType mapType) {
        for (int i = 0; i < MapType.values().length; i++) {
            if (MapType.values()[i].equals(mapType))
                return i;
        }
        return 0;
    }

    public ITileSource getTileSource() {
        return mTileSource;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) { dest.writeInt(ordinal()); }

}
