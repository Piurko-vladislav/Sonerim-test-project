package com.example.sonerimtestproject.model;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

public class LayerModel {

    private int mId;
    @ColorRes
    private int mColorId;
    @Nullable
    private GeoPointModel mCoordinate;

    private int valueForUi;

    public LayerModel() {
    }

    public LayerModel(int mId, int mColorId, @Nullable GeoPointModel mCoordinate) {
        this.mId = mId;
        this.mColorId = mColorId;
        this.mCoordinate = mCoordinate;
    }

    public LayerModel(int mId, int mColorId, @Nullable GeoPointModel mCoordinate, int value) {
        this.mId = mId;
        this.mColorId = mColorId;
        this.mCoordinate = mCoordinate;
        this.valueForUi = value;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getColorId() {
        return mColorId;
    }

    public void setColorId(@DrawableRes int colorId) {
        mColorId = colorId;
    }


    @Nullable
    public GeoPointModel getCoordinate() {
        return mCoordinate;
    }

    public void setCoordinate(@Nullable GeoPointModel coordinate) {
        mCoordinate = coordinate;
    }

    public int getValueForUi() {
        return valueForUi;
    }

    public void setValueForUi(int valueForUi) {
        this.valueForUi = valueForUi;
    }

    public void update(LayerModel layer) {
        if (layer == null) return;

        if (layer.getColorId() != 0) setColorId(layer.getColorId());
        if (layer.getCoordinate() != null) setCoordinate(layer.getCoordinate());
        if (layer.getId() != 0) setId(layer.getId());
    }
}
