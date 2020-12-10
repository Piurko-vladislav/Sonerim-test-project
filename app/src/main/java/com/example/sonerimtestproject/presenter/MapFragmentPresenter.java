package com.example.sonerimtestproject.presenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sonerimtestproject.interactor.MapFragmentInteractor;
import com.example.sonerimtestproject.view.contract.MapFragmentContract;

public class MapFragmentPresenter implements MapFragmentContract.Presenter {

    private MapFragmentInteractor interactor;

    @Nullable
    private MapFragmentContract.View view;

    public MapFragmentPresenter(MapFragmentInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void loadRandomMarkers() {
        if (view != null)
            view.showMarkers(interactor.getRandomMarkers());
    }

    @Override
    public void deleteMarkers() {

    }

    @Override
    public void updateMarkers() {

    }

    @Override
    public void loadMapConfiguration() {

    }

    @Override
    public void saveMapConfiguration() {

    }

    @Override
    public void takeView(@NonNull MapFragmentContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }
}
