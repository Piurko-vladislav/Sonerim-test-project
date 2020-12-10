package com.example.sonerimtestproject.view.contract;

import com.example.sonerimtestproject.base.BasePresenter;
import com.example.sonerimtestproject.base.BaseView;
import com.example.sonerimtestproject.model.LayerModel;

import java.util.List;

public interface MapFragmentContract {

    interface View extends BaseView {

        void showMarkers(List<LayerModel> modelList);

        void setMapLayer(LayerModel layerModel);

    }

    interface Presenter extends BasePresenter<View> {

        void loadRandomMarkers();
        void deleteMarkers();
        void updateMarkers();
        void loadMapConfiguration();
        void saveMapConfiguration();

    }
}
