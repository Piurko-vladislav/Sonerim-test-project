package com.example.sonerimtestproject.model;

import android.animation.ValueAnimator;

import androidx.annotation.Nullable;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

public class MyMarker extends Marker {

    @Nullable
    private ValueAnimator mValueAnimator;
    private String mTitle;
    private double maxHeight;
    private double maxWidth;
    private LayerModel layerModel;

    public MyMarker(MapView mapView) {
        super(mapView);
    }

    @Nullable
    public ValueAnimator getValueAnimator() {
        return mValueAnimator;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public void setValueAnimator(@Nullable ValueAnimator valueAnimator) {
        mValueAnimator = valueAnimator;
    }

    @Override
    public void setInfoWindow(MarkerInfoWindow infoWindow) {
        super.setInfoWindow(infoWindow);
    }
    //    public void setTitle(String title) {
//        mTitle = title;
//    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public double getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(double maxWidth) {
        this.maxWidth = maxWidth;
    }

    public LayerModel getLayerModel() {
        return layerModel;
    }

    public void setLayerModel(LayerModel layerModel) {
        this.layerModel = layerModel;
    }
}
