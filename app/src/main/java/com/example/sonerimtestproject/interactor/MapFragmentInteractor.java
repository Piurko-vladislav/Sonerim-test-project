package com.example.sonerimtestproject.interactor;

import android.graphics.Color;

import com.example.sonerimtestproject.model.GeoPointModel;
import com.example.sonerimtestproject.model.LayerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapFragmentInteractor {
    private List<LayerModel> randomMarkers;

    public MapFragmentInteractor() {
        randomMarkers = new ArrayList<>();
    }

    public List<LayerModel> getRandomMarkers() {
        randomMarkers = new ArrayList<>();
        for (int i = 1; i < 100; i++){
            randomMarkers.add(new LayerModel(i, Color.RED, new GeoPointModel(getRandom(49.000000, 49.999999), getRandom(32.000000, 32.999999)), i));
        }
        return randomMarkers;
    }

    private double getRandom(double min, double max){
        Random r = new Random();
        double point = min + (max - min) * r.nextDouble();
        return point;
    }
}
